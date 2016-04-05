package com.cisco.matday.ucsd.hp3par.rest.copy.json;

/**
 * Creates an object to send to the 3PAR array to create a volume copy.
 * 
 * Objects implementing this class should be passed to
 * HP3ParVolumeRestCall.create
 * 
 * @author Matt Day
 *
 */
public class HP3ParCopyParams implements HP3ParVolumeActionParams {
	/*
	 * { "parameters": { "destVolume": "matt", "destCPG": "FC_r1", "online":
	 * true, "tpvv": true, "snapCPG": "NL_r1", } }
	 */
	private String destVolume;
	private String destCPG;
	private boolean online;
	private boolean tpvv;
	private String snapCPG;
	private String comment;


	/**
	 * Creates a set of parameters to copy a volume
	 * 
	 * @param destVolume
	 *            New Volume
	 * @param destCPG
	 *            CPG for new Volume
	 * @param online
	 *            Should this volume be online or not?
	 * @param tpvv
	 *            Should this volume be thin provisioned?
	 * @param snapCPG
	 *            Should this volume have a copy CPG?
	 */
	public HP3ParCopyParams(String destVolume, String destCPG, boolean online, boolean tpvv, String snapCPG) {
		this.destVolume = destVolume;
		this.destCPG = destCPG;
		this.online = online;
		this.tpvv = tpvv;
		this.snapCPG = snapCPG;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the destVolume
	 */
	public String getDestVolume() {
		return destVolume;
	}

	/**
	 * @param destVolume
	 *            the destVolume to set
	 */
	public void setDestVolume(String destVolume) {
		this.destVolume = destVolume;
	}

	/**
	 * @return the destCPG
	 */
	public String getDestCPG() {
		return destCPG;
	}

	/**
	 * @param destCPG
	 *            the destCPG to set
	 */
	public void setDestCPG(String destCPG) {
		this.destCPG = destCPG;
	}

	/**
	 * @return the online
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * @param online
	 *            the online to set
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * @return the tpvv
	 */
	public boolean isTpvv() {
		return tpvv;
	}

	/**
	 * @param tpvv
	 *            the tpvv to set
	 */
	public void setTpvv(boolean tpvv) {
		this.tpvv = tpvv;
	}

	/**
	 * @return the snapCPG
	 */
	public String getSnapCPG() {
		return snapCPG;
	}

	/**
	 * @param snapCPG
	 *            the snapCPG to set
	 */
	public void setSnapCPG(String snapCPG) {
		this.snapCPG = snapCPG;
	}

}
