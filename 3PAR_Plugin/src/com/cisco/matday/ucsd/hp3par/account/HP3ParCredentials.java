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

import org.apache.log4j.Logger;

import com.cisco.cuic.api.client.JSON;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
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
	private String accountName = null;

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
		init(array_address, username, password, https, http_port);
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
		init(array_address, username, password, true, HP3ParConstants.DEFAULT_PORT);
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
	 * @throws Exception
	 *             If the account isn't found
	 */
	public HP3ParCredentials(String accountName) throws Exception {
		this.accountName = accountName;
		PhysicalInfraAccount acc = AccountUtil.getAccountByName(accountName);
		if (acc == null) {
			throw new Exception("Unable to find the account:" + accountName);
		}
		initFromAccount(acc);
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
	 * @throws Exception
	 *             If the account isn't found
	 */
	public HP3ParCredentials(ReportContext context) throws Exception {
		String contextId = context.getId();
		this.accountName = null;
		if (contextId != null) {
			// As the contextId returns as: "account Name;POD Name"
			this.accountName = contextId.split(";")[0];
		}
		if (this.accountName == null) {
			throw new Exception("Account not found");
		}
		PhysicalInfraAccount acc = AccountUtil.getAccountByName(this.accountName);
		if (acc == null) {
			throw new Exception("Unable to find the account:" + accountName);
		}
		initFromAccount(acc);
	}

	/**
	 * Get the user-specified account name
	 * 
	 * @return Accout name
	 */
	public String getAccountName() {
		if (accountName == null) {
			accountName = "-";
		}
		return accountName;
	}

	private void initFromAccount(PhysicalInfraAccount acc) throws Exception {
		String json = acc.getCredential();
		HP3ParAccountJsonObject account = (HP3ParAccountJsonObject) JSON.jsonToJavaObject(json,
				HP3ParAccountJsonObject.class);
		this.accountName = acc.getAccountName();
		String username = account.getUsername();
		String password = account.getPassword();
		String array_address = account.getArray_address();
		boolean https = account.isHttps();
		int tcp_port = account.getTcp_port();
		init(array_address, username, password, https, tcp_port);
	}

	/**
	 * Get the current protocol in use - http or https
	 * 
	 * @return Current protocol (http or https)
	 */
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

	/**
	 * Get TCP port in use (default 8080)
	 * 
	 * @return tcp port
	 */
	public int getTcp_port() {
		return tcp_port;
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
		return https;
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
		return array_address;
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
		return username;
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
		return password;
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
	 * Returns the internal HP3Par account information based on the account name
	 * 
	 * @param accountName
	 * @return UCS Director account information
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
