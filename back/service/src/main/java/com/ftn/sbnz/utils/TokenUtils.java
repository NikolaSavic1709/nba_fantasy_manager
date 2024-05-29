package com.ftn.sbnz.utils;

import com.ftn.sbnz.model.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Utility class for working with JSON Web Tokens
@Component
public class TokenUtils {

    // Token issuer
    @Value("spring-security-example")
    private String APP_NAME;

    // Secret known only to the backend application for generating and verifying JWT https://jwt.io/
    @Value("somesecret")
    public String SECRET;

    // Token expiration period - 30 minutes
    @Value("1800000000")
    private int EXPIRES_IN;

    // Name of the header through which JWT will be transmitted in server-client communication
    @Value("Authorization")
    private String AUTH_HEADER;

    // It is possible to generate JWTs for different clients (e.g., web and mobile clients may have different JWT durations,
    // JWTs for mobile clients may last longer because the application is less frequently used in that way)
    // For the simplicity of the example, we will not take into account the device from which the request comes.
    //    private static final String AUDIENCE_UNKNOWN = "unknown";
    //    private static final String AUDIENCE_MOBILE = "mobile";
    //    private static final String AUDIENCE_TABLET = "tablet";

    private static final String AUDIENCE_WEB = "web";

    // Algorithm for signing JWT
    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    // ============= Functions for generating JWT tokens =============
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getEmail());
        claims.put("id", user.getId());
        claims.put("role", user.getAuthority().getName());
        claims.put("auth_type", "standard");
        return this.generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(this.generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + EXPIRES_IN);
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);

        // JWT is passed through the 'Authorization' header in the format:
        // Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // take only the token (the token value is after the "Bearer " prefix)
        }

        return null;
    }

    /**
     * Function to retrieve the token owner (username).
     *
     * @param token JWT token.
     * @return Username from the token or null if it does not exist.
     */
    public String getEmailFromToken(String token) {
        String email;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            email = claims.getSubject();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            email = null;
        }

        return email;
    }

    public int getIdFromToken(String token) {
        Claims claims;
        claims = getAllClaimsFromToken(token);
        return (int) claims.get("id");
    }

    public String getRole(String token) {
        Claims claims;
        claims = getAllClaimsFromToken(token);
        String role = (String) claims.get("role");
        return role;
    }

    public String getRoleFromToken(String token) {
        String role;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            role = (String) claims.get("role");
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            role = null;
        }

        return role;
    }

    /**
     * Function to retrieve the token creation date.
     *
     * @param token JWT token.
     * @return Date when the token was created.
     */
    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    /**
     * Function to retrieve device information from the token.
     *
     * @param token JWT token.
     * @return Device type.
     */
    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            audience = claims.getAudience();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    /**
     * Function to retrieve the expiration date of the token.
     *
     * @param token JWT token.
     * @return Date until which the token is valid.
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            expiration = null;
        }

        return expiration;
    }

    /**
     * Function to read all data from the JWT token.
     *
     * @param token JWT token.
     * @return Data from the token.
     */
    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            claims = null;
        }

        // Retrieving arbitrary data is possible by calling the claims.get(key) function

        return claims;
    }

    // =================================================================

    // ============= Functions for validating JWT tokens =============

    /**
     * Function to validate JWT token.
     *
     * @param token        JWT token.
     * @param userDetails Information about the user who owns the JWT token.
     * @return Information whether the token is valid or not.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String username = getEmailFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);

        // The token is valid when:
        return (username != null // username is not null
                && username.equals(userDetails.getUsername())); // username from the token matches the username in the database
        //       && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())); // after token creation, the user has not changed their password
    }

    /**
     * Function to check if the user's password has been changed after issuing the token.
     *
     * @param created            Token creation date.
     * @param lastPasswordReset  Date of the last password change.
     * @return Information whether the token was created before the last password change or not.
     */
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    // =================================================================

    /**
     * Function to retrieve the token expiration period.
     *
     * @return Token expiration period.
     */
    public int getExpiredIn() {
        return EXPIRES_IN;
    }

    /**
     * Function to retrieve the content of the AUTH_HEADER from the request.
     *
     * @param request HTTP request.
     * @return Content from AUTH_HEADER.
     */
    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

}
