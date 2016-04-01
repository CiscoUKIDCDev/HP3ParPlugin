package com.cisco.matday.ucsd.hp3par.rest.cpg;

import java.util.List;

public class CPGResponse {
	private int total;
	private List<CPGResponseMembers> members;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<CPGResponseMembers> getMembers() {
		return members;
	}

	public void setMembers(List<CPGResponseMembers> members) {
		this.members = members;
	}
}
