package com.c1se62.clinic_booking.service.AuthenticationServices;

import com.c1se62.clinic_booking.dto.request.IntrospectRequest;
import com.c1se62.clinic_booking.dto.request.LoginRequest;
import com.c1se62.clinic_booking.dto.response.AuthenticationResponse;
import com.c1se62.clinic_booking.dto.response.IntrospectResponse;
import com.c1se62.clinic_booking.entity.User;
import com.c1se62.clinic_booking.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
@Service
public class AuthenticationServices {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationServices.class);

    @Autowired
    private UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    public AuthenticationResponse authenticated(LoginRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated)
            throw new UsernameNotFoundException(request.getUsername());
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("trungkien.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope",buidScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create Token", e);
            throw new RuntimeException(e);
        }

    }
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiTime.after(new Date()))
                .build();


    }
    private String buidScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRole())){
            user.getRole().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
}
