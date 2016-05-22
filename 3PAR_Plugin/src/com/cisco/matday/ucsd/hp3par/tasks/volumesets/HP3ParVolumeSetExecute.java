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
package com.cisco.matday.ucsd.hp3par.tasks.volumesets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.sets.json.HP3ParSetEditParams;
import com.cisco.matday.ucsd.hp3par.rest.sets.json.SetRequest;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.cisco.matday.ucsd.hp3par.tasks.copy.HP3ParCopyExecute;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

/**
 * Commands to interact with volume sets
 *
 * @author Matt Day
 *
 */
public class HP3ParVolumeSetExecute {
	private static Logger logger = Logger.getLogger(HP3ParCopyExecute.class);

	/**
	 * Create a new volume set
	 *
	 * @param c
	 * @param config
	 * @return status
	 * @throws Exception
	 */
	public static HP3ParRequestStatus create(HP3ParCredentials c, CreateVolumeSetConfig config) throws Exception {
		String[] setmembers = new String[config.getVolumes().split(",").length];
		String[] volumeList = config.getVolumes().split(",");
		for (int i = 0; i < volumeList.length; i++) {
			setmembers[i] = volumeList[i].split("@")[2];
		}

		SetRequest p = new SetRequest(config.getVolumeSetName(), setmembers, config.getComment(), config.getDomain());

		Gson gson = new Gson();
		final HP3ParRequestStatus status = new HP3ParRequestStatus();

		logger.info("JSON: " + gson.toJson(p));

		final UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(p));
		request.setUri(threeParRESTconstants.GET_VOLUMESETS_URI);

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
				HP3ParInventory.update(c, true, "Volume creation");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Return the same reference as passed for convenience and clarity
		return status;
	}

	/**
	 * Add a volume to an existing volume set
	 *
	 * @param c
	 * @param config
	 * @return Status
	 * @throws Exception
	 */
	public static HP3ParRequestStatus add(HP3ParCredentials c, AddVolumeToVolumeSetConfig config) throws Exception {
		final String volumeSetName = config.getVolumeSet().split("@")[2];
		final String volumeName = config.getVolume().split("@")[2];

		final String[] addArray = {
				volumeName
		};

		HP3ParSetEditParams addParams = new HP3ParSetEditParams(HP3ParConstants.SET_ACTION_ADD, addArray);

		HP3ParRequestStatus status = new HP3ParRequestStatus();

		// Add new members
		if (addArray.length > 0) {
			// Use defaults for a PUT request
			status = doPut(addParams, volumeSetName, c);
			if (!status.isSuccess()) {
				return status;
			}
		}

		// Update the inventory
		try {
			HP3ParInventory.update(c, true, "Added volume to volume set");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return status;

	}

	/**
	 * Remove a volume from an existing volume set
	 *
	 * @param c
	 * @param config
	 * @return Status
	 * @throws Exception
	 */
	public static HP3ParRequestStatus remove(HP3ParCredentials c, RemoveVolumeFromVolumeSetConfig config)
			throws Exception {
		final String volumeSetName = config.getVolumeSet().split("@")[2];
		final String volumeName = config.getVolume().split("@")[2];

		final String[] removeArray = {
				volumeName
		};

		HP3ParSetEditParams removeParams = new HP3ParSetEditParams(HP3ParConstants.SET_ACTION_REMOVE, removeArray);

		HP3ParRequestStatus status = new HP3ParRequestStatus();

		// Remove members
		if (removeArray.length > 0) {
			status = doPut(removeParams, volumeSetName, c);
			if (!status.isSuccess()) {
				return status;
			}
		}

		// Update the inventory
		try {
			HP3ParInventory.update(c, true, "Removed volume from volume set");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return status;

	}

	/**
	 * Edit an existing volume set
	 *
	 * @param c
	 * @param config
	 * @return Status
	 * @throws Exception
	 */
	@SuppressWarnings("boxing")
	public static HP3ParRequestStatus edit(HP3ParCredentials c, EditVolumeSetConfig config) throws Exception {
		final String volumeSetName = config.getVolumeSet().split("@")[2];

		HashMap<String, Boolean> keepMap = new HashMap<>();

		final String newMembers = config.getVolumes();
		final String[] oldMembers = HP3ParInventory.getVolumeSetInfo(c, volumeSetName).getSetMembers();

		List<String> remove = new ArrayList<>();
		List<String> add = new ArrayList<>();

		// The 3PAR array edit/remove MUST not contain elements already in the
		// array, so we need to jump through some hoops

		// Assume we're not keeping anything
		for (String volume : oldMembers) {
			keepMap.put(volume, false);
		}
		// Compute which ones to keep
		for (String volume : newMembers.split(",")) {
			keepMap.put(volume.split("@")[2], true);
		}
		// Remove anything not matching:
		for (String volume : keepMap.keySet()) {
			if (!keepMap.get(volume)) {
				remove.add(volume);
			}
		}
		// Assume everything new should be added as new
		for (String volume : newMembers.split(",")) {
			keepMap.put(volume.split("@")[2], true);
		}
		// Anything currently in the list should not
		for (String volume : oldMembers) {
			keepMap.put(volume, false);
		}
		for (String volume : keepMap.keySet()) {
			if (keepMap.get(volume)) {
				add.add(volume);
			}
		}

		// Build add parameter list:
		String[] addArray = add.toArray(new String[add.size()]);
		HP3ParSetEditParams addParams = new HP3ParSetEditParams(HP3ParConstants.SET_ACTION_ADD, addArray);
		// Build add parameter list:
		String[] removeArray = remove.toArray(new String[remove.size()]);
		HP3ParSetEditParams removeParams = new HP3ParSetEditParams(HP3ParConstants.SET_ACTION_REMOVE, removeArray);
		// Build rename parameter list:
		HP3ParSetEditParams renameParams = new HP3ParSetEditParams(config.getVolumeSetName());
		HP3ParSetEditParams commentParams = new HP3ParSetEditParams();
		commentParams.setComment(config.getComment());

		HP3ParRequestStatus status;

		// Update the comment regardless
		status = doPut(commentParams, volumeSetName, c);
		if (!status.isSuccess()) {
			return status;
		}

		// Add new members
		if (addArray.length > 0) {
			// Use defaults for a PUT request
			status = doPut(addParams, volumeSetName, c);
			if (!status.isSuccess()) {
				return status;
			}
		}
		// Removed unused ones
		if (removeArray.length > 0) {
			status = doPut(removeParams, volumeSetName, c);
			if (!status.isSuccess()) {
				return status;
			}
		}
		// Rename if needed (must be done LAST)
		if (!config.getVolumeSetName().equals(volumeSetName)) {
			status = doPut(renameParams, volumeSetName, c);
			if (!status.isSuccess()) {
				return status;
			}
		}

		// Update the inventory
		try {
			HP3ParInventory.update(c, true, "Volume set edit");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return status;

	}

	private static HP3ParRequestStatus doPut(HP3ParSetEditParams params, String volumeSetName, HP3ParCredentials c)
			throws HttpException, IOException {
		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		String uri = "/api/v1/volumesets/" + volumeSetName;
		request.setUri(uri);

		// Remove anything outstanding
		request.setPutDefaults(gson.toJson(params));
		request.execute();
		String response = request.getHttpResponse();
		if (!response.equals("")) {
			logger.warn("JSON: " + gson.toJson(params));
			HP3ParVolumeMessage message = gson.fromJson(response, HP3ParVolumeMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setSuccess(false);
			return status;
		}
		status.setError("");
		status.setSuccess(true);

		return status;
	}

	/**
	 * Delete an existing volume set
	 *
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws Exception
	 *             if the operation was unsuccessful
	 */
	public static HP3ParRequestStatus delete(HP3ParCredentials c, DeleteVolumeSetConfig config) throws Exception {

		// Get the volume name, it's in the format:
		// id@account@name
		final String volumeSetName = config.getVolumeSet().split("@")[2];
		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		String uri = "/api/v1/volumesets/" + volumeSetName;
		request.setUri(uri);

		// Use defaults for a DELETE request
		request.setDeleteDefaults();

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
				HP3ParInventory.update(c, true, "Volume set deletion");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Return the same reference as passed for convenience and clarity
		return status;

	}

}
