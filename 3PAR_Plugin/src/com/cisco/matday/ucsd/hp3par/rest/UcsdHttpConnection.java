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
package com.cisco.matday.ucsd.hp3par.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.exceptions.InvalidHP3ParTokenException;
import com.cisco.rwhitear.threeParREST.constants.threeParRESTconstants;

/**
 * This is an ugly wrapper to make the old interface via the http 3.x client to
 * the 4.2.2 client.
 * <p>
 * Don't use this, look at another UCSD project like the Spark one to see how
 * this can be much better... It was done this way to avoid major refactoring
 * and has plenty of caveats (for example, if things aren't initialised in the
 * right order you'll get a null pointer exception!
 *
 * @author matt
 *
 */

public class UcsdHttpConnection {

	private static Logger logger = Logger.getLogger(UcsdHttpConnection.class);

	private HttpRequestBase request;

	private String response = null;
	private DefaultHttpClient httpclient = new DefaultHttpClient();

	// Assume GET by default
	private httpMethod method = httpMethod.GET;

	// Default to https on 443
	private String protocol = "https";
	private int port = 443;
	private String server;

	// By default do not allow untrusted certificates
	private boolean allowUntrustedCertificates = false;

	boolean useToken = false;
	HP3ParCredentials credentials = null;

	private String path = "/";

	/**
	 * Enum type for http method
	 */
	public enum httpMethod {
		/**
		 * http POST method
		 */
		POST,
		/**
		 * http GET method
		 */
		GET,
		/**
		 * http DELETE method
		 */
		DELETE,
		/**
		 * http PUT method
		 */
		PUT
	}

	/**
	 * Enum type for protocols
	 */
	public enum httpProtocol {
		/**
		 * http
		 */
		HTTP,
		/**
		 * https
		 */
		HTTPS,
	}

	/**
	 * Only initialise from credentials to allow token later
	 *
	 * @param credentials
	 */
	@Deprecated
	public UcsdHttpConnection(HP3ParCredentials credentials) {
		this.credentials = credentials;
		// Allow untrusted credentials by default:
		this.allowUntrustedCertificates = true;
		this.server = credentials.getArray_address();
		this.port = credentials.getTcp_port();
		this.protocol = (credentials.isHttps()) ? "https" : "http";
	}

	/**
	 * Only initialise from credentials to allow token later
	 *
	 * @param credentials
	 * @param method
	 *            Http Method to use
	 */
	public UcsdHttpConnection(HP3ParCredentials credentials, httpMethod method) {
		this.credentials = credentials;
		// Allow untrusted credentials by default:
		this.allowUntrustedCertificates = true;
		this.setMethod(method);
		this.server = credentials.getArray_address();
		this.port = credentials.getTcp_port();
		this.protocol = (credentials.isHttps()) ? "https" : "http";
		this.method = method;
	}

	/**
	 * Set the URI and method to use
	 *
	 * @param path
	 *            path (e.g. /api.ciscospark.com/v1/rooms)
	 */
	public void setUri(String path) {
		this.path = path;
		try {
			this.request.setURI(new URI(path));
		}
		catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set the http method to use (i.e. GET/POST/DELETE/PUT)
	 *
	 * @param method
	 *            Method to use
	 */
	public void setMethod(httpMethod method) {
		this.method = method;
		switch (this.method) {
		case GET:
			this.request = new HttpGet(this.path);
			break;
		case PUT:
			this.request = new HttpPut(this.path);
			break;
		case POST:
			this.request = new HttpPost(this.path);
			break;
		case DELETE:
			this.request = new HttpDelete(this.path);
			break;
		default:
			logger.error("Unknown method type " + this.method);
			return;
		}
	}

	/**
	 * Set if the token should be used on the request
	 *
	 * @param useToken
	 */
	public void setToken(boolean useToken) {
		this.useToken = useToken;
	}

	/**
	 * Sets HP3Par defaults for a GET method
	 */
	public void setGetDefaults() {
		this.setMethod(httpMethod.GET);
		this.setJsonContentType();
		this.setToken(true);
	}

	/**
	 * Sets HP3Par defaults for a POST method
	 *
	 * @param body
	 *            Any body text to send with the request
	 */
	public void setPostDefaults(String body) {
		this.setMethod(httpMethod.POST);
		this.setJsonContentType();
		this.setBodyJson(body);
		this.setToken(true);
	}

	/**
	 * Sets a JSON body parameter
	 *
	 * @param body
	 */
	public void setBodyJson(String body) {
		this.setBody(body, ContentType.APPLICATION_JSON);
	}

	/**
	 * Add a JSON content header
	 */
	public void setJsonContentType() {
		this.addRequestHeaders("Accept", "application/json");
	}

	/**
	 * Set an http header
	 *
	 * @param key
	 *            Key (e.g. "Content-type")
	 * @param value
	 *            Value (e.g. "text/plain")
	 */
	public void addRequestHeaders(String key, String value) {
		this.request.addHeader(key, value);
	}

	/**
	 * Sets a body parameter
	 *
	 * @param body
	 *            message body to add
	 * @param contentType
	 *            Content type
	 */
	public void setBody(String body, ContentType contentType) {
		System.out.println(body);
		try {
			// Attempt to cast it to an EntityEnclosingMethod which supports
			// body elements (i.e. POST, PUT methods) and set the body
			((HttpEntityEnclosingRequestBase) this.request).setEntity(new StringEntity(body, contentType));
		}
		catch (Exception e) {
			logger.error("Cannot add http body to request: " + e.getMessage());
		}
	}

	/**
	 * Set HP3Par defaults for a DELETE method
	 */
	public void setDeleteDefaults() {
		this.setMethod(httpMethod.DELETE);
		this.setJsonContentType();
		this.setToken(true);
	}

	/**
	 * Set HP3Par defaults for a PUT method
	 *
	 * @param body
	 *            Any body text to send with the request
	 */
	public void setPutDefaults(String body) {
		this.setMethod(httpMethod.PUT);
		this.setJsonContentType();
		this.setBodyJson(body);
		this.setToken(true);
	}

	/**
	 * Override parent execute to include token if set
	 *
	 * @throws IOException
	 * @throws HttpException
	 */
	@SuppressWarnings("deprecation")
	public void execute() throws HttpException, IOException {
		// If SSL verification is disabled, use own socket factory
		if (this.allowUntrustedCertificates) {
			try {
				// Create a new socket factory and set it to always say yes
				SSLSocketFactory socketFactory = new SSLSocketFactory((chain, authType) -> true);

				// This method is deprecated, but the workaround is to upgrade
				// to 4.3 which isn't included in UCSD as of 5.5
				socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				this.httpclient.getConnectionManager().getSchemeRegistry()
						.register(new Scheme("https", 443, socketFactory));
			}
			catch (Exception e) {
				logger.warn("Failed to configure SSL library: " + e.getMessage());
			}
		}

		HP3ParToken token = null;
		if ((this.useToken) && (this.credentials != null)) {
			try {
				token = new HP3ParToken(this.credentials);
				logger.debug("Requested token: " + token.getToken());
				this.addRequestHeaders(threeParRESTconstants.SESSION_KEY_HEADER, token.getToken());
			}
			catch (final InvalidHP3ParTokenException e) {
				logger.error("Could not add token to http request (token expired or invalid credentials)!");
				e.printStackTrace();
			}
		}
		try {
			System.out.println("Server: " + this.server + " port: " + this.port + " protocol: " + this.protocol);
			System.out.println("URL: " + this.request.getURI().toString());
			HttpHost target = new HttpHost(this.server, this.port, this.protocol);
			HttpResponse rsp = this.httpclient.execute(target, this.request);
			if (rsp.getEntity() != null) {
				this.response = EntityUtils.toString(rsp.getEntity());
			}
		}
		finally {
			this.request.releaseConnection();
		}
		// Now clean up the token:
		if ((this.useToken) && (token != null)) {
			try {
				logger.debug("Released token: " + token.getToken());
				token.release();
			}
			catch (final InvalidHP3ParTokenException e) {
				logger.debug("Could not release token");
				e.printStackTrace();
			}
		}

	}

	/**
	 * Return http response as a String
	 *
	 * @return HTTP response as a String
	 */
	public String getHttpResponse() {
		return this.response;
	}

}
