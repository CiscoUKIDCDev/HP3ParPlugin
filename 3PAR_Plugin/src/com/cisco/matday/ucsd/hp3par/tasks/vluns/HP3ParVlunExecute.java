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
package com.cisco.matday.ucsd.hp3par.tasks.vluns;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.vluns.rest.HP3ParVlunParams;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

/**
 * REST execution tasks for VLUNs
 *
 * @author Matt Day
 *
 */
public class HP3ParVlunExecute {
	private static Logger logger = Logger.getLogger(HP3ParVlunExecute.class);

	/**
	 * Craete a new VLUN
	 *
	 * @param c
	 * @param config
	 * @return Status
	 * @throws Exception
	 */
	public static HP3ParRequestStatus create(HP3ParCredentials c, CreateVlunConfig config) throws Exception {

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new Exception("Invalid Volume: " + config.getVolume());
		}
		String volName = volInfo[2];

		// Get the host name, it's in the format:
		// id@account@name
		String[] hostInfo = config.getHost().split("@");
		if (hostInfo.length != 3) {
			logger.warn("Hostname didn't return three items! It returned: " + config.getHost());
			throw new Exception("Invalid host: " + config.getHost());
		}
		String hostName = volInfo[2];

		// Build vlun information object:
		HP3ParVlunParams params = new HP3ParVlunParams(volName, hostName, config.getLun());
		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		logger.info("REST: " + gson.toJson(params));

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(params));
		request.setUri(threeParRESTconstants.GET_VLUNS_URI);

		request.execute();
		String response = request.getHttpResponse();

		// Shouldn't get a response if all is good... if we did it's trouble
		if (!response.equals("")) {
			HP3ParVolumeMessage message = gson.fromJson(response, HP3ParVolumeMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setSuccess(false);
		}
		else {
			status.setSuccess(true);
			// Update the inventory
			try {
				HP3ParInventory.update(c, true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Return the same reference as passed for convenience and clarity
		return status;

	}

}
