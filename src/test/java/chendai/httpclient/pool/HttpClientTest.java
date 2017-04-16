package chendai.httpclient.pool;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

public class HttpClientTest {
	public static void main(String[] args) throws Exception {
		HttpClient httpClient = HttpClient.getIstance();
		HttpReq req = new HttpReq();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		req.setUrl(URI.create("http://www.ucdrs.superlib.net:80/"));
		httpClient.send(req, new HttpReqCallback() {
			
			@Override
			public void onSuccess(HttpReq req, HttpResp resp) {
				System.out.println(resp.getStatus());
				countDownLatch.countDown();
			}
			
			@Override
			public void onExcption(HttpReq req, Throwable throwable) {
				throwable.printStackTrace();
				countDownLatch.countDown();
			}
		});
		
		countDownLatch.await();
		httpClient.close();
	}
}
