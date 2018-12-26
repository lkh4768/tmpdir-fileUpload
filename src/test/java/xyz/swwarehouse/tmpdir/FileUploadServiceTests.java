package xyz.swwarehouse.tmpdir;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import xyz.swwarehouse.tmpdir.service.FileUploadService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class FileUploadServiceTests {
	@Autowired
	private FileUploadService fileUploadService;

	@Value("${tmpdir.file.expire-term-day}")
	private int expireTermDay;

	@Test
	public void testCreateExpireTime() {
		Date curDate = new Date();
		long expireTime = fileUploadService.createExpireTime(curDate.getTime());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		calendar.add(Calendar.DATE, expireTermDay);
		assertEquals(calendar.getTime().getTime(), expireTime);
	}

	@Test
	public void testCreateIdForDuplicate() {
		int count = 100000;
		Set<String> ids = new HashSet<>();
		for (int i = 0; i < count; i++) {
			ids.add(fileUploadService.createId());
		}
		assertEquals(ids.size(), count);
	}
}