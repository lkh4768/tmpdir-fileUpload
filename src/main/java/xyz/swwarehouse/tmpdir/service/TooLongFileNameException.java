package xyz.swwarehouse.tmpdir.service;

public class TooLongFileNameException extends FileUploadException {

	public TooLongFileNameException(String message) {
		super(message);
	}

	public TooLongFileNameException(String message, Throwable cause) {
		super(message, cause);
	}
}
