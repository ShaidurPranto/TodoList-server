package com.example.todoList.authGoogleOAuth;

import com.example.todoList.authJWT.JWTService;
import com.example.todoList.util.AppEnv;
import com.example.todoList.model.User;
import com.example.todoList.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        System.out.println("OAuth2 login success ");
        System.out.println("email: " + email);
        System.out.println("name: " + name);

        // Save user if new
        if (userRepository.findByEmail(email) == null) {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            userRepository.save(user);
        }

        // Generate JWT
        String jwt = jwtService.generateToken(email);

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Set to true in production
        cookie.setPath("/");
        cookie.setMaxAge(60 * 30); // 30 minutes
        cookie.setAttribute("SameSite", "None"); // set true in production

        response.addCookie(cookie);
        System.out.println("redirecting to "+ AppEnv.getFrontendUrl()+"/homeUser");
        response.sendRedirect(AppEnv.getFrontendUrl()+"/homeUser");
    }
}
