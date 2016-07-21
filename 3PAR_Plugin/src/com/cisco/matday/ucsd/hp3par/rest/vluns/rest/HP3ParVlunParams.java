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
package com.cisco.matday.ucsd.hp3par.rest.vluns.rest;

import com.cisco.matday.ucsd.hp3par.rest.ports.json.PortResponsePos;

/**
 * Enumerates HP3Par VLUN parameters
 *
 * See the HP 3PAR REST API for more info
 *
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class HP3ParVlunParams {
	private String volumeName;
	private int lun;
	private String hostname;
	PortResponsePos portPos;

	public HP3ParVlunParams(String volumeName, String hostName, int lun) {
		super();
		this.volumeName = volumeName;
		this.lun = lun;
		this.hostname = hostName;
	}

	public HP3ParVlunParams(String volumeName, String hostName, int lun, PortResponsePos portPos) {
		super();
		this.volumeName = volumeName;
		this.lun = lun;
		this.hostname = hostName;
		this.portPos = portPos;
	}

	public String getVolumeName() {
		return this.volumeName;
	}

	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

	public int getLun() {
		return this.lun;
	}

	public void setLun(int lun) {
		this.lun = lun;
	}

	public String getHostName() {
		return this.hostname;
	}

	public void setHostName(String hostName) {
		this.hostname = hostName;
	}

}
