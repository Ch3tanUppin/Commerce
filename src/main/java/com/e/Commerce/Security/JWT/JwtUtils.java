package com.e.Commerce.Security.JWT;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.e.Commerce.Security.SecurityService.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
/*
 * this is a helper class and has a set of methods that allows to work with jwt
 * Contains utility methods for generating, parsing and validating JWTS.
 * include generation of tokens from a username, validating jwt and extracting username from a token
 */

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.com.e.Commerce.app.jwtCookieName}")
    private String jwtCookie;

    /*
     * this method is getting the jwt from cookies
     * this method is extracting http request
    */
    public String getJwtFromCookies(HttpServletRequest request) {
        // and then fetching the cookie
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        //condition to check if cookie is not empty
        if (cookie != null) {
            System.out.println("COOKIE: " + cookie.getValue());
            //if cookie is not null then it gets the value
            return cookie.getValue();
        } else {
            return null; // else it returns null
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        //below line of code get the username and generates the token 
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        //and returning as cookie and setting the cookie time for 24hrs
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60)
                .httpOnly(false)
                .build(); //building cookie
        return cookie; // returing cookie
    }

    public ResponseCookie getCleanJwtCookie(){
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
        .path("/api")
        .build();
        return cookie;
    }

    //token with issuedAt and expiration, generated from username token
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    //verifying the token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                        .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    //private key
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //validating tthe token got from cookie and checking token is valid or not
    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
