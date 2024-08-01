package com.kaitech.student_crm.security;//package com.kaitech.student_crm.security;
//
//import com.kaitech.student_crm.models.User;
//import com.kaitech.student_crm.services.CustomUserDetailService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//
//@Component
//public class
//JWTAuthenticationFilter extends OncePerRequestFilter {
//    public static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
//
//    @Autowired
//    private JWTTokenProvider jwtTokenProvider;
//
//    @Autowired
//    private CustomUserDetailService customUserDetailService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            String jwt = getJWTFromRequest(request);
//            if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
//                String email = jwtTokenProvider.getUserIdFromToken(jwt);
//                User userDetails = (User) customUserDetailService.loadUserByUsername(email);
//
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        userDetails, userDetails.getPassword(), userDetails.getAuthorities()
//                );
//
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            }
//
//        }catch (Exception exception) {
//            LOGGER.error("Could not set user authentication");
//        }
//
//        filterChain.doFilter(request, response);
//
//    }
//
//    private String getJWTFromRequest(HttpServletRequest request) {
//        String bearToken = request.getHeader(SecurityConstants.HEADER_STRING);
//        if (StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
//            return bearToken.split(" ")[1];
//        }
//        return null;
//    }
//}
