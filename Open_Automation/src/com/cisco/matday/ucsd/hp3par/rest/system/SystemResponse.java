package com.cisco.matday.ucsd.hp3par.rest.system;

public class SystemResponse {
	private long id;
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
	public long getChunkletSizeMiB() {
		return chunkletSizeMiB;
	}
	public void setChunkletSizeMiB(long chunkletSizeMiB) {
		this.chunkletSizeMiB = chunkletSizeMiB;
	}
	public long getTotalCapacityMiB() {
		return totalCapacityMiB;
	}
	public void setTotalCapacityMiB(long totalCapacityMiB) {
		this.totalCapacityMiB = totalCapacityMiB;
	}
	public long getAllocatedCapacityMiB() {
		return allocatedCapacityMiB;
	}
	public void setAllocatedCapacityMiB(long allocatedCapacityMiB) {
		this.allocatedCapacityMiB = allocatedCapacityMiB;
	}
	public long getFreeCapacityMiB() {
		return freeCapacityMiB;
	}
	public void setFreeCapacityMiB(long freeCapacityMiB) {
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
	private String name;
	private String systemVersion;
	private String IPv4Addr;
	private String model;
	private String serialNumber;
	private short totalNodes;
	private short masterNode;
	private long chunkletSizeMiB;
	private long totalCapacityMiB;
	private long allocatedCapacityMiB;
	private long freeCapacityMiB;
	private double failedCapacityMiB;
	private String timeZone;

}
