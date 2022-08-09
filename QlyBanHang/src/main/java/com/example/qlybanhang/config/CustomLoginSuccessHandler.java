package com.example.qlybanhang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            String targetUrl = detemineTarget(authentication);
            if(response.isCommitted()){
                return;
            }
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            redirectStrategy.sendRedirect(request , response , targetUrl);
    }

    protected String detemineTarget(Authentication authentication){
        String url  = "";
        //Fetch the roles from the Authentication object
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<String>();
        for (GrantedAuthority item : authorities){
            roles.add(item.getAuthority());
        }
        //Check user role and decide the redirect URL
        if(roles.contains("ROLE_ADMIN")){
            url="/admin";
        }else if(roles.contains("ROLE_USER")){
            url="/";
        }
        return url;
    }
}
