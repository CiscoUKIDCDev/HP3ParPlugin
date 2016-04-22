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
package com.cisco.matday.ucsd.hp3par.tasks.cpg;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.hosts.HP3ParHostParams;
import com.cisco.matday.ucsd.hp3par.rest.hosts.HP3ParHostRestCall;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseDescriptors;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.tasks.hosts.CreateHostConfig;
import com.cisco.matday.ucsd.hp3par.tasks.hosts.DeleteHostConfig;

/**
 * @author Matt Day
 *
 */
public class HP3ParHostExecute {
	private static Logger logger = Logger.getLogger(HP3ParHostExecute.class);

	/**
	 * Create a 3PAR host
	 *
	 * @param c
	 * @param config
	 * @return status
	 * @throws Exception
	 */
	public static HP3ParRequestStatus create(HP3ParCredentials c, CreateHostConfig config) throws Exception {

		HostResponseDescriptors desc = new HostResponseDescriptors();
		desc.setComment(config.getComment());
		desc.setIPAddr(config.getIpaddr());
		desc.setContact(config.getContact());
		desc.setLocation(config.getLocation());
		desc.setModel(config.getModel());
		desc.setOs(config.getOs());

		HP3ParHostParams params = new HP3ParHostParams(config.getHostName(), config.getDomain(), desc);

		return HP3ParHostRestCall.create(c, params);

	}

	/**
	 * Delete a 3PAR host
	 *
	 * @param c
	 * @param config
	 * @return status
	 * @throws Exception
	 */
	public static HP3ParRequestStatus delete(HP3ParCredentials c, DeleteHostConfig config) throws Exception {

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getHost().split("@");
		if (volInfo.length != 3) {
			logger.warn("Host didn't return three items! It returned: " + config.getHost());
			throw new Exception("Invalid Volume: " + config.getHost());
		}
		String volName = volInfo[2];
		return HP3ParHostRestCall.delete(c, volName);

	}

}
