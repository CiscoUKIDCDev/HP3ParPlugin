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
package com.cisco.matday.ucsd.hp3par.rest.vluns.rest;

/**
 * @author Matt Day
 *
 */
public class VlunResponseMembers {
	private int lun;
	private String volumeName;
	private String hostname;
	private int type;
	private String volumeWWN;
	private int multipathing;
	private int failedPathPol;
	private int failedPathInterval;
	private boolean active;

	/**
	 * @return the lun
	 */
	public int getLun() {
		return this.lun;
	}

	/**
	 * @param lun
	 *            the lun to set
	 */
	public void setLun(int lun) {
		this.lun = lun;
	}

	/**
	 * @return the volumeName
	 */
	public String getVolumeName() {
		return this.volumeName;
	}

	/**
	 * @param volumeName
	 *            the volumeName to set
	 */
	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return this.hostname;
	}

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the volumeWWN
	 */
	public String getVolumeWWN() {
		return this.volumeWWN;
	}

	/**
	 * @param volumeWWN
	 *            the volumeWWN to set
	 */
	public void setVolumeWWN(String volumeWWN) {
		this.volumeWWN = volumeWWN;
	}

	/**
	 * @return the multipathing
	 */
	public int getMultipathing() {
		return this.multipathing;
	}

	/**
	 * @param multipathing
	 *            the multipathing to set
	 */
	public void setMultipathing(int multipathing) {
		this.multipathing = multipathing;
	}

	/**
	 * @return the failedPathPol
	 */
	public int getFailedPathPol() {
		return this.failedPathPol;
	}

	/**
	 * @param failedPathPol
	 *            the failedPathPol to set
	 */
	public void setFailedPathPol(int failedPathPol) {
		this.failedPathPol = failedPathPol;
	}

	/**
	 * @return the failedPathInterval
	 */
	public int getFailedPathInterval() {
		return this.failedPathInterval;
	}

	/**
	 * @param failedPathInterval
	 *            the failedPathInterval to set
	 */
	public void setFailedPathInterval(int failedPathInterval) {
		this.failedPathInterval = failedPathInterval;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
}
