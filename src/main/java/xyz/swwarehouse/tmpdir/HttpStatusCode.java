package xyz.swwarehouse.tmpdir;

public enum HttpStatusCode {
	TOO_LONG_FILENAME(400), INVAILD_FILENAME(400);

	private final int code;

	private HttpStatusCode(int code) {
		this.code = code;
	}

	public int vaule() {
		return code;
	}
}
