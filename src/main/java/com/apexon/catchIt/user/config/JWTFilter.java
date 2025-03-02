package com.apexon.catchIt.user.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String authHeader=request.getHeader("Authorization");
        String token=null;
        String userName=null;
        System.out.println(" inside filter");
        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            System.out.println("authH  "+authHeader);
         token = authHeader.substring(7);
            System.out.println("tokennnnn:"+token);
         userName = jwtUtility.extractUserName(token);
            System.out.println("username is : "+userName);
    }
        if(userName!=null )
        {
            System.out.println("going inside filter");
            UserDetails userDetails=customUserDetailsService.loadUserByUsername(userName);
            if(jwtUtility.isTokenValid(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                System.out.println("authToken is : "+authToken);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }

       /* if (request.getServletPath().equals("/registerUser")) {
            filterChain.doFilter(request, response);
            return;
        }*/
    filterChain.doFilter(request,response);

    }
}
