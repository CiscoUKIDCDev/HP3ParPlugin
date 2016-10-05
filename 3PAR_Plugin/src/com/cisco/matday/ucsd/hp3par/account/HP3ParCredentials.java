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
package com.cisco.matday.ucsd.hp3par.account;

import org.apache.log4j.Logger;

import com.cisco.cuic.api.client.JSON;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParAccountException;
import com.cloupia.lib.connector.account.AbstractInfraAccount;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.ReportContext;

/**
 *
 * This holds the state and credentials for connection to the 3PAR system.
 * <p>
 * Use it to store username, password, address etc and also to obtain a token
 * for use with the REST API. It has multiple constructors and can be used
 * almost anywhere in UCSD with a single call.
 * <p>
 * I suggest you use this rather than the built-in Account class to make future
 * changes a lot easier!
 *
 * @author Matt Day
 *
 */
public class HP3ParCredentials {

	private int tcp_port;
	private boolean https;
	private String array_address;
	private String username;
	private String password;
	// This MUST be intialised (thus is final):
	private final String accountName;
	private boolean validateCert = false;

	static Logger logger = Logger.getLogger(HP3ParCredentials.class);

	/**
	 * Used for test cases only, you should not use this in UCS Director
	 *
	 * @deprecated You shouldn't call this outside of test applications
	 * @param array_address
	 *            IP address of the array
	 * @param username
	 *            Username to log in as
	 * @param password
	 *            Password to log in as
	 * @param https
	 *            Use the https protocol
	 * @param http_port
	 *            What port to use
	 */
	@Deprecated
	public HP3ParCredentials(String array_address, String username, String password, boolean https, int http_port) {
		this.accountName = "internal";
		this.init(array_address, username, password, https, http_port);
	}

	/**
	 * Used for test cases only, you should not use this in UCS Director. This
	 * will use a default port of 8080 and https.
	 *
	 * @deprecated You shouldn't call this outside of test applications
	 * @param array_address
	 *            IP address of the array
	 * @param username
	 *            Username to log in as
	 * @param password
	 *            Password to log in as
	 */
	@Deprecated
	public HP3ParCredentials(String array_address, String username, String password) {
		this.accountName = "internal";
		this.init(array_address, username, password, true, HP3ParConstants.DEFAULT_PORT);
		this.validateCert = true;
	}

	/**
	 * Get credentials from the account name. The account name is specified by
	 * the user in UCS Director.
	 * <p>
	 * For example, they may have two accounts (3PAR-1 and 3PAR-2) - this will
	 * get the credentials for the right one.
	 *
	 * @param accountName
	 *            Account name specified by user
	 * @throws HP3ParAccountException
	 *             If the account isn't found
	 */
	public HP3ParCredentials(String accountName) throws HP3ParAccountException {
		this.accountName = accountName;
		try {
			PhysicalInfraAccount acc = AccountUtil.getAccountByName(this.accountName);
			if (acc == null) {
				throw new HP3ParAccountException("Unable to find the account:" + this.accountName);
			}
			this.initFromAccount(acc);
		}
		catch (@SuppressWarnings("unused") Exception e) {
			throw new HP3ParAccountException("Unable to find the account:" + this.accountName);
		}
	}

	/**
	 * Get credentials from the current context. This is typically used in the
	 * converged view where account name is known.
	 * <p>
	 * For example, if a user has two accounts (3PAR-1 and 3PAR-2) and is
	 * currently looking at a table for 3PAR-1 this will return those
	 * credentials
	 *
	 * @param context
	 *            Current context
	 * @throws HP3ParAccountException
	 *             If the account isn't found
	 */
	public HP3ParCredentials(ReportContext context) throws HP3ParAccountException {
		final String contextId = context.getId();
		// If the context ID isn't null, use it to get the account name:
		this.accountName = (contextId == null) ? null : contextId.split(";")[0];

		if (this.accountName == null) {
			throw new HP3ParAccountException("Account not found");
		}
		try {
			PhysicalInfraAccount acc = AccountUtil.getAccountByName(this.accountName);
			if (acc == null) {
				throw new HP3ParAccountException("Unable to find the account:" + this.accountName);
			}
			this.initFromAccount(acc);
		}
		catch (@SuppressWarnings("unused") Exception e) {
			throw new HP3ParAccountException("Unable to find the account:" + this.accountName);
		}

	}

	/**
	 * Get the user-specified account name
	 *
	 * @return Accout name
	 */
	public String getAccountName() {
		return this.accountName;
	}

	// Do the needed to get the account details from a PhysicalInfraAccount
	// object
	private void initFromAccount(PhysicalInfraAccount acc) throws Exception {
		String json = acc.getCredential();
		HP3ParAccountJsonObject account = (HP3ParAccountJsonObject) JSON.jsonToJavaObject(json,
				HP3ParAccountJsonObject.class);
		String initUsername = account.getUsername();
		String initPassword = account.getPassword();
		String initArrayAddress = account.getArray_address();
		this.validateCert = account.isValidateCert();
		boolean initHttps = account.isHttps();
		int initTcpPort = account.getTcp_port();
		this.init(initArrayAddress, initUsername, initPassword, initHttps, initTcpPort);
	}

	/**
	 * @return if the server certificate should be validated or not
	 */
	public boolean validateCert() {
		return this.validateCert;
	}

	/**
	 * Configure whether to validate the server certificate or not
	 * 
	 * @param validateCert
	 */
	public void setValidateCert(boolean validateCert) {
		this.validateCert = validateCert;
	}

	/**
	 * Get the current protocol in use - http or https
	 *
	 * @return Current protocol (http or https)
	 */
	public String getProtocol() {
		return (this.https) ? "https" : "http";
	}

	// Used for testing purposes only
	private void init(String initArrayAddress, String initUsername, String initPassword, boolean initHttps,
			int http_port) {
		this.array_address = initArrayAddress;
		this.username = initUsername;
		this.password = initPassword;
		this.https = initHttps;
		this.tcp_port = http_port;
	}

	/**
	 * Get TCP port in use (default 8080)
	 *
	 * @return tcp port
	 */
	public int getTcp_port() {
		return this.tcp_port;
	}

	/**
	 * Set TCP port in use (default 8080)
	 *
	 * @param tcp_port
	 *            tcp port
	 */
	public void setTcp_port(int tcp_port) {
		this.tcp_port = tcp_port;
	}

	/**
	 * Is this using secure http (https)?
	 *
	 * @return true if it's https
	 */
	public boolean isHttps() {
		return this.https;
	}

	/**
	 * Set access method
	 *
	 * @param https
	 *            True if https, false if http
	 */
	public void setHttps(boolean https) {
		this.https = https;
	}

	/**
	 * Get IP address or hostname of 3PAR array
	 *
	 * @return IP address or hostname of 3PAR array
	 */
	public String getArray_address() {
		return this.array_address;
	}

	/**
	 * Set IP address or hostname of 3PAR array
	 *
	 * @param array_address
	 *            IP address or hostname of 3PAR array
	 */
	public void setArray_address(String array_address) {
		this.array_address = array_address;
	}

	/**
	 * Username accessing array
	 *
	 * @return Username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Username accessing array
	 *
	 * @param username
	 *            Username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Password accessing array
	 *
	 * @return Password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Password accessing array
	 *
	 * @param password
	 *            Password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns the internal HP3Par account information based on the account
	 * name. Used for specific areas such as logging, connection testing etc.
	 *
	 * @param accountName
	 * @return UCS Director account information
	 * @throws Exception
	 */
	public static HP3ParAccountDBStore getInternalCredential(String accountName) throws Exception {
		PhysicalInfraAccount acc = AccountUtil.getAccountByName(accountName);
		String json = acc.getCredential();
		AbstractInfraAccount specificAcc = (AbstractInfraAccount) JSON.jsonToJavaObject(json,
				HP3ParAccountDBStore.class);
		specificAcc.setAccount(acc);

		return (HP3ParAccountDBStore) specificAcc;

	}

}
