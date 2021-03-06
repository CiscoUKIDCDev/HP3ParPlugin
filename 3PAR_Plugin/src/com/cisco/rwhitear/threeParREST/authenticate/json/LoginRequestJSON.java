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
package com.cisco.rwhitear.threeParREST.authenticate.json;

import com.google.gson.Gson;

// Supressing javadoc warnings as it's not my code to document
@SuppressWarnings("javadoc")
public class LoginRequestJSON {

	private String user = "";

	private String password = "";

	public LoginRequestJSON() {

	}

	public LoginRequestJSON(String user, String password) {
		this.user = user;
		this.password = password;
	}

	public String convertToJSON() {

		UserCredentials userCredentialsMap = new UserCredentials();

		userCredentialsMap.setUsername(this.user);
		userCredentialsMap.setPassword(this.password);

		UserCredentials jsonWrapper = new UserCredentials();

		jsonWrapper.setUsername(this.user);
		jsonWrapper.setPassword(this.password);

		Gson gson = new Gson();

		return gson.toJson(jsonWrapper);

	}

	public String getUsername() {
		return this.user;
	}

	public void setUsername(String user) {
		this.user = user;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

class UserCredentials {

	private String user;
	private String password;

	public String getUsername() {
		return this.user;
	}

	public void setUsername(String user) {
		this.user = user;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
