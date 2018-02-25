package xyz.swwarehouse.tmpdir.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import xyz.swwarehouse.tmpdir.entity.FileInfo;
import xyz.swwarehouse.tmpdir.service.FileUploadService;
import xyz.swwarehouse.tmpdir.service.StorageService;
import xyz.swwarehouse.tmpdir.util.Util;

@CrossOrigin
@RestController
public class FileUploadController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);
	private FileUploadService fileUploadService;
	private StorageService storageService;

	@Autowired
	public FileUploadController(FileUploadService fileUploadService, StorageService storageService) {
		this.fileUploadService = fileUploadService;
		this.storageService = storageService;
	}

	@PostMapping("/")
	public ResponseEntity<FileInfo> uploadFile(MultipartHttpServletRequest req) throws Exception {
		LOGGER.info("{}", Util.requestInfoToString(req));
		Iterator<String> itr = req.getFileNames();

		FileInfo fileInfo = new FileInfo();
		fileInfo.setId(fileUploadService.createId());
		fileInfo.setSubmissionTime(new Date().getTime());
		fileInfo.setExpireTime(fileUploadService.createExpireTime(fileInfo.getSubmissionTime()));

		while (itr.hasNext()) {
			String uploadedFile = itr.next();
			MultipartFile file = req.getFile(uploadedFile);
			String decodedOriginalFilename = URLDecoder.decode(file.getOriginalFilename(), StandardCharsets.UTF_8.name());
			LOGGER.info("Upload file({}), name({})", fileInfo, decodedOriginalFilename);
			storageService.storeFile(fileInfo.getId(), decodedOriginalFilename, file.getInputStream());
		}

		fileUploadService.saveFileInfoInRepo(fileInfo);
		LOGGER.info("Response ({}->{}), Uploaded file info({})", Util.getLocalInfo(req), Util.getClientInfo(req), fileInfo);
		return new ResponseEntity<>(fileInfo, HttpStatus.OK);
	}
}
