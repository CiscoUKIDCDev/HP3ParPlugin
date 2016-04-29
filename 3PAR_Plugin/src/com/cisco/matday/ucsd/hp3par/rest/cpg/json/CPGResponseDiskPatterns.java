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
 * REST object representation of the HP3PAR CPG disk patterns. See the HP 3PAR
 * REST documentation for detail on these methods and values
 *
 * @author Matt Day
 *
 */
public class CPGResponseDiskPatterns {
	private String nodeList;
	private String slotList;
	private String portList;
	private String cageList;
	private String magList;
	private String diskPosList;
	private String diskList;
	private int totalChunkletsGreaterThan;
	private int totalChunkletsLessThan;
	private int freeChunkletsGreaterThan;
	private int freeChunkletsLessThan;
	private String diskModels;
	private int diskType;
	private int RPM;

	/**
	 * @return the nodeList
	 */
	public String getNodeList() {
		return this.nodeList;
	}

	/**
	 * @param nodeList
	 *            the nodeList to set
	 */
	public void setNodeList(String nodeList) {
		this.nodeList = nodeList;
	}

	/**
	 * @return the slotList
	 */
	public String getSlotList() {
		return this.slotList;
	}

	/**
	 * @param slotList
	 *            the slotList to set
	 */
	public void setSlotList(String slotList) {
		this.slotList = slotList;
	}

	/**
	 * @return the portList
	 */
	public String getPortList() {
		return this.portList;
	}

	/**
	 * @param portList
	 *            the portList to set
	 */
	public void setPortList(String portList) {
		this.portList = portList;
	}

	/**
	 * @return the cageList
	 */
	public String getCageList() {
		return this.cageList;
	}

	/**
	 * @param cageList
	 *            the cageList to set
	 */
	public void setCageList(String cageList) {
		this.cageList = cageList;
	}

	/**
	 * @return the magList
	 */
	public String getMagList() {
		return this.magList;
	}

	/**
	 * @param magList
	 *            the magList to set
	 */
	public void setMagList(String magList) {
		this.magList = magList;
	}

	/**
	 * @return the diskPosList
	 */
	public String getDiskPosList() {
		return this.diskPosList;
	}

	/**
	 * @param diskPosList
	 *            the diskPosList to set
	 */
	public void setDiskPosList(String diskPosList) {
		this.diskPosList = diskPosList;
	}

	/**
	 * @return the diskList
	 */
	public String getDiskList() {
		return this.diskList;
	}

	/**
	 * @param diskList
	 *            the diskList to set
	 */
	public void setDiskList(String diskList) {
		this.diskList = diskList;
	}

	/**
	 * @return the totalChunkletsGreaterThan
	 */
	public int getTotalChunkletsGreaterThan() {
		return this.totalChunkletsGreaterThan;
	}

	/**
	 * @param totalChunkletsGreaterThan
	 *            the totalChunkletsGreaterThan to set
	 */
	public void setTotalChunkletsGreaterThan(int totalChunkletsGreaterThan) {
		this.totalChunkletsGreaterThan = totalChunkletsGreaterThan;
	}

	/**
	 * @return the totalChunkletsLessThan
	 */
	public int getTotalChunkletsLessThan() {
		return this.totalChunkletsLessThan;
	}

	/**
	 * @param totalChunkletsLessThan
	 *            the totalChunkletsLessThan to set
	 */
	public void setTotalChunkletsLessThan(int totalChunkletsLessThan) {
		this.totalChunkletsLessThan = totalChunkletsLessThan;
	}

	/**
	 * @return the freeChunkletsGreaterThan
	 */
	public int getFreeChunkletsGreaterThan() {
		return this.freeChunkletsGreaterThan;
	}

	/**
	 * @param freeChunkletsGreaterThan
	 *            the freeChunkletsGreaterThan to set
	 */
	public void setFreeChunkletsGreaterThan(int freeChunkletsGreaterThan) {
		this.freeChunkletsGreaterThan = freeChunkletsGreaterThan;
	}

	/**
	 * @return the freeChunkletsLessThan
	 */
	public int getFreeChunkletsLessThan() {
		return this.freeChunkletsLessThan;
	}

	/**
	 * @param freeChunkletsLessThan
	 *            the freeChunkletsLessThan to set
	 */
	public void setFreeChunkletsLessThan(int freeChunkletsLessThan) {
		this.freeChunkletsLessThan = freeChunkletsLessThan;
	}

	/**
	 * @return the diskModels
	 */
	public String getDiskModels() {
		return this.diskModels;
	}

	/**
	 * @param diskModels
	 *            the diskModels to set
	 */
	public void setDiskModels(String diskModels) {
		this.diskModels = diskModels;
	}

	/**
	 * @return the diskType
	 */
	public int getDiskType() {
		return this.diskType;
	}

	/**
	 * @param diskType
	 *            the diskType to set
	 */
	public void setDiskType(int diskType) {
		this.diskType = diskType;
	}

	/**
	 * @return the rPM
	 */
	public int getRPM() {
		return this.RPM;
	}

	/**
	 * @param rPM
	 *            the rPM to set
	 */
	public void setRPM(int rPM) {
		this.RPM = rPM;
	}

}
