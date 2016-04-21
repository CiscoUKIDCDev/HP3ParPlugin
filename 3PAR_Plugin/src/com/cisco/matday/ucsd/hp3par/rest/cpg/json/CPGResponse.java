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
package com.cisco.matday.ucsd.hp3par.rest.cpg.json;

import java.util.List;

/**
 * REST object representation of the HP3PAR CPG list. See the HP 3PAR REST
 * documentation for detail on these methods and values
 *
 * @author Matt Day
 *
 */

public class CPGResponse {
	private int total;

	private List<CPGResponseMember> members;

	// private String json;

	/**
	 * @return the json
	 */
	/*
	 * public String getJson() { return this.json; }
	 */

	/**
	 * @param json
	 *            the json to set
	 *//*
		 * public void setJson(String json) { this.json = json; }
		 */

	@SuppressWarnings("javadoc")
	public int getTotal() {
		return this.total;
	}

	@SuppressWarnings("javadoc")
	public void setTotal(int total) {
		this.total = total;
	}

	@SuppressWarnings("javadoc")
	public List<CPGResponseMember> getMembers() {
		return this.members;
	}

	@SuppressWarnings("javadoc")
	public void setMembers(List<CPGResponseMember> members) {
		this.members = members;
	}
}
