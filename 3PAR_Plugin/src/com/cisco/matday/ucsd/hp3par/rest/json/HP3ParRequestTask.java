package com.cisco.matday.ucsd.hp3par.rest.json;

/**
 * Maps to the {"taskid":x} output from 3PAR.
 * 
 * Refer to the 3PAR docs for more info
 * @author Matt Day
 *
 */
public class HP3ParRequestTask {
	private int taskid;

	/**
	 * @return the taskid
	 */
	public int getTaskid() {
		return taskid;
	}

	/**
	 * @param taskid the taskid to set
	 */
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
}
