package com.cisco.matday.ucsd.hp3par.test;

import java.io.IOException;


import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;


public class VolumeTest {
	
	final static String ipAddress = "10.51.8.210";
	final static String user = "3paradm";
	final static String password = "3pardata";
	
	public static void main(String[] args) {
		try {
			//token = new HP3ParToken(new HP3ParCredentials(ipAddress,user,password, true));
			//System.out.println(token.getToken());
			HP3ParCredentials login = new HP3ParCredentials(ipAddress,user,password, true);
			
			HP3ParVolumeList list = new HP3ParVolumeList(login);
			HP3ParSystem systemInfo = new HP3ParSystem(login);
			System.out.println("Model: " + systemInfo.getSystem().getTimeZone());
			System.out.println("Total members: " + list.getVolume().getTotal());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

/*
		try {
			String token = new GetSessionToken(ipAddress, user, password).getNewToken();
			System.out.println("3Par WAPI Auth Token: " + token);
		}
		catch (Exception e) {}
		
		UCSDHttpRequest request = new UCSDHttpRequest(ipAddress,"https", 8080);
			
		request.addContentTypeHeader(HttpRequestConstants.CONTENT_TYPE_JSON);
		
		request.setUri( threeParRESTconstants.GET_SESSION_TOKEN_URI );
			
		request.setMethodType(HttpRequestConstants.METHOD_TYPE_POST);
			
		request.setBodyText( new LoginRequestJSON(user,password).convertToJSON() );
			
		System.out.println("User JSON: " + new LoginRequestJSON(user,password).convertToJSON() );
			
		try {
			request.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		//String token = request.getHttpResponse();
			
		String token = new LoginResponseJSON().getSessionToken(request.getHttpResponse());
				
		*/
	}

}
