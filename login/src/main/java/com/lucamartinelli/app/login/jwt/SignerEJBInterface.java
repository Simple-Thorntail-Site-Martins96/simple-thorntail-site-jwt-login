package com.lucamartinelli.app.login.jwt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public interface SignerEJBInterface {

	/**
	 * Utility method to generate a signed JWT from a JSON resource file that is
	 * signed by secret key
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 */
	SignedJWT generateToken(JWTClaimsSet claims) throws JOSEException, UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeySpecException, IOException;

	boolean validateTokenString(SignedJWT jws) throws JOSEException, UnsupportedEncodingException,
			NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	boolean validateWithDifferentKeyTokenString(SignedJWT jwt) throws JOSEException;

	JWTClaimsSet decodeToken(SignedJWT jwt) throws JOSEException, ParseException, UnsupportedEncodingException,
			NoSuchAlgorithmException, InvalidKeySpecException, IOException;

}