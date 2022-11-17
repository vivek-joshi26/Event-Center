package com.cmpe275.term.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.cmpe275.term.service.UserServiceImpl;
import com.cmpe275.term.utility.oauth.CustomOAuth2UserService;
import com.cmpe275.term.utility.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserServiceImpl userService;


    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value( "${service.frontendURL}" )
    private String frontendURL;

    // these are URLs that are whitelisted, can be visited, similar to allowing CORS
    private static final String[] WHITE_LIST_URLS = {
            "/test",
            "/register",
            "/registerGoogle",
            "/login",
            "/update-time",
            "/report/**",
            "/createEvent",
            "/event/**",
            "/event-participated/**",
            "/eventsByFilter",
            "/v2/api-docs",
            "/event-and-attendees-by-organizer/**",
            "/swagger-ui.html",
            "/verifyRegistration*",
            "/resendVerifyToken*"
            ,"/oauth2/authorization/google",
            "/user/**",
            "/participate/**",
            "/participant-forum/**",
            "/signup-forum/**",
            "/reviewForParticipants",
            "/reviewForOrganizer",
            "/getParticipantRepAndReviews",
            "/getMyReviewsAsParticipant",
            "/getMyReviewsAsOrganizer",
            "/getMyReviewsGivenAsParticipant",
            "/getMyReviewsGivenAsOrganizer"
    };


//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return  new BCryptPasswordEncoder(11);
//    }


    // We are using Spring Security, so we need to bypass some APIs, like registering a user
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable().authorizeHttpRequests().antMatchers(WHITE_LIST_URLS).permitAll();
//        return http.build();
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    	auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// TODO Auto-generated method stub
    	//http.cors().and().csrf().disable().authorizeHttpRequests().antMatchers(WHITE_LIST_URLS).permitAll().anyRequest().authenticated();

        http.cors().and().csrf().disable().authorizeHttpRequests().antMatchers(WHITE_LIST_URLS).permitAll().anyRequest().authenticated().and()
                .oauth2Login()
                    .loginPage(frontendURL)
                    .userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)
                ;

    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
    	return super.authenticationManager();
    }





}