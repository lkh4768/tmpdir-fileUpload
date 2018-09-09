package xyz.swwarehouse.tmpdir;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.swwarehouse.tmpdir.entity.FileInfo;
import xyz.swwarehouse.tmpdir.repository.FileInfoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class FileUploadControllerTests {
	private static boolean INITIALIZED = false;
	private static final String FILE_KEY_IN_MULTIPART_MAP = "file";

	@Value("${server.port}")
	private int port;
	@Value("${server.host}")
	private String serverHost;
	@Autowired
	private WebApplicationContext webApplicationContext;
	private static MockMvc mockMvc = null;

	@Autowired
	private FileInfoRepository fileInfoRepository;

	@Before
	public void setUp() {
		if (!INITIALIZED) {
			mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
			INITIALIZED = true;
		}
	}

	@Test
	public void testUploadFile() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		MockMultipartFile nomalFile = new MockMultipartFile(FILE_KEY_IN_MULTIPART_MAP, Filenames.ENGLISH.incluedExtension(),
				MediaType.TEXT_PLAIN_VALUE, FileContents.NOMAL_CONTENT.toString().getBytes());

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload("http://" + serverHost + ":" + port + "/")
				.file(nomalFile);

		String jsonResponse = mockMvc.perform(builder).andDo(print()).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		System.out.println("jsonRes: " + jsonResponse);
		FileInfo fileInfo = objectMapper.readValue(jsonResponse, FileInfo.class);

		assertNotNull(fileInfoRepository.findOne(fileInfo.getId()));
	}

	@Test
	public void testUploadFileKoean() throws Exception {
		MockMultipartFile koreanFilenameOfFile = new MockMultipartFile(FILE_KEY_IN_MULTIPART_MAP,
				Filenames.KOREAN.incluedExtension(), MediaType.TEXT_PLAIN_VALUE,
				FileContents.NOMAL_CONTENT.toString().getBytes());

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload("http://" + serverHost + ":" + port + "/")
				.file(koreanFilenameOfFile);

		mockMvc.perform(builder).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testUploadFileInvailedFilename() throws Exception {
		MockMultipartFile invaildFilenameOfFile = new MockMultipartFile(FILE_KEY_IN_MULTIPART_MAP,
				Filenames.TWO_DOT.toString(), MediaType.TEXT_PLAIN_VALUE, FileContents.NOMAL_CONTENT.toString().getBytes());

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload("http://" + serverHost + ":" + port + "/")
				.file(invaildFilenameOfFile);

		mockMvc.perform(builder).andDo(print()).andExpect(status().is(HttpStatusCode.INVAILD_FILENAME.vaule()));
	}

	@Test
	public void testUploadFileTooLongFilename() throws Exception {
		MockMultipartFile invaildFilenameOfFile = new MockMultipartFile(FILE_KEY_IN_MULTIPART_MAP,
				Filenames.TOO_LONG.toString(), MediaType.TEXT_PLAIN_VALUE, FileContents.NOMAL_CONTENT.toString().getBytes());

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload("http://" + serverHost + ":" + port + "/")
				.file(invaildFilenameOfFile);

		mockMvc.perform(builder).andDo(print()).andExpect(status().is(HttpStatusCode.TOO_LONG_FILENAME.vaule()));
	}
}