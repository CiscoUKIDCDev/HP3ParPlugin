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
package com.cisco.matday.ucsd.hp3par.rest.sets.json;

/**
 * 3PAR volume/host Set list JSON enumeration
 *
 * For more info see the HP 3PAR REST API documentation
 *
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class SetRequest {
	private String name;
	private String[] setmembers;
	private String domain;
	private String comment;

	public SetRequest() {
		// leave default
	}

	public SetRequest(String name, String[] setmembers, String comment, String domain) {
		super();
		this.name = name;
		this.setmembers = setmembers;
		this.domain = ("".equals(domain)) ? null : domain;
		this.comment = ("".equals(comment)) ? null : comment;
	}

	public SetRequest(String name, String[] setmembers, String comment) {
		super();
		this.name = name;
		this.setmembers = setmembers;
		this.comment = ("".equals(comment)) ? null : comment;
	}

	public String getComment() {
		return (this.comment == null) ? "" : this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDomain() {
		return (this.domain == null) ? "" : this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getSetMembers() {
		return (this.setmembers == null) ? new String[] {} : this.setmembers;
	}

	public void setSetMembers(String[] members) {
		this.setmembers = members;
	}

}
