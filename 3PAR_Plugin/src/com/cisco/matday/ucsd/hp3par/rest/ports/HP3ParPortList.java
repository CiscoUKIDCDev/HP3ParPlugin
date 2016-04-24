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
package com.cisco.matday.ucsd.hp3par.rest.ports;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.ports.json.PortResponse;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

/**
 * Gets a list of every port
 *
 * @author Matt Day
 *
 */
public class HP3ParPortList {
	private final PortResponse ports;
	private final String json;

	/**
	 * @param loginCredentials
	 *            Login credentials for the 3PAR system you wish to access
	 * @throws HttpException
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 *             if the token is invalid or cannot be obtained
	 */
	public HP3ParPortList(HP3ParCredentials loginCredentials)
			throws HttpException, IOException, InvalidHP3ParTokenException {
		final UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(loginCredentials);
		// Use defaults for GET method
		request.setGetDefaults();
		request.setUri(threeParRESTconstants.GET_PORTS_URI);
		request.execute();
		this.json = request.getHttpResponse();
		final Gson gson = new Gson();
		this.ports = gson.fromJson(this.json, PortResponse.class);
	}

	/**
	 * Initialise from JSON
	 *
	 * @param json
	 */
	public HP3ParPortList(String json) {
		this.json = json;
		final Gson gson = new Gson();
		this.ports = gson.fromJson(this.json, PortResponse.class);
	}

	/**
	 * @return ports information
	 */
	public PortResponse getPorts() {
		return this.ports;
	}

	/**
	 * @return in JSON encoded form
	 */
	public String toJson() {
		return this.json;
	}

}
