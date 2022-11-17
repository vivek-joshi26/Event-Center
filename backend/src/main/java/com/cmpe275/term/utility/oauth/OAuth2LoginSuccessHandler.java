package com.cmpe275.term.utility.oauth;

import com.cmpe275.term.entity.AuthenticationProvider;
import com.cmpe275.term.entity.User;
import com.cmpe275.term.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    @Autowired
    UserService userService;

    @Value( "${service.frontendURL}" )
    private String frontendURL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("Inside on AuthenticationSuccess ************************** ");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        System.out.println("EMAIL************************** " + email);

        User user = userService.getUserByEmail(email);
        if(user == null){
            //  need to redirect to UI-  "/signupGoogle/vivek.jsh2@gmail.com"
            // from UI send post request to /registerGoogle
            //response.sendRedirect("/user/" + email);
            //response.sendRedirect("http://localhost:3000/register/" + email);
            response.sendRedirect(frontendURL + "register/" + email);
        }else {
            // user exist check if Provider is GOOGLE and if so redirect to dashboard
            //if(user.getAuthProvider() == AuthenticationProvider.GOOGLE){
                //   need to redirect to  UI- "/user/dashboard/vivek.jsh2@gmail.com"
                // in UI on load call /user/vivek.jsh2@gmail.com
                //response.sendRedirect("/user/" + email);
                //response.sendRedirect("http://localhost:3000/redirect/" + email);
            response.sendRedirect(frontendURL + "redirect/" + email);
            //}
            //else throw new IOException("Local User, Please signup through ID and password");
            // I think there should be a page to which we can redirect in this scenario
            //response.sendRedirect("/user/" + email);

        }

        // super.onAuthenticationSuccess(request,response,authentication);
    }

}
