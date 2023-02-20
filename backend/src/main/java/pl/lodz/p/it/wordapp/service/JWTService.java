package pl.lodz.p.it.wordapp.service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationTime}")
    private long expirationTime;

    private final ConcurrentHashMap<String, Date> invalidatedTokens = new ConcurrentHashMap<>();

    public Optional<String> generateToken(String userName) {
        try {
            return Optional.of(JWT.create()
                                  .withSubject(userName)
                                  .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                                  .sign(Algorithm.HMAC256(secret)));
        } catch (JWTCreationException ex) {
            return Optional.empty();
        }
    }

    public Optional<String> getSubjectFromToken(String token) {
        if (invalidatedTokens.containsKey(token)) {
            return Optional.empty();
        }

        try {
            return Optional.of(JWT.require(Algorithm.HMAC256(secret))
                                  .build()
                                  .verify(token)
                                  .getSubject());
        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }

    public Optional<Date> getExpriresAtFromToken(String token) {
        if (invalidatedTokens.containsKey(token)) {
            return Optional.empty();
        }

        try {
            return Optional.of(JWT.require(Algorithm.HMAC256(secret))
                                  .build()
                                  .verify(token)
                                  .getExpiresAt());
        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }

    public void invalidateToken(String token) {
        final String tokenWithoutScheme;

        if (token.startsWith("Bearer ")) {
            tokenWithoutScheme = token.replace("Bearer ", "");
        } else {
            tokenWithoutScheme = token;
        }

        Optional<Date> expiresAt = getExpriresAtFromToken(tokenWithoutScheme);

        expiresAt.filter(date -> date.after(new Date()))
                 .map(date -> invalidatedTokens.put(tokenWithoutScheme, date));
    }

    @Async
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void deleteExpiredTokens() {
        final Date now = new Date();
        invalidatedTokens.forEach((token, exp) -> {
            if (exp.before(now)) {
                invalidatedTokens.remove(token);
            }
        });
    }
}
