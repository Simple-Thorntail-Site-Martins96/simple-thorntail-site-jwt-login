package com.lucamartinelli.app.login.jwt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import javax.ejb.Stateless;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * <h1>Signing class</h1>
 * <p>This class generate, validate and decode a signed JWT with secret key.</p>
 * 
 * @author Luca Martinelli
 * @version 1.0
 * @category security
 *
 */
@Stateless
public class SigningImplEJB extends TokenUtil implements SignerEJBInterface {
	
	/**
	 * Utility method to generate a signed JWT from a JSON resource file that is
	 * signed by secret key
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 */
	@Override
	public SignedJWT generateToken(final JWTClaimsSet claims) throws JOSEException, UnsupportedEncodingException,
			NoSuchAlgorithmException, InvalidKeySpecException, IOException {

		// Create a new signer and sign
		final JWSSigner signer = new RSASSASigner(loadPrivateKey());
		final SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID("/privateKey.pem")
                .type(JOSEObjectType.JWT)
                .build(), 
                claims);
        signedJWT.sign(signer);
		return signedJWT;
	}

	@Override
	public boolean validateTokenString(final SignedJWT jws) throws JOSEException, 
			UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return jws.verify(new RSASSAVerifier(loadPublicKey()));
	}

	@Override
	public boolean validateWithDifferentKeyTokenString(final SignedJWT jwt)
			throws JOSEException{
		return jwt.verify(new MACVerifier(SECRET));
	}
	
	@Override
	public JWTClaimsSet decodeToken(final SignedJWT jwt)
			throws JOSEException, ParseException, UnsupportedEncodingException,
			NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		if (!validateTokenString(jwt)) {
			System.out.println("Token not vaild");
			return null;
		}
		return jwt.getJWTClaimsSet();
	}
}