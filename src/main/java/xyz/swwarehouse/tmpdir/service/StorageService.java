package xyz.swwarehouse.tmpdir.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StorageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);
	private static final int MAX_FILE_NAME_LEN = 255;

	@Value("${tmpdir.file.root-path}")
	private Path rootPath;

	@Autowired
	public StorageService() {}

	public boolean storeFile(final String dirname, final String filename, final InputStream fileContent) {
		LOGGER.debug("Store file(dirname: {}, name: {})", dirname, filename);
		try {
			if (!IsValdirnameFileContent(fileContent)) {
				LOGGER.error("File(dirname: {}, name: {})'s Content is null", dirname, filename);
				throw new FileUploadException("Content of file is null");
			}

			if (!IsValdirnameFilename(filename)) {
				LOGGER.error("Invaldirname file(dirname: {}, name: {})'s name ", dirname, filename);
				throw new InvalidFilenameException("Invaldirname Filename");
			}

			if (!IsValdirnameFilenameLength(filename)) {
				LOGGER.error("Exceeded maximun length({}) file(dirname: {}, name: {})'s name length({})", MAX_FILE_NAME_LEN,
						dirname, filename, filename.length());
				throw new TooLongFileNameException("Exceeded maximun filename length (" + MAX_FILE_NAME_LEN + ")");
			}
			Path baseDirPath = createDir(dirname);
			Files.copy(fileContent, baseDirPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			LOGGER.error("Failed to strore file(dirname: {}, name: {})", dirname, filename, e);
			throw new FileUploadException("Failed to store file, message(" + e.getMessage() + ")", e);
		}
		LOGGER.info("Success strore file(dirname: {}, name: {})", dirname, filename);
		return true;
	}

	private static boolean IsValdirnameFileContent(final InputStream fileContent) {
		return (fileContent != null) ? true : false;
	}

	private static boolean IsValdirnameFilename(final String filename) {
		final String invldirnameFilenameRegex = "^*[^/*\\\\\"|?:><]*$";
		final String specialFilenameRegex = "^(\\.)*$";
		if (filename == null || filename.trim().isEmpty() || !filename.matches(invldirnameFilenameRegex)
				|| filename.matches(specialFilenameRegex))
			return false;
		return true;
	}

	private static boolean IsValdirnameFilenameLength(final String filename) {
		return (filename.length() <= MAX_FILE_NAME_LEN);
	}

	private Path createDir(final String dirname) {
		LOGGER.debug("Create root({}), dir({})", this.rootPath, dirname);
		Path storeFilePath = this.rootPath.resolve(dirname);
		if (!this.rootPath.toFile().exists()) {
			LOGGER.warn("Root directory({}) isn't exist", this.rootPath);
			try {
				Files.createDirectory(this.rootPath);
			} catch (IOException e) {
				LOGGER.error("Failed to create root directory({})", this.rootPath, e);
				throw new FileUploadException(
						"Failed to create root directory(" + this.rootPath.toString() + "), message(" + e.getMessage() + ")", e);
			}
		} else
			LOGGER.debug("Root directory({}) is exist", this.rootPath);

		if (!storeFilePath.toFile().exists()) {
			try {
				Files.createDirectory(storeFilePath);
			} catch (IOException e) {
				LOGGER.error("Failed to create directory({})", storeFilePath, e);
				throw new FileUploadException(
						"Failed to create directory(" + storeFilePath.toString() + "), message(" + e.getMessage() + ")", e);
			}
			LOGGER.info("Success create directory({})", storeFilePath);
		}
		return storeFilePath;
	}
}
