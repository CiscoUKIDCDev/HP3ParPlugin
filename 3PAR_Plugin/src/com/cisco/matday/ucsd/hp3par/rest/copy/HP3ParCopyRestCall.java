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
package com.cisco.matday.ucsd.hp3par.rest.copy;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParCopyParams;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParVolumeAction;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParSnapshotParams;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestTask;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.google.gson.Gson;

/**
 * Contains static methods for managing volumes on the array
 * 
 * @author Matt Day
 */
public class HP3ParCopyRestCall {

	/**
	 * Creates a 3PAR volume snapshot
	 * 
	 * @param loginCredentials
	 *            Credentials for system
	 * @param volumeName
	 *            Name of volume to snapshot
	 * @param snapshotInformation
	 *            Specification for new snapshot (name, readonly etc)
	 * @return Status of execution (errors etc)
	 * @throws HttpException
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 */
	public static HP3ParRequestStatus createSnapshot(HP3ParCredentials loginCredentials, String volumeName,
			HP3ParSnapshotParams snapshotInformation) throws HttpException, IOException, InvalidHP3ParTokenException {

		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(loginCredentials);

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(new HP3ParVolumeAction("createSnapshot", snapshotInformation)));
		// Generate URI:
		String uri = "/api/v1/volumes/" + volumeName;
		request.setUri(uri);

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
		}
		// Return the same reference as passed for convenience and clarity
		return status;

	}

	/**
	 * Creates a 3PAR volume copy
	 * 
	 * @param loginCredentials
	 *            Credentials for system
	 * @param volumeName
	 *            Name of volume to snapshot
	 * @param copyInformation
	 *            Specification for new volume
	 * @return Status of execution (errors etc)
	 * @throws HttpException
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 */
	public static HP3ParRequestStatus createCopy(HP3ParCredentials loginCredentials, String volumeName,
			HP3ParCopyParams copyInformation) throws HttpException, IOException, InvalidHP3ParTokenException {

		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(loginCredentials);

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(new HP3ParVolumeAction("createPhysicalCopy", copyInformation)));
		// Generate URI:
		String uri = "/api/v1/volumes/" + volumeName;
		request.setUri(uri);

		request.execute();
		String response = request.getHttpResponse();

		HP3ParRequestTask r = gson.fromJson(response, HP3ParRequestTask.class);

		// We should get a task ID back
		if (!(r.getTaskid() > 0)) {
			HP3ParVolumeMessage message = gson.fromJson(response, HP3ParVolumeMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setSuccess(false);
		}
		else {
			status.setSuccess(true);
		}
		// Return the same reference as passed for convenience and clarity
		return status;

	}

}
