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
package com.cisco.matday.ucsd.hp3par.test;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.Header;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.UcsdHttpConnection;
import com.cisco.matday.ucsd.hp3par.rest.UcsdHttpConnection.httpMethod;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.vluns.rest.HP3ParVlunParams;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeMessage;
import com.cisco.matday.ucsd.hp3par.tasks.vluns.CreateVlunConfig;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;
import com.google.gson.Gson;

@SuppressWarnings("javadoc")
public class Test {
	@SuppressWarnings({
			"deprecation"
	})
	public static void main(String[] args) throws HttpException, IOException {
		HP3ParCredentials c = new HP3ParCredentials("10.51.8.210", "3paradm", "3pardata");
		c.setValidateCert(false);
		CreateVlunConfig config = new CreateVlunConfig();
		config.setVolume("0@volume@Hyper-V-Data-Store");
		config.setHost("0@host@ESXi-D");
		config.setAutoLun(true);
		// Get the volume name, it's in the format:
		// id@account@name
		String volName = config.getVolume().split("@")[2];

		final String volType = config.getVolume().split("@")[1];

		String hostName = config.getHost().split("@")[2];

		final String hostType = config.getHost().split("@")[1];

		if (hostType.equals("hostset")) {
			hostName = "set:" + hostName;
		}

		if (volType.equals("volumeset")) {
			volName = "set:" + volName;
		}

		// Build vlun information object if we need an auto LUN or not:
		HP3ParVlunParams params = null;
		if (config.useAutoLun()) {
			params = new HP3ParVlunParams(volName, hostName);
		}
		else {
			params = new HP3ParVlunParams(volName, hostName, config.getLun());
		}

		Gson gson = new Gson();
		HP3ParRequestStatus status = new HP3ParRequestStatus();

		UcsdHttpConnection request = new UcsdHttpConnection(c, httpMethod.POST);

		// Use defaults for a POST request
		request.setPostDefaults(gson.toJson(params));
		request.setUri(threeParRESTconstants.GET_VLUNS_URI);

		request.execute();
		String response = request.getHttpResponse();
		System.out.println(request.getStatusCode());

		// Shouldn't get a response if all is good... if we did it's trouble
		if (request.getStatusCode() == 201) {
			status.setSuccess(true);
			for (Header h : request.getHeaders()) {
				if (h.getName().equals("Location")) {
					// LUN ID gets returned as a String, e.g.:
					// /api/v1/vluns/VolumeName,LunID,HostName
					try {
						int id = Integer.parseInt(h.getValue().split(",")[1]);
						config.setLun(id);
						System.out.println("LUN: " + id);
					}
					catch (Exception e) {
						System.out.println("Error!");
					}
				}
			}
			System.out.println("Created OK!");
		}
		else {
			HP3ParVolumeMessage message = gson.fromJson(response, HP3ParVolumeMessage.class);
			status.setError("Error code: " + message.getCode() + ": " + message.getDesc());
			status.setSuccess(false);
		}
	}

}
