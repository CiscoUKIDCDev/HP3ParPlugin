package com.cisco.matday.ucsd.hp3par.rest.volumes.json;
/*
 * { "name": "paw-api-test", "cpg": "FC_r1", "sizeMiB": 1024,
	 * "comment": "Matt Testing" }
 */
public class HP3ParVolumeInformation {
	private String name;
	private String cpg;
	private long sizeMiB;
	private String comment;
	
	public HP3ParVolumeInformation(String name, String cpg, long sizeMiB, String comment) {
		this.name = name;
		this.cpg = cpg;
		this.sizeMiB = sizeMiB;
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpg() {
		return cpg;
	}

	public void setCpg(String cpg) {
		this.cpg = cpg;
	}

	public long getSizeMiB() {
		return sizeMiB;
	}

	public void setSizeMiB(long sizeMiB) {
		this.sizeMiB = sizeMiB;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
