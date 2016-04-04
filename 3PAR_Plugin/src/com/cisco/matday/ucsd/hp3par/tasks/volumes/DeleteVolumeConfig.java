package com.cisco.matday.ucsd.hp3par.tasks.volumes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

@PersistenceCapable(detachable = "true", table = "HP3Par_delete_volume")
public class DeleteVolumeConfig implements TaskConfigIf {
	public static final String DISPLAY_LABEL = "Delete 3PAR Volume";

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = HP3ParConstants.ACCOUNT_LIST_FORM_NAME, help = "HP 3PAR Account", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.ACCOUNT_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.ACCOUNT_LIST_FORM_TABLE_NAME)
	@Persistent
	private String account;

	@FormField(label = HP3ParConstants.VOLUME_LIST_FORM_NAME, help = "Volume to delete", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.VOLUME_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.VOLUME_LIST_FORM_TABLE_NAME)
	@Persistent
	private String volume;

	public DeleteVolumeConfig() {

	}

	public DeleteVolumeConfig(CreateVolumeConfig config) {
		this.setAccount(config.getAccount());
		this.setVolume(config.getVolumeName());
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	@Override
	public void setConfigEntryId(long configEntryId) {
		this.configEntryId = configEntryId;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	

}
