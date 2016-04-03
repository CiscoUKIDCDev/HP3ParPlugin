/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package com.cisco.matday.ucsd.hp3par.test;

import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.TokenExpiredException;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPG;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMembers;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.volumes.CreateVolumeRestCall;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeInformation;

public class VolumeTest {

	final static String ipAddress = "10.51.8.210";
	final static String user = "3paradm";
	final static String password = "3pardata";

	public static void main(String[] args) {
		try {
			Logger.getRootLogger().setLevel(Level.INFO);
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
			
			HP3ParVolumeInformation vol = new HP3ParVolumeInformation("VolumeTesat-Test", "FC_r1", 1024, "No comment");
			System.out.println(CreateVolumeRestCall.create(login, vol).getError());
			

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
		catch (TokenExpiredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
