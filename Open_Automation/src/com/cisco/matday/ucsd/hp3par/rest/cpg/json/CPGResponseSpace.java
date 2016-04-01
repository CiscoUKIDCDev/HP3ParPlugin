package com.cisco.matday.ucsd.hp3par.rest.cpg.json;

public class CPGResponseSpace {
    private long totalMiB;
    private long rawTotalMiB;
    private long usedMiB;
    public long getTotalMiB() {
		return totalMiB;
	}
	public void setTotalMiB(long totalMiB) {
		this.totalMiB = totalMiB;
	}
	public long getRawTotalMiB() {
		return rawTotalMiB;
	}
	public void setRawTotalMiB(long rawTotalMiB) {
		this.rawTotalMiB = rawTotalMiB;
	}
	public long getUsedMiB() {
		return usedMiB;
	}
	public void setUsedMiB(long usedMiB) {
		this.usedMiB = usedMiB;
	}
	public long getRawUsedMiB() {
		return rawUsedMiB;
	}
	public void setRawUsedMiB(long rawUsedMiB) {
		this.rawUsedMiB = rawUsedMiB;
	}
	private long rawUsedMiB;
}
