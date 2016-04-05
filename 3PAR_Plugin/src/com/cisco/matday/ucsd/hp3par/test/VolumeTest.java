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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPG;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeRestCall;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeStatus;

// Don't document this test case, it changes too often
@SuppressWarnings("javadoc")
public class VolumeTest {

	final static String ipAddress = "10.51.8.210";
	final static String user = "3paradm";
	final static String password = "3pardata";

	public static void main(String[] args) {

		try {

			Logger.getRootLogger().setLevel(Level.INFO);

			// Don't warn that I'm using test methods
			@SuppressWarnings("deprecation")
			HP3ParCredentials login = new HP3ParCredentials(ipAddress, user, password);

			HP3ParVolumeList list = new HP3ParVolumeList(login);
			HP3ParSystem systemInfo = new HP3ParSystem(login);
			HP3ParCPG cpgInfo = new HP3ParCPG(login);
			System.out.println("Model: " + systemInfo.getSystem().getModel());
			System.out.println("Total Volumes: " + list.getVolume().getTotal());
			System.out.println("Total CPGs: " + cpgInfo.getCpg().getTotal());

			// HP3ParVolumeInformation vol = new
			// HP3ParVolumeInformation("SSD-Test",
			// "SSD_r1", 1024, "No comment");
			// System.out.println(CreateVolumeRestCall.create(login,
			// vol).getError());

			HP3ParVolumeStatus s = HP3ParVolumeRestCall.delete(login, "Testing");
			System.out.println(s.getError());

			// HP3ParVolumeList newlist = new HP3ParVolumeList(login);

			/*
			 * for (Iterator<VolumeResponseMembers> i =
			 * newlist.getVolume().getMembers().iterator(); i.hasNext();) {
			 * VolumeResponseMembers volume = i.next();
			 * System.out.println(volume.getUserCPG());
			 * System.out.println(login.getToken()); }
			 * 
			 * /* HP3ParCPG cpglist = new HP3ParCPG(login);
			 * 
			 * for (Iterator<CPGResponseMembers> i =
			 * cpglist.getCpg().getMembers().iterator(); i.hasNext();) {
			 * CPGResponseMembers cpg = i.next(); // Get total long total =
			 * ((cpg.getUsrUsage().getTotalMiB() +
			 * cpg.getSAUsage().getTotalMiB() +
			 * cpg.getSDUsage().getTotalMiB())); long used =
			 * ((cpg.getUsrUsage().getUsedMiB() + cpg.getSAUsage().getUsedMiB()
			 * + cpg.getSDUsage().getUsedMiB())); long free = total - used; int
			 * volumeCount = cpg.getNumTPVVs();
			 * 
			 * System.out.println(cpg.getName() + " == " + total + " == " + free
			 * + " == TPVVS = " + volumeCount);
			 * //System.out.println(login.getToken()); }
			 *//*
				 * } catch (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); } catch (InvalidHP3ParTokenException e)
				 * { // TODO Auto-generated catch block e.printStackTrace(); }
				 * 
				 * }
				 */
		} catch (Exception e) {

		}
	}
}
