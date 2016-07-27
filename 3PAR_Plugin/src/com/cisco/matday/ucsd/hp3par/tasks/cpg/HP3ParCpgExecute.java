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
package com.cisco.matday.ucsd.hp3par.tasks.cpg;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.rest.UcsdHttpConnection;
import com.cisco.matday.ucsd.hp3par.rest.UcsdHttpConnection.httpMethod;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.HP3ParCreateCPGParams;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.HP3ParDiskTypeParams;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.HP3ParEditCPGParams;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.HP3ParLDLayoutParams;
import com.cisco.matday.ucsd.hp3par.rest.hosts.HP3ParHostMessage;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

/**
 * Execute CPG related tasks
 *
 * @author Matt Day
 *
 */
public class HP3ParCpgExecute {
	private static Logger logger = Logger.getLogger(HP3ParRequestStatus.class);

	/**
	 * Create a new CPG
	 *
	 * @author Matt Day
	 * @param c
	 *            Credentials to authenticate with
	 * @param config
	 *            Configuration from which to create a new CPG
	 * @return Status of CPG creation
	 * @throws Exception
	 *             In the case the CPG cannot be created
	 *
	 */
	public static HP3ParRequestStatus create(HP3ParCredentials c, CreateCpgConfig config) throws Exception {

		// Create the disk object:
		HP3ParDiskTypeParams d[] = {
				new HP3ParDiskTypeParams(config.getDiskType())
		};

		HP3ParLDLayoutParams ldLayout = new HP3ParLDLayoutParams(config.getRaidType(), d);

		HP3ParCreateCPGParams params = new HP3ParCreateCPGParams(config.getCpgName(), ldLayout);

		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UcsdHttpConnection request = new UcsdHttpConnection(c, httpMethod.POST);

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(params));
		request.setUri(threeParRESTconstants.GET_CPG_URI);

		request.execute();
		String response = request.getHttpResponse();

		// Shouldn't get a response if all is good... if we did it's trouble
		if (!response.equals("")) {
			HP3ParHostMessage message = gson.fromJson(response, HP3ParHostMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setSuccess(false);
		}
		else {
			status.setSuccess(true);
			// Update the inventory
			try {
				HP3ParInventory.update(c, true, "CPG creation");
			}
			catch (Exception e) {
				logger.warn("Error updating: " + e.getMessage());
			}
		}
		// Return the same reference as passed for convenience and clarity
		return status;
	}

	/**
	 * Delete an existing CPG
	 *
	 * @author Matt Day
	 * @param c
	 *            Credentials to authenticate with
	 * @param config
	 *            Configuration from which to delete a CPG
	 * @return Status of CPG deletion
	 * @throws Exception
	 *             In the case the CPG cannot be deleted
	 *
	 */
	public static HP3ParRequestStatus delete(HP3ParCredentials c, DeleteCpgConfig config) throws Exception {

		final String cpgName = config.getCpg().split("@")[2];
		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UcsdHttpConnection request = new UcsdHttpConnection(c, httpMethod.DELETE);
		// Use defaults for a DELETE request
		request.setDeleteDefaults();

		String uri = "/api/v1/cpgs/" + cpgName;
		request.setUri(uri);

		request.execute();
		String response = request.getHttpResponse();

		// Shouldn't get a response if all is good... if we did it's trouble
		if (!response.equals("")) {
			HP3ParHostMessage message = gson.fromJson(response, HP3ParHostMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setSuccess(false);
		}
		else {
			status.setSuccess(true);
			// Update the inventory
			try {
				HP3ParInventory.update(c, true, "CPG deleted");
			}
			catch (Exception e) {
				logger.warn("Error deleting CPG: " + e.getMessage());
			}
		}
		// Return the same reference as passed for convenience and clarity
		return status;
	}

	/**
	 * Edit an existing CPG
	 *
	 * @author Matt Day
	 * @param c
	 *            Credentials to authenticate with
	 * @param config
	 *            Configuration from which to edit a CPG
	 * @return Status of CPG deletion
	 * @throws Exception
	 *             In the case the CPG cannot be edited
	 *
	 */
	public static HP3ParRequestStatus edit(HP3ParCredentials c, EditCpgConfig config) throws Exception {

		final String cpgName = config.getCpg().split("@")[2];

		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		if (cpgName.equals(config.getNewName())) {
			status.setError("No modification");
			status.setSuccess(true);
			return status;
		}

		UcsdHttpConnection request = new UcsdHttpConnection(c, httpMethod.DELETE);

		HP3ParEditCPGParams params = new HP3ParEditCPGParams(config.getNewName());

		// Use defaults for a DELETE request
		request.setPutDefaults(gson.toJson(params));

		String uri = "/api/v1/cpgs/" + cpgName;
		request.setUri(uri);

		request.execute();
		String response = request.getHttpResponse();

		// Shouldn't get a response if all is good... if we did it's trouble
		if (!response.equals("")) {
			HP3ParHostMessage message = gson.fromJson(response, HP3ParHostMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setSuccess(false);
		}
		else {
			status.setSuccess(true);
			// Update the inventory
			try {
				HP3ParInventory.update(c, true, "CPG deleted");
			}
			catch (Exception e) {
				logger.warn("Error deleting CPG: " + e.getMessage());
			}
		}
		// Return the same reference as passed for convenience and clarity
		return status;
	}
}
