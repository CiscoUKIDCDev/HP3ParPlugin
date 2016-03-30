package com.cisco.matday.ucsd.hp3par.test;

import com.cisco.rwhitear.threeParREST.authenticate.GetSessionToken;

public class VolumeTest {

	public static void main(String[] args) {

		String ipAddress = "10.51.8.210";
		String user = "3paradm";
		String password = "3pardata";
		try {
			String token = new GetSessionToken(ipAddress, user, password).getNewToken();
			System.out.println("3Par WAPI Auth Token: " + token);
		}
		catch (Exception e) {}

		
	}

}
