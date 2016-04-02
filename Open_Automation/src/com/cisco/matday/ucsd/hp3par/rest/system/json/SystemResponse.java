/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.cisco.matday.ucsd.hp3par.rest.system.json;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getIPv4Addr() {
		return IPv4Addr;
	}

	public void setIPv4Addr(String iPv4Addr) {
		IPv4Addr = iPv4Addr;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public short getTotalNodes() {
		return totalNodes;
	}

	public void setTotalNodes(short totalNodes) {
		this.totalNodes = totalNodes;
	}

	public short getMasterNode() {
		return masterNode;
	}

	public void setMasterNode(short masterNode) {
		this.masterNode = masterNode;
	}

	public double getChunkletSizeMiB() {
		return chunkletSizeMiB;
	}

	public void setChunkletSizeMiB(double chunkletSizeMiB) {
		this.chunkletSizeMiB = chunkletSizeMiB;
	}

	public double getTotalCapacityMiB() {
		return totalCapacityMiB;
	}

	public void setTotalCapacityMiB(double totalCapacityMiB) {
		this.totalCapacityMiB = totalCapacityMiB;
	}

	public double getAllocatedCapacityMiB() {
		return allocatedCapacityMiB;
	}

	public void setAllocatedCapacityMiB(double allocatedCapacityMiB) {
		this.allocatedCapacityMiB = allocatedCapacityMiB;
	}

	public double getFreeCapacityMiB() {
		return freeCapacityMiB;
	}

	public void setFreeCapacityMiB(double freeCapacityMiB) {
		this.freeCapacityMiB = freeCapacityMiB;
	}

	public double getFailedCapacityMiB() {
		return failedCapacityMiB;
	}

	public void setFailedCapacityMiB(double failedCapacityMiB) {
		this.failedCapacityMiB = failedCapacityMiB;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}



}
