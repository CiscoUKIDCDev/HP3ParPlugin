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
package com.cisco.matday.ucsd.hp3par.rest.hosts.json;

import java.util.List;

/**
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class HostResponseMember {

	private int id;
	private String name;
	private List<HostResponseFCPaths> FCPaths;

	private List<HostResponseiSCSIPaths> iSCSIPaths;
	private int persona;
	private boolean initiatorChapEnabled;
	private boolean targetChapEnabled;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<HostResponseFCPaths> getFCPaths() {
		return this.FCPaths;
	}

	public void setFCPaths(List<HostResponseFCPaths> fCPaths) {
		this.FCPaths = fCPaths;
	}

	public List<HostResponseiSCSIPaths> getiSCSIPaths() {
		return this.iSCSIPaths;
	}

	public void setiSCSIPaths(List<HostResponseiSCSIPaths> iSCSIPaths) {
		this.iSCSIPaths = iSCSIPaths;
	}

	public int getPersona() {
		return this.persona;
	}

	public void setPersona(int persona) {
		this.persona = persona;
	}

	public boolean isInitiatorChapEnabled() {
		return this.initiatorChapEnabled;
	}

	public void setInitiatorChapEnabled(boolean initiatorChapEnabled) {
		this.initiatorChapEnabled = initiatorChapEnabled;
	}

	public boolean isTargetChapEnabled() {
		return this.targetChapEnabled;
	}

	public void setTargetChapEnabled(boolean targetChapEnabled) {
		this.targetChapEnabled = targetChapEnabled;
	}

}
