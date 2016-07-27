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
package com.cisco.matday.ucsd.hp3par.rest.volumes;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.exceptions.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.UcsdHttpConnection;
import com.cisco.matday.ucsd.hp3par.rest.UcsdHttpConnection.httpMethod;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

/**
 * Gets a list of every volume in the array
 *
 * @author Matt Day
 *
 */
public class HP3ParVolumeList {
	private final VolumeResponse volume;
	private final String json;

	/**
	 * @param loginCredentials
	 *            Login credentials for the 3PAR system you wish to access
	 * @throws HttpException
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 *             if the token is invalid or cannot be obtained
	 */
	public HP3ParVolumeList(HP3ParCredentials loginCredentials)
			throws HttpException, IOException, InvalidHP3ParTokenException {
		final UcsdHttpConnection request = new UcsdHttpConnection(loginCredentials, httpMethod.GET);
		// Use defaults for GET method
		request.setGetDefaults();
		request.setUri(threeParRESTconstants.GET_VOLUMES_URI);
		request.execute();
		this.json = request.getHttpResponse();
		// String response = new HP3ParRestWrapper(loginCredentials,
		// threeParRESTconstants.GET_VOLUMES_URI,
		// HttpRequestConstants.METHOD_TYPE_GET).execute();
		final Gson gson = new Gson();
		this.volume = gson.fromJson(this.json, VolumeResponse.class);
		// this.volume.setJson(this.json);
	}

	/**
	 * Initialise from JSON
	 *
	 * @param json
	 */
	public HP3ParVolumeList(String json) {
		this.json = json;
		final Gson gson = new Gson();
		this.volume = gson.fromJson(this.json, VolumeResponse.class);
	}

	/**
	 * @return Volumes information
	 */
	public VolumeResponse getVolume() {
		return this.volume;
	}

	/**
	 * @return in JSON encoded form
	 */
	public String toJson() {
		return this.json;
	}

}
