package xyz.swwarehouse.tmpdir.service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import xyz.swwarehouse.tmpdir.entity.FileInfo;
import xyz.swwarehouse.tmpdir.repository.FileInfoRepository;

@Service
public class FileUploadService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadService.class);

	@Value("${tmpdir.file.expire-term-day}")
	private int expireTermDay;
	private final FileInfoRepository fileRepository;

	@Autowired
	public FileUploadService(FileInfoRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

	public String createId() {
		UUID uuid = UUID.randomUUID();
		LOGGER.info("Cerate UUID({})", uuid);
		return uuid.toString();
	}

	public long createExpireTime(final long submisstionTime) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(submisstionTime));
		c.add(Calendar.DATE, expireTermDay);
		LOGGER.info("Cerate expireTime({}) using submissionTime({}) and expireTermDay({})", c.getTime(), submisstionTime, expireTermDay);
		return c.getTime().getTime();
	}

	public void saveFileInfoInRepo(final FileInfo fileInfo) {
		LOGGER.info("Save fileInfo({}) in repo", fileInfo);
		fileRepository.save(fileInfo);
	}
}