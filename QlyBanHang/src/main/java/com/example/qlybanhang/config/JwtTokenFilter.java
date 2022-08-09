package com.example.qlybanhang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
public class JwtTokenFilter{
//public class JwtTokenFilter extends OncePerRequestFilter {
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String token = resolveToken(httpServletRequest);
//
//        try {
//            if (token != null && jwtTokenProvider.validateToken(token)) {
//                Authentication auth = jwtTokenProvider.getAuthentication(token);
//
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        } catch (Exception ex) {
//            // this is very important, since it guarantees the user is not authenticated at
//            // all
//            SecurityContextHolder.clearContext();
//            httpServletResponse.sendError(401, ex.getMessage());
//            return;
//        }
//
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
//
//    public String resolveToken(HttpServletRequest req) {
//        String bearerToken = req.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
}
