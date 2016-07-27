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
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;
import com.google.gson.Gson;

/**
 * Gets information about a single volume
 *
 * @author Matt Day
 *
 */
public class HP3ParVolumeInfo {

	private VolumeResponseMember member;

	/**
	 * Get information about a specific volume
	 *
	 * @param loginCredentials
	 *            Account credentials for this array
	 * @param volName
	 *            Name of the volume to query
	 * @throws HttpException
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 *             If credentials are invalid
	 */
	@Deprecated
	public HP3ParVolumeInfo(HP3ParCredentials loginCredentials, String volName)
			throws HttpException, IOException, InvalidHP3ParTokenException {
		UcsdHttpConnection request = new UcsdHttpConnection(loginCredentials, httpMethod.GET);
		// Use defaults for GET method
		request.setGetDefaults();
		// Volume info uri:
		String uri = "/api/v1/volumes/" + volName;
		request.setUri(uri);
		request.execute();
		String response = request.getHttpResponse();
		Gson gson = new Gson();
		this.member = gson.fromJson(response, VolumeResponseMember.class);
	}

	/**
	 * @return Information about this volume
	 */
	public VolumeResponseMember getMember() {
		return this.member;
	}

}
