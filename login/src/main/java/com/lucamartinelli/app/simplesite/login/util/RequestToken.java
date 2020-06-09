package com.lucamartinelli.app.simplesite.login.util;

import java.util.Arrays;
import java.util.Date;

import com.lucamartinelli.app.simplesite.login.vo.UserVO;
import com.nimbusds.jwt.JWTClaimsSet;

import net.minidev.json.JSONArray;

public class RequestToken {
	
	private static final String ISSUER = "LucaApproved";
	
	public static JWTClaimsSet createClaims(final UserVO user) {
		final long currentTime = System.currentTimeMillis();
		return new JWTClaimsSet.Builder()
				.subject(user.getName() + " " + user.getSurname())
				.claim("developer_name", "Luca")
				.issuer(ISSUER)
				.claim("groups", loadGroups(user.getRoles()))
				//Expiration 30 days
				.expirationTime(new Date(currentTime + 2592000000L))
				.issueTime(new Date(currentTime))
				.build();
	}

	protected static JSONArray loadGroups(final String[] roles) {
        final JSONArray result = new JSONArray();
        result.addAll(Arrays.asList(roles));
        return result;
	}
	
}
