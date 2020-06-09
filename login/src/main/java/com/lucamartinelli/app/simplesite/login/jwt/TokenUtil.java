package com.lucamartinelli.app.simplesite.login.jwt;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import java.util.Base64.Decoder;

import net.minidev.json.JSONArray;

public class TokenUtil {
	public static final String ISSUER = "LucaApproved";
	protected static final String SECRET = "BA8905E0040CBA9ED554EAFD5E8D0D7AB1CD87F0E2295380A7775121736BBF9C";

	protected TokenUtil() {
	}
	
	protected static RSAPrivateKey loadPrivateKey() 
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String keyPath = new String(
				"/private_key_pkcs8.pem");

		// read key bytes
		byte[] keyBytes = readKeyDecoded(keyPath);

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) kf.generatePrivate(spec);
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
		InputStream in = TokenUtil.class.getResourceAsStream(path);
		byte[] keyBytes = new byte[in.available()];
		in.read(keyBytes);
		in.close();

		String pubKey = new String(keyBytes, "UTF-8");
		pubKey = pubKey.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
		pubKey = pubKey.replaceAll("(-+BEGIN PRIVATE KEY-+\\r?\\n|-+END PRIVATE KEY-+\\r?\\n?)", "");
		pubKey = pubKey.replaceAll("\\n", "");
		pubKey = pubKey.replaceAll("\\r", "");

		Decoder decoder = Base64.getDecoder();
		return decoder.decode(pubKey);
	}
	
	protected static JSONArray loadGroups() {
        JSONArray result = new JSONArray();
        result.add("dev");
        return result;
	}
}
