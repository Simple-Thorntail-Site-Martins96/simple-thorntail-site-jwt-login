package com.lucamartinelli.app.simplesite.login.ejb;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.lucamartinelli.app.simplesite.login.jwt.SignerEJBInterface;
import com.lucamartinelli.app.simplesite.login.util.HashUtil;
import com.lucamartinelli.app.simplesite.login.util.RequestToken;
import com.lucamartinelli.app.simplesite.login.vo.CredentialsVO;
import com.lucamartinelli.app.simplesite.login.vo.UserVO;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

@Stateless
@LocalBean
public class LoginDatabaseEJB {
	
private final Logger log;
	
	@EJB
	private SignerEJBInterface jwtSigner;
	
	@Inject
	@ConfigProperty(name = "database.services.login.endpoint", 
			defaultValue = "http://localhost:8084/database/login")
	private String dbLoginEndpoint;
	
	@Inject
	@ConfigProperty(name = "database.services.login.apikey", 
			defaultValue = "123")
	private String dbLoginAPIkey;

    public LoginDatabaseEJB() {
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
    	
    	
    	final String hashPwd = HashUtil.generateSHA256Hash(credentials.getPassword());
    	credentials.setPassword(hashPwd);
    	
    	log.fine("Building client for endpoint: " + dbLoginEndpoint);
		final Client client = ClientBuilder.newClient();
		final WebTarget webTarget = client.target(dbLoginEndpoint);
		final UserVO user = webTarget.request(MediaType.APPLICATION_JSON)
				.header("X-APi-Key", dbLoginAPIkey)
				.post(Entity.entity(credentials, MediaType.APPLICATION_JSON), UserVO.class);
    	
		if (user == null) {
			log.warning("User credentials are not corrrect");
			throw new LoginException("Credentials not correct");
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
