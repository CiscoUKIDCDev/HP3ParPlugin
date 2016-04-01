package com.cisco.matday.ucsd.hp3par.test;

import java.io.IOException;
import java.util.Iterator;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.cpg.CPGResponseMembers;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPG;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;

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
			HP3ParCPG cpgInfo = new HP3ParCPG(login);
			System.out.println("Model: " + systemInfo.getSystem().getModel());
			System.out.println(systemInfo.getSystem().getFreeCapacityMiB() / 1024d);
			System.out.println(systemInfo.getSystem().getTotalCapacityMiB() / 1024d);
			System.out.println("Total members: " + list.getVolume().getTotal());
			System.out.println(cpgInfo.getCpg().getTotal());
			

			//HP3ParVolumeList newlist = new HP3ParVolumeList(login);

			/*for (Iterator<VolumeResponseMembers> i = newlist.getVolume().getMembers().iterator(); i.hasNext();) {
				VolumeResponseMembers volume = i.next();
				System.out.println(volume.getUserCPG());
				System.out.println(login.getToken());
			}*/
			
			HP3ParCPG cpglist = new HP3ParCPG(login);

			for (Iterator<CPGResponseMembers> i = cpglist.getCpg().getMembers().iterator(); i.hasNext();) {
				CPGResponseMembers cpg = i.next();
				// Get total
				long total = ((cpg.getUsrUsage().getTotalMiB() + cpg.getSAUsage().getTotalMiB() + cpg.getSDUsage().getTotalMiB()));
				long used = ((cpg.getUsrUsage().getUsedMiB() + cpg.getSAUsage().getUsedMiB() + cpg.getSDUsage().getUsedMiB()));
				long free = total - used;
				int volumeCount = cpg.getNumTPVVs();
				
				System.out.println(cpg.getName() + " == " + total + " == " + free + " == TPVVS = " + volumeCount);
				//System.out.println(login.getToken());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
