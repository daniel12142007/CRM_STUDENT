package com.kaitech.student_crm.security;//package com.kaitech.student_crm.security;
//
//import com.kaitech.student_crm.models.User;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.SignatureException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class JWTTokenProvider {
//    public static final Logger LOGGER = LoggerFactory.getLogger(JWTTokenProvider.class);
//
//    public String generateToken(Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
//        Date now = new Date(System.currentTimeMillis());
//        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);
//
//        Map<String, Object> claimsMap = new HashMap<>();
//        claimsMap.put("username", user.getEmail());
//        claimsMap.put("firstname", user.getFirstname());
//        claimsMap.put("lastname", user.getLastname());
//
//        return Jwts.builder()
//                .addClaims(claimsMap)
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
//                .compact();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                    .setSigningKey(SecurityConstants.SECRET)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            return true;
//        }catch (SignatureException |
//                MalformedJwtException |
//                ExpiredJwtException |
//                IllegalArgumentException ex) {
//            LOGGER.error(ex.getMessage());
//            return false;
//        }
//    }
//
//
//    public String getUserIdFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(SecurityConstants.SECRET)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        String email = (String) claims.get("username");
//
//        return email;
//    }
//}
