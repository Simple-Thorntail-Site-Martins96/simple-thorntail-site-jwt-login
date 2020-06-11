package com.lucamartinelli.app.simplesite.login.rest.services;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lucamartinelli.app.simplesite.login.ejb.JWTValidationEJB;

@Path("/validate")
public class ValidationService {
	
	@EJB
	private JWTValidationEJB validation;
	
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	public Response validate(final String jwt) {
		if (validation.isValid(jwt))
			return Response.status(201).build();
		return Response.status(401).build();
	}
	
	
}
