package com.ict.finalproject.common.jwt;

import com.ict.finalproject.member.service.MemberService;
import com.ict.finalproject.member.vo.MemberVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtUtil {

    private final Key secretKey;
    private long accessToken;
    private long refreshToken;


    // 생성자
    public JwtUtil(String secret, long  accessToken, long refreshToken){
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // accessToken 생성
    public String generateAccessToken(String userId){
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessToken))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // refreshToken 생성
    public String generateRefreshToken(String userId){
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshToken))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 받아서 검증해서 사용자 아이디 추출
    public String validateAndExtractuserId(String token){
        try{
            // 넘어온 token "Bearer Token내용"
            // Bearer 는 HTTP Authorization헤더를 통해서 토근 전달 방식
            // Authorization: Bearer token내용
            // token = token.substring(7) // token 내용만 남는다.

            // 페이로드는 토큰에 담긴 실제 정보인 **클레임(Claim)**을 포함하는 JSON 객체
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 만들때  .setSubject(userId) 때문에 get를 사용해서 userId 를 얻어낼수 있다.
            return claims.getSubject();
        }catch (JwtException | IllegalArgumentException e){
            throw  new IllegalArgumentException("Token Error");
        }
    }


    // 토큰 만료 되었는지 확인하는 메서드
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // 토큰 검증 (아이디 추출)
    public boolean validateToken(String token, UserDetails userDetails){
        try{
            String userId = validateAndExtractuserId(token);
            return userId.equals(userDetails.getUsername()) && !isTokenExpired(token);
            //return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken){
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge((int) (this.refreshToken/1000))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    // 만료 날짜 추출
    public Date extractExpiration(String refreshToken){
        return extractAllClaims(refreshToken).getExpiration();
    }

    // 받은 토큰을 이용해서 모든 정보 반환하기
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 클라이언트에서 보낸 정보 중 쿠키를 추출하기
    public String extractRefreshTokenFromCookie(HttpServletRequest request){
        if(request.getCookies() == null) return null;
        for( Cookie cookie : request.getCookies()){
            if("refreshToken".equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }

    // 쿠키에서 리프레쉬 토큰 삭제
    public void deleteRefreshTokenCookie(HttpServletResponse response){

        ResponseCookie deleteCookie = ResponseCookie
                .from("refreshToken", "")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
    }


}


