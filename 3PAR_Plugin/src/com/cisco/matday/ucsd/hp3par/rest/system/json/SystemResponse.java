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
package com.cisco.matday.ucsd.hp3par.rest.system.json;

/**
 * Object to represent JSON response for a System request on a 3PAR array.
 * 
 * See the HP3Par documentation for what these fields mean.
 * 
 * @author Matt Day
 *
 */
public class SystemResponse {
	private long id;
	private String name;
	private String systemVersion;
	private String IPv4Addr;
	private String model;
	private String serialNumber;
	private short totalNodes;
	private short masterNode;
	private double chunkletSizeMiB;
	private double totalCapacityMiB;
	private double allocatedCapacityMiB;
	private double freeCapacityMiB;
	private double failedCapacityMiB;
	private String timeZone;

	@SuppressWarnings("javadoc")
	public long getId() {
		return id;
	}

	@SuppressWarnings("javadoc")
	public void setId(long id) {
		this.id = id;
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
	public String getSystemVersion() {
		return systemVersion;
	}

	@SuppressWarnings("javadoc")
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	@SuppressWarnings("javadoc")
	public String getIPv4Addr() {
		return IPv4Addr;
	}

	@SuppressWarnings("javadoc")
	public void setIPv4Addr(String iPv4Addr) {
		IPv4Addr = iPv4Addr;
	}

	@SuppressWarnings("javadoc")
	public String getModel() {
		return model;
	}

	@SuppressWarnings("javadoc")
	public void setModel(String model) {
		this.model = model;
	}

	@SuppressWarnings("javadoc")
	public String getSerialNumber() {
		return serialNumber;
	}

	@SuppressWarnings("javadoc")
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@SuppressWarnings("javadoc")
	public short getTotalNodes() {
		return totalNodes;
	}

	@SuppressWarnings("javadoc")
	public void setTotalNodes(short totalNodes) {
		this.totalNodes = totalNodes;
	}

	@SuppressWarnings("javadoc")
	public short getMasterNode() {
		return masterNode;
	}

	@SuppressWarnings("javadoc")
	public void setMasterNode(short masterNode) {
		this.masterNode = masterNode;
	}

	@SuppressWarnings("javadoc")
	public double getChunkletSizeMiB() {
		return chunkletSizeMiB;
	}

	@SuppressWarnings("javadoc")
	public void setChunkletSizeMiB(double chunkletSizeMiB) {
		this.chunkletSizeMiB = chunkletSizeMiB;
	}

	@SuppressWarnings("javadoc")
	public double getTotalCapacityMiB() {
		return totalCapacityMiB;
	}

	@SuppressWarnings("javadoc")
	public void setTotalCapacityMiB(double totalCapacityMiB) {
		this.totalCapacityMiB = totalCapacityMiB;
	}

	@SuppressWarnings("javadoc")
	public double getAllocatedCapacityMiB() {
		return allocatedCapacityMiB;
	}

	@SuppressWarnings("javadoc")
	public void setAllocatedCapacityMiB(double allocatedCapacityMiB) {
		this.allocatedCapacityMiB = allocatedCapacityMiB;
	}

	@SuppressWarnings("javadoc")
	public double getFreeCapacityMiB() {
		return freeCapacityMiB;
	}

	@SuppressWarnings("javadoc")
	public void setFreeCapacityMiB(double freeCapacityMiB) {
		this.freeCapacityMiB = freeCapacityMiB;
	}

	@SuppressWarnings("javadoc")
	public double getFailedCapacityMiB() {
		return failedCapacityMiB;
	}

	@SuppressWarnings("javadoc")
	public void setFailedCapacityMiB(double failedCapacityMiB) {
		this.failedCapacityMiB = failedCapacityMiB;
	}

	@SuppressWarnings("javadoc")
	public String getTimeZone() {
		return timeZone;
	}

	@SuppressWarnings("javadoc")
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}
