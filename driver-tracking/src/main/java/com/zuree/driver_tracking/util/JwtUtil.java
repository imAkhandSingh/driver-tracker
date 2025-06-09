package com.zuree.driver_tracking.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JwtUtil {

    private static String jwtSecret;

    @Value("${jwt.secret}")
    public void setJwtSecret(String jwtSecret) {
        JwtUtil.jwtSecret = jwtSecret;
    }

    public static String generateToken(String username) throws JOSEException {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + AppConstants.EXPIRATION_TIME))
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        signedJWT.sign(new MACSigner(jwtSecret));
        return signedJWT.serialize();
    }

    public static boolean validateToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.verify(new MACVerifier(jwtSecret)) &&
                new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
    }

    public static String getUsername(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }
}

