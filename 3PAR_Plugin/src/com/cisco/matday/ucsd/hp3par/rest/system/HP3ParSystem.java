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
package com.cisco.matday.ucsd.hp3par.rest.system;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.system.json.SystemResponse;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

/**
 * Gets system information from a 3PAR array and stores it as an object
 *
 * @author Matt Day
 *
 */
public class HP3ParSystem {
	//
	private SystemResponse systemResponse;
	private String json;

	/**
	 * @param loginCredentials
	 *            Login credentials for the 3PAR system you wish to access
	 * @throws HttpException
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 *             if the token is invalid or cannot be obtained
	 */
	public HP3ParSystem(HP3ParCredentials loginCredentials)
			throws HttpException, IOException, InvalidHP3ParTokenException {

		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(loginCredentials);
		// Use defaults for a GET request
		request.setGetDefaults();
		request.setUri(threeParRESTconstants.GET_SYSTEM_URI);

		request.execute();
		this.json = request.getHttpResponse();

		Gson gson = new Gson();
		this.systemResponse = gson.fromJson(this.json, SystemResponse.class);
		this.systemResponse.setJson(this.json);
	}

	/**
	 * @return information about the 3PAR system
	 */
	public SystemResponse getSystem() {
		return this.systemResponse;
	}

	/**
	 * @return in JSON encoded form
	 */
	public String toJson() {
		return this.json;
	}

}
