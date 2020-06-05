package com.lucamartinelli.app.login.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class HashUtil {
	
	private static final Logger log = Logger.getLogger(HashUtil.class.getCanonicalName());
	
	public static String generateSHA256Hash(String pwd) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			log.severe("Error in get algorithm SHA-256: " + e.getMessage());
			return "";
		}
		byte[] encodedhash = digest.digest(pwd.getBytes());
		final String hash = bytesToHex(encodedhash);
		log.info("Generated HASH: " + hash);
		return hash;
	}
	
	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
}
