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
package com.cisco.matday.ucsd.hp3par.account;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.cisco.cuic.api.client.JSON;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.HP3ParToken;
import com.cisco.matday.ucsd.hp3par.rest.TokenExpiredException;
import com.cloupia.lib.connector.account.AbstractInfraAccount;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.ReportContext;

/**
 * 
 * This holds the state and credentials for connection to the 3PAR system.
 * 
 * Use it to store username, password, address etc and also to obtain a token
 * for use with the REST API. It has multiple constructors and can be used
 * almost anywhere in UCSD with a single call.
 * 
 * I suggest you use this rather than the built-in Account class to make future
 * changes a lot easier!
 * 
 * @author matt
 *
 */
public class HP3ParCredentials {

	private int tcp_port;
	private boolean https;
	private String array_address;
	private String username;
	private String password;
	private HP3ParToken token;
	static Logger logger = Logger.getLogger(HP3ParCredentials.class);

	public HP3ParCredentials(String array_address, String username, String password, boolean https, int http_port) {
		init(array_address, username, password, https, http_port);
	}

	public HP3ParCredentials(String array_address, String username, String password, boolean https) {
		init(array_address, username, password, https, HP3ParConstants.DEFAULT_PORT);
	}

	public HP3ParCredentials(String contextId) throws Exception {
		logger.info("contextId: " + contextId);
		String accountName = null;
		if (contextId != null) {
			// As the contextId returns as: "account Name;POD Name"
			accountName = contextId.split(";")[0];
		}
		if (accountName == null) {
			throw new Exception("Unable to find the account name");
		}

		PhysicalInfraAccount acc = AccountUtil.getAccountByName(accountName);
		if (acc == null) {
			throw new Exception("Unable to find the account:" + accountName);
		}
		initFromAccount(acc);
	}

	public HP3ParCredentials(ReportContext context) throws Exception {
		String contextId = context.getId();
		String accountName = null;
		if (contextId != null) {
			// As the contextId returns as: "account Name;POD Name"
			accountName = contextId.split(";")[0];
		}
		if (accountName == null) {
			throw new Exception("Account not found");
		}
		PhysicalInfraAccount acc = AccountUtil.getAccountByName(accountName);
		if (acc == null) {
			throw new Exception("Unable to find the account:" + accountName);
		}
		initFromAccount(acc);
	}

	public HP3ParCredentials(PhysicalInfraAccount acc) throws Exception {
		initFromAccount(acc);
	}

	private void initFromAccount(PhysicalInfraAccount acc) throws Exception {
		String json = acc.getCredential();
		HP3ParAccountJsonObject account = (HP3ParAccountJsonObject) JSON.jsonToJavaObject(json,
				HP3ParAccountJsonObject.class);
		String username = account.getUsername();
		String password = account.getPassword();
		String array_address = account.getArray_address();
		boolean https = account.isHttps();
		int tcp_port = account.getTcp_port();
		init(array_address, username, password, https, tcp_port);
	}

	@Deprecated
	/**
	 * @deprecated Don't call directly as it's not safe due it not releasing a
	 *             token afterwards causing major issues later
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public String getToken() throws HttpException, IOException, TokenExpiredException {
		if (this.token == null) {
			this.token = new HP3ParToken(this);
			logger.info("Got token from 3PAR: " + token.getToken());
		}
		logger.info("Returning token: " + token.getToken());
		return token.getToken();
	}

	public void setToken(HP3ParToken token) {
		this.token = token;
	}

	public String getProtocol() {
		return (https) ? "https" : "http";
	}

	private void init(String array_address, String username, String password, boolean https, int http_port) {
		this.array_address = array_address;
		this.username = username;
		this.password = password;
		this.https = https;
		this.tcp_port = http_port;
	}

	public int getTcp_port() {
		return tcp_port;
	}

	public void setTcp_port(int tcp_port) {
		this.tcp_port = tcp_port;
	}

	public boolean isHttps() {
		return https;
	}

	public void setHttps(boolean https) {
		this.https = https;
	}

	public String getArray_address() {
		return array_address;
	}

	public void setArray_address(String array_address) {
		this.array_address = array_address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * To get the object of HP3Par by passing the AccountName.
	 * 
	 * @param accountName
	 * @return
	 * @throws Exception
	 */
	public static HP3ParAccount getInternalCredential(String accountName) throws Exception {
		PhysicalInfraAccount acc = AccountUtil.getAccountByName(accountName);
		String json = acc.getCredential();
		AbstractInfraAccount specificAcc = (AbstractInfraAccount) JSON.jsonToJavaObject(json, HP3ParAccount.class);
		specificAcc.setAccount(acc);

		return (HP3ParAccount) specificAcc;

	}

}
