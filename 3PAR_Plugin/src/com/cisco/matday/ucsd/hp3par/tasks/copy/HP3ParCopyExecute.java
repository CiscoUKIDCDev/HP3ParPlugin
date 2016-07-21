/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
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
package com.cisco.matday.ucsd.hp3par.tasks.copy;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParCpgException;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParVolumeException;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParCopyParams;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParSnapshotParams;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParVolumeAction;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestTask;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.google.gson.Gson;

/**
 * Provides the task logic to perform copy actions on a 3PAR array for both
 * tasks and action buttons
 *
 * @author matt
 *
 */
public class HP3ParCopyExecute {
	private static Logger logger = Logger.getLogger(HP3ParCopyExecute.class);

	/**
	 * Copy an existing volume
	 *
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws HP3ParVolumeException
	 *             If the volume was invalid
	 * @throws Exception
	 *             if the operation was unsuccessful
	 * @throws HP3ParCpgException
	 *             if the CPG is invalid
	 */
	public static HP3ParRequestStatus copy(HP3ParCredentials c, CreateVolumeCopyConfig config)
			throws HP3ParVolumeException, HP3ParCpgException, Exception {

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new HP3ParVolumeException("Invalid Volume: " + config.getVolume());
		}
		final String volName = volInfo[2];
		// Parse out CPG - it's in the format:
		// ID@AccountName@Name
		String[] cpgInfo = config.getCpg().split("@");
		if (cpgInfo.length != 3) {
			logger.warn("CPG didn't return three items! It returned: " + config.getCpg());
			throw new HP3ParCpgException("Invalid CPG");
		}
		final String cpgName = cpgInfo[2];

		String copyCpgName = null;

		if (config.getCopyCpg() != null) {
			String[] copyCpgInfo = config.getCopyCpg().split("@");
			if (copyCpgInfo.length == 3) {
				copyCpgName = copyCpgInfo[2];
			}
		}

		// Build copy parameter list:
		final HP3ParCopyParams p = new HP3ParCopyParams(config.getNewVolumeName(), cpgName, config.isOnline(),
				config.isThinProvision(), copyCpgName);

		Gson gson = new Gson();
		final HP3ParRequestStatus status = new HP3ParRequestStatus();

		final UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(new HP3ParVolumeAction("createPhysicalCopy", p)));
		// Generate URI:
		final String uri = "/api/v1/volumes/" + volName;
		request.setUri(uri);

		request.execute();
		final String response = request.getHttpResponse();

		final HP3ParRequestTask r = gson.fromJson(response, HP3ParRequestTask.class);

		// We should get a task ID back
		if (!(r.getTaskid() > 0)) {
			HP3ParVolumeMessage message = gson.fromJson(response, HP3ParVolumeMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setSuccess(false);
		}
		else {
			status.setSuccess(true);
			// Update the inventory
			try {
				HP3ParInventory.update(c, true, "Volume Copy");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Return the same reference as passed for convenience and clarity
		return status;

	}

	/**
	 * Snapshot an existing volume
	 *
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws Exception
	 *             if the operation was unsuccessful
	 * @throws HP3ParVolumeException
	 *             If the volume is invalid
	 */
	public static HP3ParRequestStatus snapshot(HP3ParCredentials c, CreateVolumeSnapshotConfig config)
			throws HP3ParVolumeException, Exception {

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new HP3ParVolumeException("Invalid Volume: " + config.getVolume());
		}
		final String volName = volInfo[2];

		final HP3ParSnapshotParams p = new HP3ParSnapshotParams(config.getSnapshotName(), config.isReadOnly(),
				config.getComment());

		Gson gson = new Gson();
		final HP3ParRequestStatus status = new HP3ParRequestStatus();

		final UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(c);

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(new HP3ParVolumeAction("createSnapshot", p)));
		// Generate URI:
		final String uri = "/api/v1/volumes/" + volName;
		request.setUri(uri);

		request.execute();
		final String response = request.getHttpResponse();

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
				HP3ParInventory.update(c, true, "Snapshot creation");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Return the same reference as passed for convenience and clarity
		return status;

	}

}
