package com.lucamartinelli.app.simplesite.login.ejb;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;

@Stateless
@LocalBean
public class JWTValidationEJB {
	
	private final Logger log;
	
    public JWTValidationEJB() {
    	log = Logger.getLogger(this.getClass().getCanonicalName());
    }
	
	public boolean isValid(final String jwt) {
		SignedJWT jws = null;
		try {
			jws = SignedJWT.parse(jwt);
		} catch (ParseException e1) {
			log.log(Level.WARNING, "Error in parsing jwt for validation, jwt is not valid");
			return false;
		}
		if (jws == null)
			return false;
		try {
			return validateTokenString(jws);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | JOSEException | IOException e) {
			log.log(Level.SEVERE, "Exception during validation Signed JWT", e);
			return false;
		}
	}
	
	private boolean validateTokenString(final SignedJWT jws) throws JOSEException, 
			UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return jws.verify(new RSASSAVerifier(loadPublicKey()));
	}
	
	protected static RSAPublicKey loadPublicKey()
			throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		String keyPathFile = "/public_key.pem";

		// read key bytes
		byte[] keyBytes = readKeyDecoded(keyPathFile);

		// generate public key
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(spec);
		return (RSAPublicKey) publicKey;
	}
	
	private static byte[] readKeyDecoded(String path) throws IOException {
		// read key bytes
		InputStream in = JWTValidationEJB.class.getResourceAsStream(path);
		byte[] keyBytes = new byte[in.available()];
		in.read(keyBytes);
		in.close();

		String pubKey = new String(keyBytes, "UTF-8");
		pubKey = pubKey.replaceAll("-----BEGIN (.*)-----", "");
		pubKey = pubKey.replaceAll("-----END (.*)----", "");
		pubKey = pubKey.replaceAll("\r\n", "");
		pubKey = pubKey.replaceAll("\n", "");

		Decoder decoder = Base64.getDecoder();
		return decoder.decode(pubKey);
	}
}
