package com.cisco.matday.ucsd.hp3par.rest.volumes.json;

public class HP3ParVolumeStatus {
	private boolean created = false;
	private String error;
	public String getError() {
		if (error == null) {
			return "no error";
		}
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public boolean isCreated() {
		return created;
	}
	public void setCreated(boolean created) {
		this.created = created;
	}
}
