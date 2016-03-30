package com.cisco.matday.ucsd.hp3par.rest.volumes;

public class VolumeResponseMembersPolicies {
	public boolean isStaleSS() {
		return staleSS;
	}
	public void setStaleSS(boolean staleSS) {
		this.staleSS = staleSS;
	}
	public boolean isOneHost() {
		return oneHost;
	}
	public void setOneHost(boolean oneHost) {
		this.oneHost = oneHost;
	}
	public boolean isZeroDetect() {
		return zeroDetect;
	}
	public void setZeroDetect(boolean zeroDetect) {
		this.zeroDetect = zeroDetect;
	}
	public boolean isSystem() {
		return system;
	}
	public void setSystem(boolean system) {
		this.system = system;
	}
	public boolean isCaching() {
		return caching;
	}
	public void setCaching(boolean caching) {
		this.caching = caching;
	}
	private boolean staleSS;
	private boolean oneHost;
	private boolean zeroDetect;
	private boolean system;
	private boolean caching;
}
