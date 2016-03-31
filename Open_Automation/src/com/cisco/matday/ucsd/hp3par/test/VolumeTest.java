package com.cisco.matday.ucsd.hp3par.test;

import java.io.IOException;
import java.util.Iterator;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.VolumeResponseMembers;

public class VolumeTest {

	final static String ipAddress = "10.51.8.210";
	final static String user = "3paradm";
	final static String password = "3pardata";

	public static void main(String[] args) {
		try {
			// token = new HP3ParToken(new
			// HP3ParCredentials(ipAddress,user,password, true));
			// System.out.println(token.getToken());
			HP3ParCredentials login = new HP3ParCredentials(ipAddress, user, password, true);

			HP3ParVolumeList list = new HP3ParVolumeList(login);
			HP3ParSystem systemInfo = new HP3ParSystem(login);
			System.out.println("Model: " + systemInfo.getSystem().getModel());
			System.out.println("Total members: " + list.getVolume().getTotal());

			HP3ParVolumeList newlist = new HP3ParVolumeList(login);

			for (Iterator<VolumeResponseMembers> i = newlist.getVolume().getMembers().iterator(); i.hasNext();) {
				VolumeResponseMembers volume = i.next();
				System.out.println(volume.getUserCPG());
				System.out.println(login.getToken());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
