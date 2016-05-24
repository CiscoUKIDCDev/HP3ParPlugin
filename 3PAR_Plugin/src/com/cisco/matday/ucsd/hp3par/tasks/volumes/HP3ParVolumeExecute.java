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
package com.cisco.matday.ucsd.hp3par.tasks.volumes;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParCpgException;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParVolumeException;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeEditParams;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeGrowParams;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeParams;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

/**
 * Provides the task logic to perform volume actions on a 3PAR array for both
 * tasks and action buttons
 *
 * @author matt
 *
 */
public class HP3ParVolumeExecute {
	private static Logger logger = Logger.getLogger(HP3ParVolumeExecute.class);

	// All the actions you can do when growing a volume
	@SuppressWarnings("javadoc")
	public static final int VOLUME_GROWTH_STOP_PHYSICAL_COPY = 1;
	@SuppressWarnings("javadoc")
	public static final int VOLUME_GROWTH_RESYNC_PHYSICAL_COPY = 2;
	@SuppressWarnings("javadoc")
	public static final int VOLUME_GROWTH_GROW_VOLUME = 3;
	@SuppressWarnings("javadoc")
	public static final int VOLUME_GROWTH_PROMOTE_VIRTUAL_COPY = 4;
	@SuppressWarnings("javadoc")
	public static final int VOLUME_GROWTH_STOP_PROMOTE_VIRTUAL_COPY = 5;
	@SuppressWarnings("javadoc")
	public static final int VOLUME_GROWTH_TUNE_VOLUME = 6;
	@SuppressWarnings("javadoc")
	public static final int VOLUME_GROWTH_UPDATE_VIRTUAL_COPY = 7;
	@SuppressWarnings("javadoc")
	public static final int VOLUME_GROWTH_SNAPSHOT_ENUM_ACTION = 8;

	/**
	 * Create a new volume
	 *
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws HP3ParCpgException
	 *             if the CPG specified is invalid
	 * @throws Exception
	 *             if the operation was unsuccessful
	 */
	public static HP3ParRequestStatus create(HP3ParCredentials c, CreateVolumeConfig config)
			throws HP3ParCpgException, Exception {
		// Parse out CPG - it's in the format:
		// ID@AccountName@Name
		String[] cpgInfo = config.getCpg().split("@");
		if (cpgInfo.length != 3) {
			logger.warn("CPG didn't return three items! It returned: " + config.getCpg());
			throw new HP3ParCpgException("Invalid CPG");
		}
		String cpgName = cpgInfo[2];

		String copyCpgName = null;

		if (config.getCopyCpg() != null) {
			String[] copyCpgInfo = config.getCopyCpg().split("@");
			if (copyCpgInfo.length == 3) {
				copyCpgName = copyCpgInfo[2];
			}
		}

		// Build volume information object:
		HP3ParVolumeParams volume = new HP3ParVolumeParams(config.getVolumeName(), cpgName, config.getVolume_size(),
				config.getComment(), config.isThin_provision(), copyCpgName);
		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(volume));
		request.setUri(threeParRESTconstants.GET_VOLUMES_URI);

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
	 * Delete an existing volume
	 *
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws HP3ParVolumeException
	 *             if the volume was incorrect
	 * @throws Exception
	 *             if the operation was unsuccessful
	 */
	public static HP3ParRequestStatus delete(HP3ParCredentials c, DeleteVolumeConfig config)
			throws HP3ParVolumeException, Exception {

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new HP3ParVolumeException("Invalid Volume: " + config.getVolume());
		}
		String volName = volInfo[2];
		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		String uri = "/api/v1/volumes/" + volName;
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
				HP3ParInventory.update(c, true, "Volume deletion");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Return the same reference as passed for convenience and clarity
		return status;

	}

	/**
	 * Edit a volume
	 *
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws HP3ParVolumeException
	 *             If the volume is invalid
	 * @throws Exception
	 *             if the operation was unsuccessful
	 */
	public static HP3ParRequestStatus edit(HP3ParCredentials c, EditVolumeConfig config)
			throws HP3ParVolumeException, Exception {
		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new HP3ParVolumeException("Invalid Volume: " + config.getVolume());
		}
		String volName = volInfo[2];

		String newVolName = null;

		// If the volume name hasn't changed, set it to null else 3PAR gives an
		// error
		if (!config.getNewVolumeName().equals(volName)) {
			newVolName = config.getNewVolumeName();
		}

		String copyCpgName = null;

		if (config.getCopyCpg() != null) {
			String[] copyCpgInfo = config.getCopyCpg().split("@");
			// Can leave the copy CPG as null if this errors out
			if (copyCpgInfo.length == 3) {
				copyCpgName = copyCpgInfo[2];
			}
		}

		if (copyCpgName == null) {
			copyCpgName = "";
		}

		// VolumeResponseMember volinfo = new HP3ParVolumeInfo(c,
		// config.getOriginalName()).getMember();
		VolumeResponseMember volinfo = HP3ParInventory.getVolumeInfo(c, volName);

		// If the new copy CPG name is the same as the old one, set it to
		// null (3PAR will otherwise return an error)
		if ((copyCpgName.equals(volinfo.getCopyCPG())) || ("".equals(copyCpgName))) {
			copyCpgName = null;
		}

		// Build copy parameter list:
		HP3ParVolumeEditParams p = new HP3ParVolumeEditParams(newVolName, null, config.getComment(), copyCpgName);

		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		String uri = "/api/v1/volumes/" + volName;
		request.setUri(uri);

		// Use defaults for a PUT request
		request.setPutDefaults(gson.toJson(p));

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
				HP3ParInventory.update(c, true, "Volume edited");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return status;
	}

	/**
	 * Grow an existing volume
	 *
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws HP3ParVolumeException
	 *             if the volume was incorrect
	 * @throws Exception
	 *             if the operation was unsuccessful
	 */
	public static HP3ParRequestStatus grow(HP3ParCredentials c, GrowVolumeConfig config)
			throws HP3ParVolumeException, Exception {

		final String volName = config.getVolume().split("@")[2];
		final HP3ParVolumeGrowParams p = new HP3ParVolumeGrowParams(VOLUME_GROWTH_GROW_VOLUME, config.getNewSize());

		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		final String uri = "/api/v1/volumes/" + volName;
		request.setUri(uri);

		// Use defaults for a PUT request
		request.setPutDefaults(gson.toJson(p));

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
				HP3ParInventory.update(c, true, "Volume expanded");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return status;
	}
}
