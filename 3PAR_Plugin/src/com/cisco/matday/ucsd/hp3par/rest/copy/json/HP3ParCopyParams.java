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
package com.cisco.matday.ucsd.hp3par.rest.copy.json;

/**
 * Creates an object to send to the 3PAR array to create a volume copy.
 * 
 * Objects implementing this class should be passed to
 * HP3ParVolumeRestCall.create
 * 
 * @author Matt Day
 *
 */
public class HP3ParCopyParams implements HP3ParVolumeActionParams {
	/*
	 * { "parameters": { "destVolume": "matt", "destCPG": "FC_r1", "online":
	 * true, "tpvv": true, "snapCPG": "NL_r1", } }
	 */
	private String destVolume;
	private String destCPG;
	private boolean online;
	private boolean tpvv;
	private String snapCPG;

	/**
	 * Creates a set of parameters to copy a volume
	 * 
	 * @param destVolume
	 *            New Volume
	 * @param destCPG
	 *            CPG for new Volume
	 * @param online
	 *            Should this volume be online or not?
	 * @param tpvv
	 *            Should this volume be thin provisioned?
	 * @param snapCPG
	 *            Should this volume have a copy CPG?
	 */
	public HP3ParCopyParams(String destVolume, String destCPG, boolean online, boolean tpvv, String snapCPG) {
		this.destVolume = destVolume;
		this.destCPG = destCPG;
		this.online = online;
		this.tpvv = tpvv;
		this.snapCPG = snapCPG;
	}

	/**
	 * @return the destVolume
	 */
	public String getDestVolume() {
		return destVolume;
	}

	/**
	 * @param destVolume
	 *            the destVolume to set
	 */
	public void setDestVolume(String destVolume) {
		this.destVolume = destVolume;
	}

	/**
	 * @return the destCPG
	 */
	public String getDestCPG() {
		return destCPG;
	}

	/**
	 * @param destCPG
	 *            the destCPG to set
	 */
	public void setDestCPG(String destCPG) {
		this.destCPG = destCPG;
	}

	/**
	 * @return the online
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * @param online
	 *            the online to set
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * @return the tpvv
	 */
	public boolean isTpvv() {
		return tpvv;
	}

	/**
	 * @param tpvv
	 *            the tpvv to set
	 */
	public void setTpvv(boolean tpvv) {
		this.tpvv = tpvv;
	}

	/**
	 * @return the snapCPG
	 */
	public String getSnapCPG() {
		return snapCPG;
	}

	/**
	 * @param snapCPG
	 *            the snapCPG to set
	 */
	public void setSnapCPG(String snapCPG) {
		this.snapCPG = snapCPG;
	}

}
