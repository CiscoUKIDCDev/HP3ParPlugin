package com.cisco.matday.ucsd.hp3par.rest;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;

public class TokenManager {
	private static HP3ParToken token;
	private static long time = 0;
	
	public static String getToken (HP3ParCredentials credentials) throws HttpException, IOException, TokenExpiredException {
		Date d = new Date();
		if (token == null) {
			token = new HP3ParToken(credentials);
			
		}
		else if ((d.getTime() - time) > HP3ParConstants.TOKEN_LIFE) {
			
		}

		return token.getToken();
	}
}
