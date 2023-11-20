package com.example.Phone.Pay.management.config;

import com.example.Phone.Pay.management.dto.GenericResponse;
import com.example.Phone.Pay.management.entity.User;
import com.example.Phone.Pay.management.repo.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;


public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private JwtTokenUtils jwtTokenUtil;

    private UserRepo userCRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

    JwtAuthenticationFilter(JwtTokenUtils jwtTokenUtil, UserRepo userCRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userCRepo = userCRepo;
    }

    private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filter)
            throws IOException {
        try {
            String authToken = req.getHeader("Authorization");
            String username = jwtTokenUtil.parseToken(authToken);
            User employee = userCRepo.findByMobile(username);
            if (employee != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(employee, null, Arrays.asList(
                        new SimpleGrantedAuthority(employee.getRoleType().name())));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filter.doFilter(req, res);
            }
            else {
                generateUnauthorisedAccess(res);
            }

        } catch (Exception e) {
            System.out.println(e);
            generateUnauthorisedAccess(res);
        }
    }

    public void generateUnauthorisedAccess(HttpServletResponse res) throws IOException {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        GenericResponse resp = new GenericResponse(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORISED");
        String jsonRespString = ow.writeValueAsString(resp);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = res.getWriter();
        writer.write(jsonRespString);
        System.out.println("===============================");

    }

}
