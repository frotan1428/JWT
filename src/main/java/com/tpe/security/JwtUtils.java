package com.tpe.security;

import com.tpe.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger=LoggerFactory.getLogger(JwtUtils.class);


    private String jwtSecret="batch66-67";

    //24*60*60*1000
    private long jwtExpirationMs=86400000;


    public String generateJwtToken(Authentication authentication){
          UserDetailsImpl userDetails=    (UserDetailsImpl)  authentication.getPrincipal();

          return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
                  .setExpiration(new Date(new Date().getTime()+jwtExpirationMs))
                  .signWith(SignatureAlgorithm.HS512,jwtSecret).compact();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch(UnsupportedJwtException e){
            logger.error("JWT Token unsupported {}",e.getMessage());
        }
        catch(MalformedJwtException e){
            logger.error("JWT Token malformed {}",e.getMessage());
        }
        catch(SignatureException e){
            logger.error("JWT Token invalid signature {}",e.getMessage());
        }
        catch(ExpiredJwtException e){
            logger.error("JWT Token expired {}",e.getMessage());
        }
        catch(IllegalArgumentException e){
            logger.error("JWT illegal argument {}",e.getMessage());
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }




}
