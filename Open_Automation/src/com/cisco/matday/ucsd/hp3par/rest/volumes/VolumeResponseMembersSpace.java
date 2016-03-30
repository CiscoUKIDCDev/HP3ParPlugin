package com.cisco.matday.ucsd.hp3par.rest.volumes;

public class VolumeResponseMembersSpace {
	 /* "snapshotSpace": { "reservedMiB": 0, "rawReservedMiB": 0, "usedMiB": 0,
	 * "freeMiB": 0 },*/
	private long reservedMiB;
	private long rawReservedMiB;
	private long usedMiB;
	private long freeMiB;
	public long getReservedMiB() {
		return reservedMiB;
	}
	public void setReservedMiB(long reservedMiB) {
		this.reservedMiB = reservedMiB;
	}
	public long getRawReservedMiB() {
		return rawReservedMiB;
	}
	public void setRawReservedMiB(long rawReservedMiB) {
		this.rawReservedMiB = rawReservedMiB;
	}
	public long getUsedMiB() {
		return usedMiB;
	}
	public void setUsedMiB(long usedMiB) {
		this.usedMiB = usedMiB;
	}
	public long getFreeMiB() {
		return freeMiB;
	}
	public void setFreeMiB(long freeMiB) {
		this.freeMiB = freeMiB;
	}
}
