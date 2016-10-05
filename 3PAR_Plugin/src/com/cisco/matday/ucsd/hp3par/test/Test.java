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

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.UcsdHttpConnection;
import com.cisco.matday.ucsd.hp3par.rest.UcsdHttpConnection.httpMethod;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;

@SuppressWarnings("javadoc")
public class Test {
	@SuppressWarnings({
			"deprecation"
	})
	public static void main(String[] args) throws HttpException, IOException {
		HP3ParCredentials c = new HP3ParCredentials("10.51.8.210", "3paradm", "3pardata");
		c.setValidateCert(false);
		final UcsdHttpConnection request = new UcsdHttpConnection(c, httpMethod.GET);
		// Use defaults for GET method
		request.setGetDefaults();
		request.setUri(threeParRESTconstants.GET_VOLUMES_URI);
		request.execute();
		String json = request.getHttpResponse();
		System.out.println(json);
	}

}
