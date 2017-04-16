package chendai.httpclient.pool;

public interface HttpReqCallback {
	void onSuccess(HttpReq req, HttpResp resp);
	void onExcption(HttpReq req, Throwable throwable);
}
