package com.lucamartinelli.app.simplesite.login.persistency;

import java.sql.Connection;
import java.sql.SQLException;

import javax.ejb.Singleton;

@Singleton
public class ConnectionManager {
	
	private Connection conn = null;
	
	public Connection getConnection() throws SQLException {
		
		if (conn == null ||  conn.isClosed()) {
			// TODO
		}
		
		return conn;
	}
	
}
