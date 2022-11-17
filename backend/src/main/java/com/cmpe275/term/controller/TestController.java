package com.cmpe275.term.controller;

import com.cmpe275.term.utility.JWTUtility;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
//@RequestMapping("/api")
public class TestController {

	@Autowired
	private JWTUtility jwtUtility;
//
	@GetMapping("/test")
	public ResponseEntity test() throws Exception{
		return new ResponseEntity("Hi" , HttpStatus.OK);

	}

//
//	@GetMapping
//	public Principal user(Principal principal) {
//		System.out.println(principal);
//		//System.out.println("User Name  ****************88 " + principal.getName());
//		return principal;
//	}


	@GetMapping("/login/oauth2/code/google")
	public Principal user2(Principal principal) {
		System.out.println(principal);
		System.out.println("User Name  ****************88 " + principal.getName());
		return principal;
	}

//    @GetMapping("/oauth2/authorization/google")
//    public String usercec(Principal principal) {
//        System.out.println(principal);
//        //System.out.println("User Name  ****************88 " + principal.getName();
//        return "cec";
//    }


//	@GetMapping("/auth/google")
//	public void startGoogleAuth(HttpServletRequest request, HttpServletResponse response) {
//		System.out.println("Google Oauth Login Initiated");
//				String uri = request.getRequestURI();
//		System.out.println(uri);
//	}



//	@PostMapping("/{id}/upload-image")
//	public ResponseEntity<RoomEntry> uploadImage(@PathVariable("id") Long id, @RequestBody MultipartFile file){
//		RoomEntry roomEntry = roomService.uploadImage(file,id);
//		if(roomEntry != null){
//			return new ResponseEntity<>( roomEntry, HttpStatus.OK);
//		}
//		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	}


//	public RoomEntry uploadImage(MultipartFile file, Long id){
//		try{
//			String folderName = String.format("room-%s",id);
//			s3Utils.uploadFile("alpha-hotel-images",folderName, file.getInputStream());
//			RoomEntity roomEntity = roomRepository.getById(id);
//			return convertToEntry(roomEntity);
//		}
//		catch (Exception exception){
//			exception.printStackTrace();
//			System.out.println("Exception in RoomEntry uploadImage(MultipartFile file, Long id), msg :  " );
//			return null;
//		}
//	}
//	String folderName = String.format("room-%s",roomEntry.getId());
//	String url = s3Utils.getFileURL("alpha-hotel-images", folderName);
//	roomEntry.setImageUrl(url);




}
