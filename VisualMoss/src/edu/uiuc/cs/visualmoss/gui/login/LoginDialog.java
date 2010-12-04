package edu.uiuc.cs.visualmoss.gui.login;

import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.net.VisualMossAuthenticator;
import edu.uiuc.cs.visualmoss.utility.ldap.LDAPAuth;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Authenticator;

public class LoginDialog extends JDialog implements ActionListener, WindowListener
{
	private final int DIALOG_WIDTH = 400;
	private final int DIALOG_HEIGHT = 200;
	
	private JTextField netidField;
	private JPasswordField passwordField;
	private JButton loginButton;

    private String netId;
    private String password;

	public LoginDialog(JFrame owner)
	{
		super(owner, true); //make this modal
		init();
	}
	
	public LoginDialog()
	{
		super();
		init();
	}
	
	private void init()
	{
		this.setTitle("Login");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,1));
		panel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
		
		netidField = new JTextField();
		netidField.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
		TitledBorder b1 = BorderFactory.createTitledBorder("Netid");
		b1.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
		netidField.setBorder(b1);
		
		passwordField = new JPasswordField();
		TitledBorder b2 = BorderFactory.createTitledBorder("Active Directory Password");
		b2.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
		passwordField.setBorder(b2);
		
		loginButton = new JButton("Login");
		loginButton.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
		loginButton.addActionListener(this);
		
		panel.add(netidField);
		panel.add(passwordField);
		panel.add(loginButton);
		
		this.setContentPane(panel);
		this.addWindowListener(this);
		
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	public void actionPerformed(ActionEvent arg0) 
	{
		netId = netidField.getText();
		password = new String(passwordField.getPassword());

		//changed this to use direct LDAP auth rather than query the http server to auth since on unsuccessful auth
		//against the http server, it keeps trying until the max redirects limit is reached, which unfortunately
		//caused the UIUC AD to lock the account, this way we only hit the AD once and won't lock out the account
		//unless many unsuccessful tries are made
		boolean auth = LDAPAuth.authenticate(netId, password);
		
		if(auth)
		{
			VisualMossAuthenticator authenticator = new VisualMossAuthenticator();
			authenticator.setDefaultAuthentication(netId, password);
			Authenticator.setDefault(authenticator);
			
			this.setVisible(false);
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Invalid netid or AD password or your AD account may be locked", "Login Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

    public String getNetId() {
        return netId;
    }

    public String getPassword() {
        return password;
    }
}
