import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TrustAllSSLSocketFactory extends SSLSocketFactory
{
    private static class TrustAllTrustManager
        implements X509TrustManager
    {

        public void checkClientTrusted(X509Certificate ax509certificate[], String s1)
            throws CertificateException
        {
        }

        public void checkServerTrusted(X509Certificate ax509certificate[], String s1)
            throws CertificateException
        {
        }

        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[0];
        }

        private TrustAllTrustManager()
        {
        }

    }


    public TrustAllSSLSocketFactory()
    {
        try
        {
            SSLContext sslcontent = SSLContext.getInstance("TLS");
            sslcontent.init(null, new TrustManager[] {
                new TrustAllTrustManager()
            }, new SecureRandom());
            _factory = sslcontent.getSocketFactory();
        }
        catch(Throwable t)
        {
        	//do nothing
        }
    }

    public static SocketFactory getDefault()
    {
        return new TrustAllSSLSocketFactory();
    }

    public Socket createSocket(Socket socket, String s, int i, boolean flag)
        throws IOException
    {
        return _factory.createSocket(socket, s, i, flag);
    }

    public Socket createSocket(InetAddress inaddr, int i, InetAddress inaddr2, int j)
        throws IOException
    {
        return _factory.createSocket(inaddr, i, inaddr2, j);
    }

    public Socket createSocket(InetAddress inaddr, int i)
        throws IOException
    {
        return _factory.createSocket(inaddr, i);
    }

    public Socket createSocket(String s, int i, InetAddress inaddr, int j)
        throws IOException
    {
        return _factory.createSocket(s, i, inaddr, j);
    }

    public Socket createSocket(String s, int i)
        throws IOException
    {
        return _factory.createSocket(s, i);
    }

    public String[] getDefaultCipherSuites()
    {
        return _factory.getSupportedCipherSuites();
    }

    public String[] getSupportedCipherSuites()
    {
        return _factory.getSupportedCipherSuites();
    }

    private SSLSocketFactory _factory;

}