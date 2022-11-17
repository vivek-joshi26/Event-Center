package com.cmpe275.term;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.cmpe275.term.enums.ReviewEnum;
import com.cmpe275.term.model.ForumMessageRequest;
import com.cmpe275.term.model.ReviewRequest;
import com.cmpe275.term.model.UserModel;
import com.cmpe275.term.service.ReviewService;
import com.cmpe275.term.service.ReviewServiceImpl;
import com.cmpe275.term.service.SignupForumService;
import com.cmpe275.term.service.SignupForumServiceImpl;
import com.cmpe275.term.service.UserService;
import com.cmpe275.term.service.UserServiceImpl;





//@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
//@TestConfiguration
//@EnableJpaRepositories(basePackages = "com.cmpe.275.term")
//@DataJpaTest
class DemoApplicationTests {
	
//	@MockBean
//	private EventRepository eventRepository;
	
	
//	@Autowired
//    private MockMvc mockMvc;
	
//	@Autowired
//	private UserModel userModel;
	 
	
	@Test
	public void checkScreenNameValidTest() {
		UserService userService = new UserServiceImpl();
		UserModel userModel = new UserModel();
		userModel.setEmail("sam42@gmail.com");
		userModel.setPassword("cmpe275");
		userModel.setRole("PERSON");
		userModel.setName("sameer");
		userModel.setScreenName("sjsjs");
		userModel.setGender("male");
		ResponseEntity res = userService.registerUser(userModel);
		res.getStatusCodeValue();
		Assertions.assertEquals(400, res.getStatusCodeValue());  
		
		
		}
	
	@Test
	public void checkReviewTextNotNull() {
		ReviewRequest request = new ReviewRequest();
		request.setEventId(7L);
		request.setReviewGivenBy(1L);
		request.setReviewGivenTo(3L);
		request.setReviewStar(4.0);
		request.setReviewFor(ReviewEnum.PARTICIPANT);
		ReviewService service = new ReviewServiceImpl();
		ResponseEntity res = service.postReviewForParticipant(request);
		Assertions.assertEquals(400, res.getStatusCodeValue()); 
		
	}
	
	@Test
	public void checkUniqueEmail() {
		UserService userService = new UserServiceImpl();
		UserModel userModel = new UserModel();
		userModel.setEmail("tanya.gupta@sjsu.edu");
		userModel.setPassword("cmpe275");
		userModel.setRole("PERSON");
		userModel.setName("tanya");
		userModel.setScreenName("tanya");
		userModel.setGender("female");
		ResponseEntity res = userService.registerUser(userModel);
		res.getStatusCodeValue();
		Assertions.assertEquals(400, res.getStatusCodeValue());
	}
	
//	@Test
//	public void postMessageInSignUpForum() throws IOException {
//		boolean thrown =false;
//		ForumMessageRequest request = new ForumMessageRequest();
//		request.setUserId(3L);
//		request.setEventId(2L);
//		request.setSysDate("2023-05-10 14:00");
//		request.setMessage("Hi All");
//		SignupForumService service = new SignupForumServiceImpl();
//		File file = new File("src/main/resources/File/test.png");
//		FileInputStream input;
//		
//			input = new FileInputStream(file);
//			MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);
//			ResponseEntity res = service.sendMessage(multipartFile,request);
//			res.getStatusCodeValue();
//			Assertions.assertEquals(400, res.getStatusCodeValue());
//		 
//		Assertions.assertEquals(true,thrown);
//		//MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);
//		
//		
//		
//	}
	
//	@Test
//	public void postMessageParticipantForum() throws IOException {
//		boolean thrown =false;
//		ForumMessageRequest request = new ForumMessageRequest();
//		request.setUserId(3L);
//		request.setEventId(2L);
//		request.setSysDate("2023-05-10 14:00");
//		request.setMessage("Hi All");
//		ParticipantForumService service = new ParticipantForumServiceImpl();
//		File file = new File("src/main/resources/File/test.png");
//		FileInputStream input;
//		
//			input = new FileInputStream(file);
//			MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);
//			ResponseEntity res = service.sendMessage(multipartFile,request);
//			res.getStatusCodeValue();
//			Assertions.assertEquals(400, res.getStatusCodeValue());
//		 
//		Assertions.assertEquals(true,thrown);
//		//MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);
//		
//		
//		
//	}
	
	@Test
	public void checkReviewTextLength() {
		ReviewRequest request = new ReviewRequest();
		request.setEventId(7L);
		request.setReviewGivenBy(1L);
		request.setReviewGivenTo(3L);
		request.setReviewStar(4.0);
		request.setReviewText("5djdjdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddjddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
		request.setReviewFor(ReviewEnum.PARTICIPANT);
		ReviewService service = new ReviewServiceImpl();
		ResponseEntity res = service.postReviewForParticipant(request);
		Assertions.assertEquals(400, res.getStatusCodeValue()); 
		
	}
	
	@Test
	public void checkReviewDate() {
		ReviewRequest request = new ReviewRequest();
		request.setEventId(7L);
		request.setReviewGivenBy(1L);
		request.setReviewGivenTo(3L);
		request.setReviewStar(4.0);
		request.setSysDate("2020-05-10 14:00");
		request.setReviewFor(ReviewEnum.PARTICIPANT);
		ReviewService service = new ReviewServiceImpl();
		ResponseEntity res = service.postReviewForParticipant(request);
		Assertions.assertEquals(400, res.getStatusCodeValue()); 
		
	}
	
	@Test
	public void checkReviewEndDate() {
		ReviewRequest request = new ReviewRequest();
		request.setEventId(7L);
		request.setReviewGivenBy(1L);
		request.setReviewGivenTo(3L);
		request.setReviewStar(4.0);
		request.setSysDate("2023-05-10 14:00");
		request.setReviewFor(ReviewEnum.PARTICIPANT);
		ReviewService service = new ReviewServiceImpl();
		ResponseEntity res = service.postReviewForParticipant(request);
		Assertions.assertEquals(400, res.getStatusCodeValue()); 
		
	}
	
	@Test
	public void checkOrganizerReviewDate() {
		ReviewRequest request = new ReviewRequest();
		request.setEventId(7L);
		request.setReviewGivenBy(3L);
		request.setReviewGivenTo(1L);
		request.setReviewStar(4.0);
		request.setSysDate("2020-05-10 14:00");
		request.setReviewFor(ReviewEnum.ORGANIZER);
		ReviewService service = new ReviewServiceImpl();
		ResponseEntity res = service.postReviewForParticipant(request);
		Assertions.assertEquals(400, res.getStatusCodeValue()); 
		
	}

	@Test
	public void checkOrgReviewTextLength() {
		ReviewRequest request = new ReviewRequest();
		request.setEventId(7L);
		request.setReviewGivenBy(3L);
		request.setReviewGivenTo(1L);
		request.setReviewStar(4.0);
		request.setReviewText("5djdjdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddjddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
		request.setReviewFor(ReviewEnum.ORGANIZER);
		ReviewService service = new ReviewServiceImpl();
		ResponseEntity res = service.postReviewForParticipant(request);
		Assertions.assertEquals(400, res.getStatusCodeValue()); 
	}
	
	@Test
	public void checkScreenUniqueTest() {
		UserService userService = new UserServiceImpl();
		UserModel userModel = new UserModel();
		userModel.setEmail("sam42@gmail.com");
		userModel.setPassword("cmpe275");
		userModel.setRole("PERSON");
		userModel.setName("Alice");
		userModel.setScreenName("Alice");
		userModel.setGender("male");
		ResponseEntity res = userService.registerUser(userModel);
		res.getStatusCodeValue();
		Assertions.assertEquals(400, res.getStatusCodeValue());  
		
		
		}
	
	@Test
	void contextLoads(ApplicationContext context) {
		assertThat(context).isNotNull();
	}

}

