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

/**
 * REST object representation of the HP3PAR CPG list. See the HP 3PAR REST
 * documentation for detail on these methods and values
 * 
 * @author Matt Day
 *
 */
public class CPGResponseMembers {
	private int id;
	private String uuid;
	private String name;
	private int numFPVVs;
	private int numTPVVs;
	private CPGResponseSpace UsrUsage;
	private CPGResponseSpace SAUsage;
	private CPGResponseSpace SDUsage;
	private int state;

	@SuppressWarnings("javadoc")
	public int getId() {
		return id;
	}

	@SuppressWarnings("javadoc")
	public void setId(int id) {
		this.id = id;
	}

	@SuppressWarnings("javadoc")
	public String getUuid() {
		return uuid;
	}

	@SuppressWarnings("javadoc")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@SuppressWarnings("javadoc")
	public String getName() {
		return name;
	}

	@SuppressWarnings("javadoc")
	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("javadoc")
	public int getNumFPVVs() {
		return numFPVVs;
	}

	@SuppressWarnings("javadoc")
	public void setNumFPVVs(int numFPVVs) {
		this.numFPVVs = numFPVVs;
	}

	@SuppressWarnings("javadoc")
	public int getNumTPVVs() {
		return numTPVVs;
	}

	@SuppressWarnings("javadoc")
	public void setNumTPVVs(int numTPVVs) {
		this.numTPVVs = numTPVVs;
	}

	@SuppressWarnings("javadoc")
	public CPGResponseSpace getUsrUsage() {
		return UsrUsage;
	}

	@SuppressWarnings("javadoc")
	public void setUsrUsage(CPGResponseSpace usrUsage) {
		UsrUsage = usrUsage;
	}

	@SuppressWarnings("javadoc")
	public CPGResponseSpace getSAUsage() {
		return SAUsage;
	}

	@SuppressWarnings("javadoc")
	public void setSAUsage(CPGResponseSpace sAUsage) {
		SAUsage = sAUsage;
	}

	@SuppressWarnings("javadoc")
	public CPGResponseSpace getSDUsage() {
		return SDUsage;
	}

	@SuppressWarnings("javadoc")
	public void setSDUsage(CPGResponseSpace sDUsage) {
		SDUsage = sDUsage;
	}

	@SuppressWarnings("javadoc")
	public int getState() {
		return state;
	}

	@SuppressWarnings("javadoc")
	public void setState(int state) {
		this.state = state;
	}

}
