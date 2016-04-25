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
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.hostsets.json.HP3ParHostSetEditParams;
import com.cisco.matday.ucsd.hp3par.rest.hostsets.json.HostSetRequest;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.cisco.matday.ucsd.hp3par.tasks.copy.HP3ParCopyExecute;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

/**
 * Commands to interact with host sets
 *
 * @author Matt Day
 *
 */
public class HP3ParHostSetExecute {
	private static Logger logger = Logger.getLogger(HP3ParCopyExecute.class);

	/**
	 * Create a new host set
	 *
	 * @param c
	 * @param config
	 * @return status
	 * @throws Exception
	 */
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

	/**
	 * Edit an existing host set
	 *
	 * @param c
	 * @param config
	 * @return Status
	 * @throws Exception
	 */
	@SuppressWarnings("boxing")
	public static HP3ParRequestStatus edit(HP3ParCredentials c, EditHostSetConfig config) throws Exception {
		final String hostSetName = config.getHostSet().split("@")[2];

		HashMap<String, Boolean> keepMap = new HashMap<>();

		final String newMembers = config.getHosts();
		final String[] oldMembers = HP3ParInventory.getHostSetInfo(c, hostSetName).getSetMembers();

		List<String> remove = new ArrayList<>();
		List<String> add = new ArrayList<>();

		// The 3PAR array edit/remove MUST not contain elements already in the
		// array, so we need to jump through some hoops

		// Assume we're not keeping anything
		for (String host : oldMembers) {
			keepMap.put(host, false);
		}
		// Compute which ones to keep
		for (String host : newMembers.split(",")) {
			keepMap.put(host.split("@")[2], true);
		}
		// Remove anything not matching:
		for (String host : keepMap.keySet()) {
			if (!keepMap.get(host)) {
				remove.add(host);
			}
		}
		// Assume everything new should be added as new
		for (String host : newMembers.split(",")) {
			keepMap.put(host.split("@")[2], true);
		}
		// Anything currently in the list should not
		for (String host : oldMembers) {
			keepMap.put(host, false);
		}
		for (String host : keepMap.keySet()) {
			if (keepMap.get(host)) {
				add.add(host);
			}
		}

		// Build add parameter list:
		HP3ParHostSetEditParams addParams = new HP3ParHostSetEditParams(HP3ParConstants.SET_ACTION_ADD,
				config.getHostSetName(), config.getComment(), (String[]) add.toArray());
		// Build add parameter list:
		HP3ParHostSetEditParams removeParams = new HP3ParHostSetEditParams(HP3ParConstants.SET_ACTION_REMOVE,
				config.getHostSetName(), config.getComment(), (String[]) remove.toArray());

		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		String uri = "/api/v1/hostsets/" + hostSetName;
		request.setUri(uri);

		logger.info("Add rest: " + gson.toJson(addParams));
		logger.info("Remove rest: " + gson.toJson(removeParams));

		// Use defaults for a PUT request
		request.setPutDefaults(gson.toJson(addParams));
		request.execute();
		String response = request.getHttpResponse();

		// Shouldn't get a response if all is good... if we did it's trouble
		if (!response.equals("")) {
			HP3ParVolumeMessage message = gson.fromJson(response, HP3ParVolumeMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setSuccess(false);
			return status;
		}

		// Use defaults for a PUT request
		request.setPutDefaults(gson.toJson(removeParams));
		request.execute();
		response = request.getHttpResponse();
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
