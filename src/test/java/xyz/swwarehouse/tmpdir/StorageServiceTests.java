package xyz.swwarehouse.tmpdir;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import xyz.swwarehouse.tmpdir.service.FileUploadException;
import xyz.swwarehouse.tmpdir.service.FileUploadService;
import xyz.swwarehouse.tmpdir.service.InvalidFilenameException;
import xyz.swwarehouse.tmpdir.service.StorageService;
import xyz.swwarehouse.tmpdir.service.TooLongFileNameException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class StorageServiceTests {
	@Autowired
	private StorageService storageService;
	@Autowired
	private FileUploadService fileUploadService;

	@Test
	public void testStoreFileInStorageForEnglishFileName() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.ENGLISH.incluedExtension(), contentInputStream);
	}

	@Test
	public void testStoreFileInStorageForKoreanFileName() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.KOREAN.incluedExtension(), contentInputStream);
	}

	@Test
	public void testStoreFileInStorageForEnglishAndKoreanFileName() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.ENGLISH + Filenames.KOREAN.incluedExtension(), contentInputStream);
	}

	@Test(expected = TooLongFileNameException.class)
	public void testStoreFileInStorageForOverFilenameMax() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.TOO_LONG.toString(), contentInputStream);
	}

	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForTwoDotFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.TWO_DOT.toString(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForOneDotFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.ONE_DOT.toString(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForSlashFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.SLASH.toString(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForBackSlashFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.BACK_SLASH.toString(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForQuestionMarkFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.QUESTION_MARK.toString(), contentInputStream);
	}

	@Test
	public void testStoreFileInStorageForPercentSignFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.PERCENT_SIGN.toString(), contentInputStream);
	}

	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForAsteriskFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.ASTERISK.toString(), contentInputStream);
	}

	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForColonFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.COLON.toString(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForVerticalBarFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.VERTICAL_BAR.toString(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForDoubleQuotationFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.DOUBLE_QUOTATION.toString(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForLeftAngleBracketFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.LEFT_ANGLE_BRACKET.toString(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForRightAngleBracketFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.RIGHT_ANGLE_BRACKET.toString(), contentInputStream);
	}

	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForEmptyFileName() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.EMPTY.toString(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForSpaceFileName() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.SPACE.toString(), contentInputStream);
		
	}
	
	public void testStoreFileInStorageForTwoDotAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.TWO_DOT.incluedExtension(), contentInputStream);
	}
	
	public void testStoreFileInStorageForOneDotAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.ONE_DOT.incluedExtension(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForSlashAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.SLASH.incluedExtension(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForBackSlashAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.BACK_SLASH.incluedExtension(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForQuestionMarkAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.QUESTION_MARK.incluedExtension(), contentInputStream);
	}

	@Test
	public void testStoreFileInStorageForPercentSignAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.PERCENT_SIGN.incluedExtension(), contentInputStream);
	}

	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForAsteriskAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.ASTERISK.incluedExtension(), contentInputStream);
	}

	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForColonAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.COLON.incluedExtension(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForVerticalBarAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.VERTICAL_BAR.incluedExtension(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForDoubleQuotationAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.DOUBLE_QUOTATION.incluedExtension(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForLeftAngleBracketAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.LEFT_ANGLE_BRACKET.incluedExtension(), contentInputStream);
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void testStoreFileInStorageForRightAngleBracketAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.RIGHT_ANGLE_BRACKET.incluedExtension(), contentInputStream);
	}

	@Test
	public void testStoreFileInStorageForEmptyAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		System.out.println(Filenames.EMPTY.incluedExtension());
		storageService.storeFile(id, Filenames.EMPTY.incluedExtension(), contentInputStream);
	}
	
	@Test
	public void testStoreFileInStorageForSpaceAndExtensionFilename() {
		String id = fileUploadService.createId();
		InputStream contentInputStream = new ByteArrayInputStream(FileContents.NOMAL_CONTENT.toString().getBytes());
		storageService.storeFile(id, Filenames.SPACE.incluedExtension(), contentInputStream);
	}
	
	@Test(expected = FileUploadException.class)
	public void testStoreFileInStorageForNullFileContent() {
		String id = fileUploadService.createId();
		storageService.storeFile(id, Filenames.ENGLISH.incluedExtension(), null);
	}
}