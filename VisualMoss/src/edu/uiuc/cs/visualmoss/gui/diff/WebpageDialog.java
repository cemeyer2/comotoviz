package edu.uiuc.cs.visualmoss.gui.diff;

import java.awt.Frame;
import java.net.MalformedURLException;

import org.lobobrowser.gui.*;
import org.lobobrowser.main.*;

import javax.swing.*;

public class WebpageDialog extends JDialog {
	private static final int DIALOG_WIDTH = 950;
	private static final int DIALOG_HEIGHT = 650;

	private BrowserPanel browserPanel;
	private FramePanel framePanel;

	// Creates a modal webpage dialog
	public WebpageDialog(Frame owner, String url, boolean showNavigation) throws MalformedURLException {
		super(owner, true);
		init(url, showNavigation);
	}

	// Creates a modeless webpage dialog
	public WebpageDialog(String url, boolean showNavigation) throws MalformedURLException {
		super();
		init(url, showNavigation);
	}

	private void init(String url, boolean showNavigation) throws MalformedURLException {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

		if(showNavigation)
		{
			browserPanel = new BrowserPanel();
			this.getContentPane().add(browserPanel);
		}
		else
		{
			framePanel = new FramePanel();
			this.getContentPane().add(framePanel);
		}

		setUrl(url);
		this.setVisible(true);
		this.setLocationRelativeTo(null);

	}

	public void setUrl(String url) throws MalformedURLException {
		if(browserPanel != null)
		{
			browserPanel.navigate(url);
		}
		else
		{
			framePanel.navigate(url);
		}
	}
}