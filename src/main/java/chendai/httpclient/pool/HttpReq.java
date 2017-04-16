package chendai.httpclient.pool;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpReq {
	private URI url;
	private Map<String, String> httpHeaders = new HashMap<>();
	private byte[] body = new byte[0];
	
	public URI getUrl() {
		return url;
	}
	public void setUrl(URI url) {
		this.url = url;
	}
	public Map<String, String> getHttpHeaders() {
		return httpHeaders;
	}
	public void setHttpHeaders(Map<String, String> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
}
