package xyz.swwarehouse.tmpdir.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import xyz.swwarehouse.tmpdir.HttpStatusCode;
import xyz.swwarehouse.tmpdir.service.FileUploadException;
import xyz.swwarehouse.tmpdir.service.InvalidFilenameException;
import xyz.swwarehouse.tmpdir.service.TooLongFileNameException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(TooLongFileNameException.class)
	void handleTooLongFileNameException(TooLongFileNameException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatusCode.TOO_LONG_FILENAME.vaule(), e.getMessage());
	}

	@ExceptionHandler(InvalidFilenameException.class)
	void handleInvaildFilenameException(InvalidFilenameException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatusCode.INVAILD_FILENAME.vaule(), e.getMessage());
	}

	@ExceptionHandler(FileUploadException.class)
	void handleFileUploadException(FileUploadException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatusCode.INVAILD_FILENAME.vaule(), e.getMessage());
	}
}
