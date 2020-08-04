package com.lucamartinelli.app.simplesite.login.ejb;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;

import com.lucamartinelli.app.simplesite.login.jwt.SignerEJBInterface;
import com.lucamartinelli.app.simplesite.login.vo.CredentialsVO;

@Stateless
@LocalBean
public class LoginDatabaseEJB {
	
private final Logger log;
	
	@EJB
	private SignerEJBInterface jwtSigner;

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
    	
    	// TODO implement here
    	
    	return null;
    }
	
}
