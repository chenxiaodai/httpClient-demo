package chendai.httpclient.pool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpClient {
	private CloseableHttpClient httpClient;
	private static volatile HttpClient instance;  
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50); 
	
	private HttpClient(){
		init();
		
	}
	
    public static HttpClient getIstance() { 
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient();   
                }   
            }   
        }   
        return instance;   
    }   
	
	private void init(){
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(100);
		cm.setDefaultMaxPerRoute(50);
		httpClient = HttpClients.custom().setConnectionManager(cm)
				.build();
	}
	
	public void send(final HttpReq req, final HttpReqCallback callback) throws Exception{
		   
		try {
			
			fixedThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					HttpPost httpPost = new HttpPost(req.getUrl());
					for(String header:req.getHttpHeaders().keySet()){
						httpPost.setHeader(header, req.getHttpHeaders().get(header));
					}
					httpPost.setEntity(new ByteArrayEntity(req.getBody()));
					   
					CloseableHttpResponse response = null;
					try {
						response= httpClient.execute(httpPost);
						HttpResp resp = new HttpResp();
						resp.setStatus(response.getStatusLine().getStatusCode());
	            	   
	            	   	Map<String,String> headerMap = new HashMap<>();
	            	   	for(Header header : response.getAllHeaders()){
	            	   		headerMap.put(header.getName(), header.getValue());
	            	   	}
	            	   	resp.setHttpHeaders(headerMap);
	            	   	HttpEntity entity = response.getEntity();
	            	   	resp.setBody(EntityUtils.toByteArray(entity));
	            	   	callback.onSuccess(req, resp);
		              
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						HttpClientUtils.closeQuietly(response);
					}					
				}
			});
			

           } catch (Exception e) {
              callback.onExcption(req, e);
           }
	}
	
	public void close(){
		  HttpClientUtils.closeQuietly(httpClient);
	}
	
}
