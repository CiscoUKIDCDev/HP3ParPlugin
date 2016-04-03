package com.cisco.matday.ucsd.hp3par.tasks.volumes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

@PersistenceCapable(detachable = "true", table = "HP3Par_create_volume")
public class CreateVolumeConfig implements TaskConfigIf {
	public static final String DISPLAY_LABEL = "Create 3PAR Volume";
	
	@Persistent
	private long configEntryId;
	@Persistent
	private long actionId;
	
	@FormField(label = "Volume Name", help = "Name for your new volume", mandatory = true)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String volumeName;
	
	@FormField(label = "Volume Size MiB", help = "Size of your new volume in MiB", mandatory = true)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private long volume_size;
	
	@FormField(label = "Account", help = "HP 3PAR Account", mandatory = true)
	@UserInputField(type = HP3ParConstants.ACCOUNT_LIST_FORM_NAME)
	@Persistent
	private String account;
	
	@FormField(label = "CPG", help = "CPG to place the volume", mandatory = true)
	@UserInputField(type = HP3ParConstants.ACCOUNT_LIST_FORM_NAME)
	@Persistent
	private String cpg;
	
	@FormField(label = "Comment", help = "Comment")
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String comment;
	
	public CreateVolumeConfig () {
		
	}
	@Override
	public long getActionId() {
		return actionId;
	}

	@Override
	public long getConfigEntryId() {
		return configEntryId;
	}

	@Override
	public String getDisplayLabel() {
		return DISPLAY_LABEL;
	}
   

	public String getVolumeName() {
		return volumeName;
	}



	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}



	public long getVolume_size() {
		return volume_size;
	}



	public void setVolume_size(long volume_size) {
		this.volume_size = volume_size;
	}



	public String getAccount() {
		return account;
	}



	public void setAccount(String account) {
		this.account = account;
	}



	public String getCpg() {
		return cpg;
	}



	public void setCpg(String cpg) {
		this.cpg = cpg;
	}



	public String getComment() {
		return comment;
	}



	public void setComment(String comment) {
		this.comment = comment;
	}


	@Override
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	@Override
	public void setConfigEntryId(long configEntryId) {
		this.configEntryId = configEntryId;
	}

}
