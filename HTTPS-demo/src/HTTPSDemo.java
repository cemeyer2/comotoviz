import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class HTTPSDemo {
	public static void main(String[] args) throws IOException
	{
		
		final String login ="netid";
		final String password ="password";

		Authenticator.setDefault(new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication (login, password.toCharArray());
		    }
		});
		
		
		String url = "https://maggie.cs.illinois.edu/private";
		URL u = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
		
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		String str = "";
		while((str = in.readLine()) != null)
		{
			System.out.println(str);
		}
		
		in.close();
		conn.disconnect();
	}
}
