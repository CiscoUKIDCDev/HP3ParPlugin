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
package com.cisco.matday.ucsd.hp3par.tasks.hostsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.hostsets.json.HostSetRequest;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.cisco.matday.ucsd.hp3par.tasks.copy.HP3ParCopyExecute;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

public class HP3ParHostSetExecute {
	private static Logger logger = Logger.getLogger(HP3ParCopyExecute.class);

	public static HP3ParRequestStatus create(HP3ParCredentials c, CreateHostSetConfig config) throws Exception {
		String[] setmembers = new String[config.getHosts().split(",").length];
		String[] hostList = config.getHosts().split(",");
		for (int i = 0; i < hostList.length; i++) {
			setmembers[i] = hostList[i].split("@")[2];
		}

		HostSetRequest p = new HostSetRequest(config.getHostSetName(), setmembers, config.getComment(),
				config.getDomain());

		Gson gson = new Gson();
		final HP3ParRequestStatus status = new HP3ParRequestStatus();

		logger.info("JSON: " + gson.toJson(p));

		final UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(p));
		request.setUri(threeParRESTconstants.GET_HOSTSETS_URI);

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

	public static HP3ParRequestStatus edit(HP3ParCredentials c, EditHostSetConfig config) throws Exception {
		final String hostSetName = config.getHostSet().split("@")[2];

		List<String> currentMembers = Arrays.asList(HP3ParInventory.getHostSetInfo(c, hostSetName).getSetMembers());

		List<String> keepList = new ArrayList<String>();

		String[] hostList = config.getHosts().split(",");
		for (String element : hostList) {
			final String memberName = element.split("@")[2];
			if (currentMembmemberName)
		}

		return null;
	}

}
