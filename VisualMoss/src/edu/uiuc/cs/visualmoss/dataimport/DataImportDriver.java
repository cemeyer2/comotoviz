package edu.uiuc.cs.visualmoss.dataimport;

import edu.uiuc.cs.visualmoss.utility.Pair;
import prefuse.data.io.DataIOException;

import java.sql.SQLException;

/**
 * A handle on the data import class to test its creation.
 *
 * @author jon
 */
public class DataImportDriver {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, DataIOException {
		new DataImport(new Pair<String, String>("username", "password"));
	}
}
