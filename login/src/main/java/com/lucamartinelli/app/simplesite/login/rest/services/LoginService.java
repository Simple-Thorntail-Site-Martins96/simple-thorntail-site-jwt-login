package com.lucamartinelli.app.simplesite.login.rest.services;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.security.auth.login.LoginException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.ConfigProvider;

import com.lucamartinelli.app.simplesite.login.ejb.LoginDatabaseEJB;
import com.lucamartinelli.app.simplesite.login.ejb.LoginInMemoryEJB;
import com.lucamartinelli.app.simplesite.login.ejb.LoginPropertiesEJB;
import com.lucamartinelli.app.simplesite.login.vo.CredentialsVO;
import com.lucamartinelli.app.simplesite.login.vo.LoginModes;

@Path("/login")
@RequestScoped
public class LoginService {
	
	@EJB
	private LoginInMemoryEJB inMemoryEJB;
	
	@EJB
	private LoginDatabaseEJB dbEJB;
	
	@EJB
	private LoginPropertiesEJB propEJB;
	
	private static final Logger log = 
			LogManager.getLogManager().getLogger(LoginService.class.getCanonicalName());
	
	private static LoginModes LOGIN_MODE;
	
	@PostConstruct
	private void init() {
		final String loginModeConfKey = "login.mode";
		try {
			LOGIN_MODE = LoginModes.valueOf(ConfigProvider.getConfig()
					.getOptionalValue(loginModeConfKey, String.class)
					.orElse("IN_MEMORY"));
		
		} catch (IllegalArgumentException e) {
			log.severe("ERROR in load login mode, cannot understand the value ["
					+ ConfigProvider.getConfig().getOptionalValue(loginModeConfKey, String.class).get()
					+ "] please fix property configuration with name " + loginModeConfKey 
					+ "\nStarting with default ***IN_MEMORY***");
		}
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_HTML)
	public Response login(CredentialsVO cred) {
		
		log.info("Login Mode: " + LOGIN_MODE);
		switch (LOGIN_MODE) {
		case IN_MEMORY:
			try {
				return Response.ok().entity(inMemoryEJB.login(cred)).build();
			} catch (LoginException e) {
				log.severe("Login failed: " + e.getMessage());
				return Response.status(403).entity(e.getMessage()).build();
			}
		case PROPERTIES:
			try {
				return Response.ok().entity(propEJB.login(cred)).build();
			} catch (LoginException e) {
				log.severe("Login failed: " + e.getMessage());
				return Response.status(403).entity(e.getMessage()).build();
			}
		case DATABASE:
			try {
				return Response.ok().entity(dbEJB.login(cred)).build();
			} catch (LoginException e) {
				log.severe("Login failed: " + e.getMessage());
				return Response.status(403).entity(e.getMessage()).build();
			}
		
		default:
			return Response.serverError().entity("Not manageble scenario").build();
		}
	}
	
	
}
