package net.atos.zepe.auth.beans.jwt;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	private String getKeycloakCertificateUrl(DecodedJWT token) {
		return token.getIssuer() + "/protocol/openid-connect/certs";
	}

	public Map<String, Object> validateJwtToken(String authToken) throws NoSuchAlgorithmException, InvalidKeySpecException, MalformedURLException, JwkException {

		try {
		DecodedJWT jwt = JWT.decode(authToken);
		RSAPublicKey publicKey = loadPublicKey(jwt);
		Algorithm algorithm = Algorithm.RSA256(publicKey, null);
		JWTVerifier verifier = JWT.require(algorithm)
				.withIssuer(jwt.getIssuer())
				.build();

		jwt = verifier.verify(authToken);

			return new HashMap<>(jwt.getClaims());
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
			return null;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
			return null;
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
			return null;
		}
	}

	private RSAPublicKey loadPublicKey(DecodedJWT token) throws JwkException, MalformedURLException {

		final String url = getKeycloakCertificateUrl(token);
		JwkProvider provider = new UrlJwkProvider(new URL(url));

		return (RSAPublicKey) provider.get(token.getKeyId()).getPublicKey();
	}
}
