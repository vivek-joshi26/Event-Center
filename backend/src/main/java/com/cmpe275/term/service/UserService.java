package com.cmpe275.term.service;

import com.cmpe275.term.entity.User;
import com.cmpe275.term.entity.VerificationToken;
import com.cmpe275.term.model.LoginRequest;
import com.cmpe275.term.model.LoginResponse;
import com.cmpe275.term.model.UserModel;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<User> registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(Long oldToken);
    
    public ResponseEntity<LoginResponse> login(LoginRequest request) throws Exception;

    User getUserByEmail(String email);

    ResponseEntity<User> registerGoogleUser(UserModel userModel);

    public ResponseEntity<LoginResponse> loginGoogle(String email) throws Exception;

    String deleteUser(String email);
}
