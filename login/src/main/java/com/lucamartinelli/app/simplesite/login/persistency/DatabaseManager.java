package com.lucamartinelli.app.simplesite.login.persistency;

import com.lucamartinelli.app.simplesite.login.vo.UserVO;

public interface DatabaseManager {
	
	public UserVO loginOnDB(String username, String encodedPassoword);
	
}
