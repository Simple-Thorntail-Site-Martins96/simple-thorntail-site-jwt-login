package com.lucamartinelli.app.login.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.lucamartinelli.app.login.vo.CredentialsVO;

/**
 * Session Bean implementation class LoginProperties
 */
@Stateless
@LocalBean
public class LoginProperties {

    public LoginProperties() {
    }
    
    public void login(String username, String password) {
    	login(new CredentialsVO(username, password));
    }
    
    public void login(CredentialsVO cred) {
    	
    }

}
