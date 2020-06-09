package com.lucamartinelli.app.simplesite.login.ejb;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;

import com.lucamartinelli.app.simplesite.login.jwt.SignerEJBInterface;
import com.lucamartinelli.app.simplesite.login.util.HashUtil;
import com.lucamartinelli.app.simplesite.login.util.RequestToken;
import com.lucamartinelli.app.simplesite.login.vo.CredentialsVO;
import com.lucamartinelli.app.simplesite.login.vo.UserVO;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Session Bean implementation class LoginInMemoryEJB
 */
@Stateless
@LocalBean
public class LoginInMemoryEJB {
	
	private static final Map<String, UserVO> registredUser = new HashMap<>();
	
	static {
		// All password is: secret1
		registredUser.put("luca", 
				new UserVO("luca", "5b11618c2e44027877d0cd0921ed166b9f176f50587fc91e7534dd2946db77d6",
						new String[]{"dev", "test"}, "Luca", "Martinelli"));
		registredUser.put("developer", 
				new UserVO("developer", "5b11618c2e44027877d0cd0921ed166b9f176f50587fc91e7534dd2946db77d6",
						new String[]{"dev"}, "Dev", "Eloper"));
		registredUser.put("tester", 
				new UserVO("tester", "5b11618c2e44027877d0cd0921ed166b9f176f50587fc91e7534dd2946db77d6",
						new String[]{"test"}, "Test", "Re-test"));
	}
	
	private final Logger log;
	
	@EJB
	private SignerEJBInterface jwtSigner;

    public LoginInMemoryEJB() {
    	log = Logger.getLogger(this.getClass().getCanonicalName());
    }
    
    public String login(String username, String password) throws LoginException {
    	return login(new CredentialsVO(username, password));
    }
    
    public String login(CredentialsVO credentials) throws LoginException {
    	if (credentials == null || 
    			credentials.getUsername() == null ||
    			credentials.getPassword() == null)
    		throw new LoginException("Credential is null");
    	UserVO user = registredUser.get(credentials.getUsername().toLowerCase());
    	if (user == null || user.getPassword() == null)
    		throw new LoginException("User not found");
    	
    	final String hashPwd = HashUtil.generateSHA256Hash(credentials.getPassword());
    	if (!user.getPassword().equals(hashPwd))
    		throw new LoginException("Invalid password");
    	
    	log.info("User " + credentials.getUsername() + " logged successful");
    	
    	String jwt = null;
		try {
			final JWTClaimsSet claims = RequestToken.createClaims(user);
			jwt = jwtSigner.generateToken(claims).serialize();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | JOSEException | IOException e) {
			log.severe("Cannot create JWT, cause: " + e.getMessage());
		}
    	
    	log.info("GENERATED JWT --> " + jwt);
    	
    	return jwt;
    }
    

}
