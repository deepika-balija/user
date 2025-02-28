package com.apexon.catchIt.user.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtility {

    //@Value("${jwt.secret}")
    private static final String SECRET = "kalyanienupalahyderabadapexontelangana12345";


    // @Value("${jwt.expirationMs}")
    private final int expirationMs=86400000;

    //Generate JWT Token
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());  // Add roles
        claims.put("created", new Date());
        System.out.println("gng inside generateToken method");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expirationMs))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();

    }
    //Extract Username from JWT
    public String extractUserName(String token)
    {
        return extractClaim(token,Claims::getSubject);
    }
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token,UserDetails userDetails) {
        String userName=extractUserName(token);
        System.out.println("username is : "+userName);
        return userName.equals(userDetails.getUsername()) && (!extractExpiration(token).before(new Date()));
    }


}
