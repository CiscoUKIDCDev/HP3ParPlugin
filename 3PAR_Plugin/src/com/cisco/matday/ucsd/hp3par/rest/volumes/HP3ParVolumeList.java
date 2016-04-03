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
package com.cisco.matday.ucsd.hp3par.rest.volumes;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.TokenExpiredException;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;


public class HP3ParVolumeList {
	private VolumeResponse volume;
	
	public HP3ParVolumeList (HP3ParCredentials loginCredentials) throws HttpException, IOException, TokenExpiredException {
		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(loginCredentials);
		// Use defaults for GET method
		request.setGetDefaults();
		request.setUri(threeParRESTconstants.GET_VOLUMES_URI);
		request.execute();
		String response = request.getHttpResponse();
		//String response = new HP3ParRestWrapper(loginCredentials, threeParRESTconstants.GET_VOLUMES_URI, HttpRequestConstants.METHOD_TYPE_GET).execute();
		Gson gson = new Gson();
		this.volume = gson.fromJson(response, VolumeResponse.class);
	}

	public VolumeResponse getVolume() {
		return volume;
	}

	public void setVolume(VolumeResponse volume) {
		this.volume = volume;
	}
	
}
