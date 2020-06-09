package com.lucamartinelli.app.simplesite.login.rest.services;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

public class HealthCheckService implements HealthCheck {

	@Override
	public HealthCheckResponse call() {
		return HealthCheckResponse.up("login");
	}
	
}
