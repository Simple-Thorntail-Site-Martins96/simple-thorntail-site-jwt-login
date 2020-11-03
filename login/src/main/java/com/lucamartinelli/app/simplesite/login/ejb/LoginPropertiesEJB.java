package com.lucamartinelli.app.simplesite.login.ejb;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;

import com.google.gson.Gson;
import com.lucamartinelli.app.simplesite.login.jwt.SignerEJBInterface;
import com.lucamartinelli.app.simplesite.login.util.HashUtil;
import com.lucamartinelli.app.simplesite.login.util.RequestToken;
import com.lucamartinelli.app.simplesite.login.vo.CredentialsVO;
import com.lucamartinelli.app.simplesite.login.vo.UserVO;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Session Bean implementation class LoginProperties
 */
@Stateless
@LocalBean
public class LoginPropertiesEJB {
	
	private final Logger log;
	
	@EJB
	private SignerEJBInterface jwtSigner;

    public LoginPropertiesEJB() {
    	log = Logger.getLogger(LoginPropertiesEJB.class.getCanonicalName());
    }
    
    public String login(String username, String password) throws LoginException  {
    	return login(new CredentialsVO(username, password));
    }
    
    public String login(CredentialsVO cred) throws LoginException {
    	if (cred == null || 
    			cred.getUsername() == null ||
    			cred.getPassword() == null)
    		throw new LoginException("Credential is null");
    	
    	final Properties prop = new Properties();
		try {
			prop.load(this.getClass().getResourceAsStream("/cred/login.properties"));
		} catch (IOException e) {
			log.severe("Properties file not found");
			throw new LoginException("Properties file not found");
		}
		
		final String userString = prop.getProperty(cred.getUsername());
		if (userString == null) {
			log.warning("User " + cred.getUsername() + " not found");
			throw new LoginException("User " + cred.getUsername() + " not found");
		}
		
		final UserVO user = new Gson().fromJson(userString, UserVO.class);
		user.setUsername(cred.getUsername());
		
		final String hashPwd = HashUtil.generateSHA256Hash(cred.getPassword());
		if (!hashPwd.equalsIgnoreCase(user.getPassword())) {
			log.warning("Password wrong");
			throw new LoginException("Password wrong");
		}
		
		log.info("User " + user.getUsername() + " logged successful");
    	
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
