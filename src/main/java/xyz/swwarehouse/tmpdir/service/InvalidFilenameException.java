package xyz.swwarehouse.tmpdir.service;

public class InvalidFilenameException extends FileUploadException {

	public InvalidFilenameException(String message) {
		super(message);
	}

	public InvalidFilenameException(String message, Throwable cause) {
		super(message, cause);
	}
}
