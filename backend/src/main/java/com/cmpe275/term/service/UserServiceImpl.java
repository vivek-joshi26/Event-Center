package com.cmpe275.term.service;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.transaction.Transactional;

import com.cmpe275.term.model.MimicTime;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cmpe275.term.entity.AuthenticationProvider;
import com.cmpe275.term.entity.User;
import com.cmpe275.term.entity.VerificationToken;
import com.cmpe275.term.mapper.UserMapper;
import com.cmpe275.term.model.LoginRequest;
import com.cmpe275.term.model.LoginResponse;
import com.cmpe275.term.model.UserModel;
import com.cmpe275.term.repository.UserRepository;
import com.cmpe275.term.repository.VerificationTokenRepository;
import com.cmpe275.term.utility.JWTUtility;
import com.cmpe275.term.utility.RegexPattern;

@Service
public class UserServiceImpl implements UserService,UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
	private UserMapper mapper;
    
    @Autowired
    private JWTUtility jwtUtility;

    @Value("${service.frontendURL}")
    private String frontendURL;
    
    @Autowired
    private RegexPattern pattern;

    @Autowired
    private EmailSenderService emailSenderService;


    @Autowired
    private MimicTime time;


    @Override
    public ResponseEntity registerUser(UserModel userModel) {
        try {
            User user = this.mapper.userModelToEntity(userModel);
            // Add a check to see that password can't be null
            user.setPassword(passwordEncoder.encode(userModel.getPassword()));
            user.setAuthProvider(AuthenticationProvider.LOCAL);
            List<User> existingUsers = userRepository.getByScreenName(userModel.getScreenName());
            List<User> users = userRepository.findByEmail(userModel.getEmail());
            if (users.size() != 0) {
                return ResponseEntity.badRequest().body("This email id already exists! Please proceed to login or enter a new email id"
                		+ ""
                		+ "");
            }
            System.out.println("User SCREENNAME ************ " + existingUsers);
            if (existingUsers.size() != 0) {
                return ResponseEntity.badRequest().body("This screenName already exists! Try to be more creative!.");
            }
            if(!userModel.getScreenName().equalsIgnoreCase(userModel.getName())) {
            	if(!this.pattern.screenNameRegex(userModel.getName(), userModel.getScreenName())) {
            		return ResponseEntity.badRequest().body("Screen name should be equal to full name or atleast start with full name!");
            	}
            }
            User createdUser =  userRepository.save(user);
            emailSenderService.sendEmail(user.getEmail(), "You have signed up successfully with local email.", "Signup received");
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }catch (Exception exception){
            System.out.println("Exception in UserServiceImpl ResponseEntity registerUser(UserModel userModel): " + exception.getMessage());
            return ResponseEntity.badRequest().body("Bad Request, please check the data used for registration");
        }
    }

    @Override
    public ResponseEntity registerGoogleUser(UserModel userModel) {
        try {
            User user = this.mapper.userModelToEntity(userModel);
            // user.setPassword(passwordEncoder.encode(userModel.getPassword()));
            user.setPassword(passwordEncoder.encode("GOOGLEUSER"));
            user.setAuthProvider(AuthenticationProvider.GOOGLE);
            // String screenName = userModel.getScreenName();
            List<User> existingUsers = userRepository.getByScreenName(userModel.getScreenName());
            System.out.println("User SCREENNAME ************ " + existingUsers);
            if (existingUsers.size() != 0) {
                return ResponseEntity.badRequest().body("This screenName already exists! Try to be more creative!.");
            }
            if(!userModel.getScreenName().equalsIgnoreCase(userModel.getName())) {
            	if(!this.pattern.screenNameRegex(userModel.getName(), userModel.getScreenName())) {
            		return ResponseEntity.badRequest().body("Screen name should be equal to full name or atleast start with full name!");
            	}
            }
            
            User createdUser =  userRepository.save(user);
            emailSenderService.sendEmail(user.getEmail(), "You have signed up successfully with google.", "Signup received");
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }catch (Exception exception){
            System.out.println("Exception in registerGoogleUser(UserModel userModel), : " +exception );
            return (ResponseEntity<User>) ResponseEntity.badRequest();
        }
    }



    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null){
            return "invalid";
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if(verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0){
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        emailSenderService.sendEmail(user.getEmail(), "You have verified your account successfully.", "Account verified");
        userRepository.save(user);

        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(Long userId) {
    	User user = userRepository.getById(userId);
        VerificationToken verificationToken = verificationTokenRepository.findByUser(user);
        if(verificationToken == null){
            verificationToken = new VerificationToken(user, UUID.randomUUID().toString());
            verificationTokenRepository.save(verificationToken);
        }else {
            verificationTokenRepository.delete(verificationToken);
            verificationToken = new VerificationToken(user, UUID.randomUUID().toString());
            verificationTokenRepository.save(verificationToken);
        }
        return verificationToken;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<User> users = userRepository.findByEmail(username);
		User user = null;
		if(users.size()>0) {
			user = users.get(0);
		}
		else {
			System.out.println("No user found with given email id");
			
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
	}
	@Override
	public ResponseEntity<LoginResponse> login(LoginRequest request) throws Exception {
		List<User> users = userRepository.findByEmail(request.getEmail());
		User user = null;
		String token = "";
		if(users.size()>0) {
			user = users.get(0);
			//String encodedPassword = passwordEncoder.encode(request.getPassword());
            if(!user.getAuthProvider().equals(AuthenticationProvider.LOCAL)) {
                // throw new Exception("Please login with google");
                return new ResponseEntity("Please login with google" ,HttpStatus.FORBIDDEN);
            }
			if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
				//throw new Exception("Password did not match!");
                return new ResponseEntity("Password did not match!" ,HttpStatus.BAD_REQUEST);
			}
			if(!user.isEnabled()) {
				//throw new Exception("Please verify the account before login!");
                return new ResponseEntity("Please verify the account before login!" ,HttpStatus.UNAUTHORIZED);
			}
			final UserDetails userDetails = this.loadUserByUsername(request.getEmail());
            System.out.println("User: " + userDetails);
			token = jwtUtility.generateToken(userDetails);

            // for every login the Mimic time will be disabled
//            time.setMode(false);
		}
		else {
			//throw new NotFoundException();
            return new ResponseEntity("Please signup before login" ,HttpStatus.NOT_FOUND);
		}

        String strDate = "";
        if(!time.isMode()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date now = new Date();
            strDate = sdf.format(now);
        }else {
            strDate = time.getSysDate();
        }

		return new ResponseEntity<LoginResponse>(new LoginResponse(user,token, strDate), HttpStatus.OK);
		
	}


    @Override
    public ResponseEntity<LoginResponse> loginGoogle(String email) throws Exception {
        //List<User> users = userRepository.findByEmail(email);
        User user = userRepository.getByEmail(email);
        String token = "";
       if(user != null){
            if(!user.getAuthProvider().equals(AuthenticationProvider.GOOGLE)) {
                //throw new Exception("Please login with Local id and password");
                return new ResponseEntity("Please login with Local id and password" ,HttpStatus.FORBIDDEN);
                // redirect to UI login page
            }
            if(!user.isEnabled()) {
                //throw new Exception("Please verify the account before login!");
                return new ResponseEntity("Please verify the account before login!" ,HttpStatus.UNAUTHORIZED);
                // response.sendRedirect(frontendURL);
                // redirect to  UI login page
            }
            final UserDetails userDetails = this.loadUserByUsername(email);
            System.out.println("User: " + userDetails);
            token = jwtUtility.generateToken(userDetails);

           // for every login the Mimic time will be disabled
//            time.setMode(false);
            }
        else {
            //throw new NotFoundException();
           return new ResponseEntity("Please Signup before login!",HttpStatus.NOT_FOUND);
        }
        //return ResponseEntity.ok((new LoginResponse(user, token)));

        String strDate = "";
        if(!time.isMode()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date now = new Date();
            strDate = sdf.format(now);
        }else {
            strDate = time.getSysDate();
        }

        return new ResponseEntity<LoginResponse>(new LoginResponse(user, token, strDate), HttpStatus.OK);
    }

    @Transactional
    @Override
    public String deleteUser(String email) {
        userRepository.deleteUserByEmail(email);
        return "Users with email: " +email + " deleted successfully.";
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

}
