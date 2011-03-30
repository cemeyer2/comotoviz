/*
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright (c) 2011 University of Illinois at Urbana-Champaign.
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal with the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimers.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimers in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     * Neither the names of the CoMoTo Project team, the University of
 *       Illinois at Urbana-Champaign, nor the names of its contributors
 *       may be used to endorse or promote products derived from this
 *       Software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
 */

package edu.illinois.comoto.viz.view;

import edu.illinois.comoto.api.object.Student;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/16/11
 * Time:    1:41 PM
 * Package: edu.illinois.comoto.viz.view
 * Created by IntelliJ IDEA.
 */
public class StudentInfoDialog extends JDialog {

    private Student student;

    public StudentInfoDialog(Student student) {
        super();
        this.student = student;
        initialize();
    }

    private void initialize() {
        setTitle(FrontendConstants.STUDENT_INFO_DIALOG_TITLE + student.getNetid());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(FrontendConstants.STUDENT_INFO_DIALOG_WIDTH, FrontendConstants.STUDENT_INFO_DIALOG_HEIGHT));
        JScrollPane tablePane = getJTable();
        add(tablePane, BorderLayout.CENTER);
        // Change the program icon
        setIconImage(BackendConstants.PROGRAM_IMAGE);

        // Display the dialog
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private JScrollPane getJTable() {

        String[] columns = {"Field", "Data"};
        Map<String, Object[]> directoryInfo = student.getDirectoryInfo();
        List<Object[]> dataList = new LinkedList<Object[]>();
        for (String field : directoryInfo.keySet()) {
            for (Object data : directoryInfo.get(field)) {
                Object[] arr = {field, data};
                dataList.add(arr);
            }
        }
        Object[][] data = new Object[dataList.size()][2];
        int i = 0;
        for (Object[] array : dataList) {
            String field = (String) array[0];
            String colData = (String) array[1];
            data[i][0] = field;
            data[i][1] = colData;
            i = i + 1;
        }
        JTable table = new JTable(data, columns);
        JScrollPane pane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        Enumeration<TableColumn> cols = table.getColumnModel().getColumns();
        while (cols.hasMoreElements()) {
            TableColumn col = cols.nextElement();
            col.setCellRenderer(new CustomRenderer());
        }
        return pane;
    }


    //adopted from http://www.roseindia.net/javatutorials/multi_line_cells_in_jtable.shtml
    private class CustomRenderer extends JTextArea implements TableCellRenderer {

        public CustomRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((String) obj);
            int height_wanted = (int) getPreferredSize().getHeight();
            if (height_wanted != table.getRowHeight(row)) {
                table.setRowHeight(row, height_wanted);
            }
            return this;
        }
    }

}
