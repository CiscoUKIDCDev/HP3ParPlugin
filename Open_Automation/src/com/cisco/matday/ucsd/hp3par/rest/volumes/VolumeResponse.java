package com.cisco.matday.ucsd.hp3par.rest.volumes;

import java.util.List;

public class VolumeResponse {
	private int total;
	private List<VolumeResponseMembers> members;
	
	public void setMembers(List<VolumeResponseMembers> members) {
		this.members = members;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public long getTotal() {
		return total;
	}

	public List<VolumeResponseMembers> getMembers() {
		return members;
	}

	
	
}
