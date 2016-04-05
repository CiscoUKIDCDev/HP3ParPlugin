package com.cisco.matday.ucsd.hp3par.rest.copy.json;

/**
 * Represents an HP 3PAR REST API call to create a volume snapshot
 * 
 * See the HP documentation for more detail
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class HP3ParVolumeAction {

	
	private String action;

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the parameters
	 */
	public HP3ParVolumeActionParams getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(HP3ParVolumeActionParams parameters) {
		this.parameters = parameters;
	}

	public HP3ParVolumeAction(String action, HP3ParVolumeActionParams parameters) {
		this.action = action;
		this.parameters = parameters;
	}

	private HP3ParVolumeActionParams parameters;

}
