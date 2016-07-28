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

/**
 * 3PAR Account information as a REST object
 *
 * See the HP 3PAR documentation for more information
 *
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class HP3ParAccountJsonObject {

	private String account;
	private String array_address;
	private String dnDetachedState;
	private String isCredentialPolicy;
	private String username;
	private String password;
	private boolean validateCert;
	private int tcp_port;
	private boolean https;

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the validateCert
	 */
	public boolean isValidateCert() {
		return this.validateCert;
	}

	/**
	 * @param validateCert
	 *            the validateCert to set
	 */
	public void setValidateCert(boolean validateCert) {
		this.validateCert = validateCert;
	}

	public String getArray_address() {
		return this.array_address;
	}

	public void setArray_address(String array_address) {
		this.array_address = array_address;
	}

	public String getDnDetachedState() {
		return this.dnDetachedState;
	}

	public void setDnDetachedState(String dnDetachedState) {
		this.dnDetachedState = dnDetachedState;
	}

	public String getIsCredentialPolicy() {
		return this.isCredentialPolicy;
	}

	public void setIsCredentialPolicy(String isCredentialPolicy) {
		this.isCredentialPolicy = isCredentialPolicy;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTcp_port() {
		return this.tcp_port;
	}

	public void setTcp_port(int tcp_port) {
		this.tcp_port = tcp_port;
	}

	public boolean isHttps() {
		return this.https;
	}

	public void setHttps(boolean https) {
		this.https = https;
	}

}
