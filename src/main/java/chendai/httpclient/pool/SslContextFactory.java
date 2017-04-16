package chendai.httpclient.pool;

import java.io.File;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;

public class SslContextFactory {
	private static SSLContext clientContext;
	{
		try {
			clientContext=SSLContexts.custom().loadKeyMaterial(new File("keyStorePath"),"keyStorePass".toCharArray(), "keyPass".toCharArray())
			.loadTrustMaterial(new TrustSelfSignedStrategy()).build();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static SSLContext getClientContext(){		
		return clientContext;
	}

}