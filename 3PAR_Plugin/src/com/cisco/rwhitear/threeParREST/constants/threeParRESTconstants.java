/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Russ Whitear
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
package com.cisco.rwhitear.threeParREST.constants;

// Ignoring javadoc
@SuppressWarnings("javadoc")
public class threeParRESTconstants {

	public static final String SESSION_KEY_HEADER = "X-HP3PAR-WSAPI-SessionKey";

	public static final String GET_SESSION_TOKEN_URI = "/api/v1/credentials";
	public static final String GET_VOLUMES_URI = "/api/v1/volumes";
	public static final String GET_SYSTEM_URI = "/api/v1/system";
	public static final String GET_CPG_URI = "/api/v1/cpgs";
	public static final String GET_HOSTS_URI = "/api/v1/hosts";
	public static final String GET_VLUNS_URI = "/api/v1/vluns";
	public static final String GET_PORTS_URI = "/api/v1/ports";

	public static final String GET_HOSTSETS_URI = "/api/v1/hostsets";

	public static final String GET_VOLUMESETS_URI = "/api/v1/volumesets";

}
