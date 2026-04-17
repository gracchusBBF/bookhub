package com.eni.bookhub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour JwtService.
 *
 * Clé de test HS256 encodée en Base64 (256 bits minimum requis).
 * Ne jamais utiliser cette clé en production.
 */
class JwtServiceTest {

    private JwtService jwtService;

    // Clé Base64 valide de 256 bits (32 octets) pour les tests
    private static final String VALID_SECRET =
            "dGVzdFNlY3JldEtleUZvckp3dFRlc3RpbmdQdXJwb3NlczEyMw==";

    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_ROLE  = "ROLE_USER";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "SECRET", VALID_SECRET);
    }

    // =========================================================
    // generateToken()
    // =========================================================
    @Nested
    @DisplayName("generateToken()")
    class GenerateTokenTests {

        @Test
        @DisplayName("Le token généré ne doit pas être null ni vide")
        void shouldReturnNonNullNonEmptyToken() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            assertThat(token).isNotNull().isNotBlank();
        }

        @Test
        @DisplayName("Le token doit contenir exactement 3 parties séparées par des points")
        void shouldHaveThreeParts() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            String[] parts = token.split("\\.");
            assertThat(parts).hasSize(3);
        }

        @Test
        @DisplayName("Le sujet extrait doit correspondre à l'email fourni")
        void shouldContainCorrectSubject() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            assertThat(jwtService.extractUsername(token)).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("Le rôle extrait doit correspondre au rôle fourni")
        void shouldContainCorrectRole() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            assertThat(jwtService.extractRole(token)).isEqualTo(TEST_ROLE);
        }
    }

    // =========================================================
    // createToken()
    // =========================================================
    @Nested
    @DisplayName("createToken()")
    class CreateTokenTests {

        @Test
        @DisplayName("Le token créé doit être parseable sans exception")
        void shouldBeParseableWithoutException() {
            Map<String, Object> claims = new HashMap<>();
            claims.put("customClaim", "value");

            String token = jwtService.createToken(claims, TEST_EMAIL);

            assertThatNoException().isThrownBy(() -> jwtService.extractAllClaims(token));
        }

        @Test
        @DisplayName("La date d'expiration doit être ~1h après l'émission")
        void expirationShouldBeOneHourAfterIssuedAt() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            Claims claims    = jwtService.extractAllClaims(token);
            long  issuedAt   = claims.getIssuedAt().getTime();
            long  expiration = claims.getExpiration().getTime();
            long  diff       = expiration - issuedAt;

            // Tolérance de ±2 secondes
            assertThat(diff)
                    .isGreaterThanOrEqualTo(3_598_000L)
                    .isLessThanOrEqualTo(3_602_000L);
        }

        @Test
        @DisplayName("Les claims custom passés doivent être présents dans le payload")
        void shouldContainCustomClaims() {
            Map<String, Object> claims = new HashMap<>();
            claims.put("department", "IT");
            claims.put("level", 3);

            String token = jwtService.createToken(claims, TEST_EMAIL);

            Claims extracted = jwtService.extractAllClaims(token);
            assertThat(extracted.get("department", String.class)).isEqualTo("IT");
            assertThat(extracted.get("level", Integer.class)).isEqualTo(3);
        }
    }

    // =========================================================
    // extractUsername()
    // =========================================================
    @Nested
    @DisplayName("extractUsername()")
    class ExtractUsernameTests {

        @Test
        @DisplayName("Doit retourner l'email correct depuis un token valide")
        void shouldReturnCorrectEmail() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            assertThat(jwtService.extractUsername(token)).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("Doit lever une exception si le token est malformé")
        void shouldThrowOnMalformedToken() {
            assertThatExceptionOfType(MalformedJwtException.class)
                    .isThrownBy(() -> jwtService.extractUsername("not.a.valid.jwt"));
        }

        @Test
        @DisplayName("Doit lever une exception si le token est vide")
        void shouldThrowOnEmptyToken() {
            assertThatException()
                    .isThrownBy(() -> jwtService.extractUsername(""));
        }
    }

    // =========================================================
    // extractRole()
    // =========================================================
    @Nested
    @DisplayName("extractRole()")
    class ExtractRoleTests {

        @Test
        @DisplayName("Doit retourner le rôle correct depuis un token valide")
        void shouldReturnCorrectRole() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            assertThat(jwtService.extractRole(token)).isEqualTo(TEST_ROLE);
        }

        @Test
        @DisplayName("Doit retourner null si le claim 'role' est absent")
        void shouldReturnNullWhenRoleClaimAbsent() {
            // Token créé sans claim "role"
            String token = jwtService.createToken(new HashMap<>(), TEST_EMAIL);

            assertThat(jwtService.extractRole(token)).isNull();
        }

        @Test
        @DisplayName("Doit lever une exception si le token est invalide")
        void shouldThrowOnInvalidToken() {
            assertThatExceptionOfType(MalformedJwtException.class)
                    .isThrownBy(() -> jwtService.extractRole("invalid.token.here"));
        }
    }

    // =========================================================
    // extractExpiration()
    // =========================================================
    @Nested
    @DisplayName("extractExpiration()")
    class ExtractExpirationTests {

        @Test
        @DisplayName("La date d'expiration doit être dans le futur pour un token frais")
        void expirationShouldBeInFuture() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            Date expiration = jwtService.extractExpiration(token);

            assertThat(expiration).isAfter(new Date());
        }

        @Test
        @DisplayName("La date d'expiration doit être dans environ 1h")
        void expirationShouldBeApproximatelyOneHourFromNow() {
            long before = System.currentTimeMillis();
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);
            long after  = System.currentTimeMillis();

            long expMs = jwtService.extractExpiration(token).getTime();

            assertThat(expMs)
                    .isGreaterThanOrEqualTo(before + 3_598_000L)
                    .isLessThanOrEqualTo(after  + 3_602_000L);
        }

        @Test
        @DisplayName("Doit lever une exception si le token est malformé")
        void shouldThrowOnMalformedToken() {
            assertThatExceptionOfType(MalformedJwtException.class)
                    .isThrownBy(() -> jwtService.extractExpiration("bad.token.value"));
        }
    }

    // =========================================================
    // extractClaim()
    // =========================================================
    @Nested
    @DisplayName("extractClaim()")
    class ExtractClaimTests {

        @Test
        @DisplayName("Doit extraire correctement un claim String via Function")
        void shouldExtractStringClaimWithFunction() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            String role = jwtService.extractClaim(token, c -> c.get("role", String.class));

            assertThat(role).isEqualTo(TEST_ROLE);
        }

        @Test
        @DisplayName("Doit extraire correctement un claim Date via Function")
        void shouldExtractDateClaimWithFunction() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

            assertThat(expiration).isNotNull().isAfter(new Date());
        }

        @Test
        @DisplayName("Doit extraire correctement le sujet via Function")
        void shouldExtractSubjectWithFunction() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            String subject = jwtService.extractClaim(token, Claims::getSubject);

            assertThat(subject).isEqualTo(TEST_EMAIL);
        }
    }

    // =========================================================
    // extractAllClaims()
    // =========================================================
    @Nested
    @DisplayName("extractAllClaims()")
    class ExtractAllClaimsTests {

        @Test
        @DisplayName("Doit retourner les claims complets d'un token valide")
        void shouldReturnAllClaimsForValidToken() {
            String token = jwtService.generateToken(TEST_EMAIL, TEST_ROLE);

            Claims claims = jwtService.extractAllClaims(token);

            assertThat(claims).isNotNull();
            assertThat(claims.getSubject()).isEqualTo(TEST_EMAIL);
            assertThat(claims.get("role", String.class)).isEqualTo(TEST_ROLE);
            assertThat(claims.getIssuedAt()).isNotNull();
            assertThat(claims.getExpiration()).isNotNull();
        }

        @Test
        @DisplayName("Doit lever ExpiredJwtException pour un token expiré")
        void shouldThrowExpiredJwtExceptionForExpiredToken() {
            // Token expiré avec expiration dans le passé
            Map<String, Object> claims = new HashMap<>();
            String expiredToken = io.jsonwebtoken.Jwts.builder()
                    .claims(claims)
                    .subject(TEST_EMAIL)
                    .issuedAt(new Date(System.currentTimeMillis() - 7_200_000L)) // -2h
                    .expiration(new Date(System.currentTimeMillis() - 3_600_000L)) // -1h
                    .signWith(
                            io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                                    io.jsonwebtoken.io.Decoders.BASE64.decode(VALID_SECRET)),
                            io.jsonwebtoken.SignatureAlgorithm.HS256)
                    .compact();

            assertThatExceptionOfType(ExpiredJwtException.class)
                    .isThrownBy(() -> jwtService.extractAllClaims(expiredToken));
        }

        @Test
        @DisplayName("Doit lever MalformedJwtException pour un token malformé")
        void shouldThrowMalformedJwtExceptionForMalformedToken() {
            assertThatExceptionOfType(MalformedJwtException.class)
                    .isThrownBy(() -> jwtService.extractAllClaims("this.is.notvalid"));
        }

        @Test
        @DisplayName("Doit lever SignatureException pour une mauvaise signature")
        void shouldThrowSignatureExceptionForWrongKey() {
            // Autre clé valide HS256 mais différente
            String otherSecret = "YW5vdGhlclNlY3JldEtleUZvckp3dFRlc3RpbmdYWVo=";
            JwtService otherService = new JwtService();
            ReflectionTestUtils.setField(otherService, "SECRET", otherSecret);

            String tokenFromOtherKey = otherService.generateToken(TEST_EMAIL, TEST_ROLE);

            assertThatExceptionOfType(SignatureException.class)
                    .isThrownBy(() -> jwtService.extractAllClaims(tokenFromOtherKey));
        }

        @Test
        @DisplayName("Doit lever une exception pour un token aléatoire/tronqué")
        void shouldThrowForRandomOrTruncatedToken() {
            assertThatException()
                    .isThrownBy(() -> jwtService.extractAllClaims("abc123"));
        }
    }

    // =========================================================
    // getSignKey() — tests indirects via le comportement observable
    // =========================================================
    @Nested
    @DisplayName("getSignKey() — tests indirects")
    class GetSignKeyTests {

        @Test
        @DisplayName("Doit lever une exception si le secret n'est pas un Base64 valide")
        void shouldThrowIfSecretIsNotValidBase64() {
            JwtService badService = new JwtService();
            ReflectionTestUtils.setField(badService, "SECRET", "not-valid-base64!!!");

            assertThatException()
                    .isThrownBy(() -> badService.generateToken(TEST_EMAIL, TEST_ROLE));
        }

        @Test
        @DisplayName("Doit lever une exception si la clé est trop courte pour HS256 (< 256 bits)")
        void shouldThrowIfKeyIsTooShortForHS256() {
            // "short" encodé en Base64 → seulement 5 octets, insuffisant pour HS256
            JwtService weakService = new JwtService();
            ReflectionTestUtils.setField(weakService, "SECRET", "c2hvcnQ=");

            assertThatException()
                    .isThrownBy(() -> weakService.generateToken(TEST_EMAIL, TEST_ROLE));
        }
    }
}