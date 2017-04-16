package chendai.httpclient.pool;

import java.util.Map;

public class HttpResp {
	private int status;
	private Map<String, String> httpHeaders;
	private byte[] body;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
