package com.cisco.matday.ucsd.hp3par.tasks.volumes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Config file to edit volumes
 * 
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "HP3Par_edit_volume")
public class EditVolumeConfig implements TaskConfigIf {
	/**
	 * @return the volume
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(String volume) {
		this.volume = volume;
	}

	/**
	 * @return the newVolumeName
	 */
	public String getNewVolumeName() {
		return newVolumeName;
	}

	/**
	 * @param newVolumeName
	 *            the newVolumeName to set
	 */
	public void setNewVolumeName(String newVolumeName) {
		this.newVolumeName = newVolumeName;
	}

	/**
	 * Task display label
	 */
	public static final String DISPLAY_LABEL = "3PAR Edit Volume";

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = HP3ParConstants.ACCOUNT_LIST_FORM_LABEL, help = "HP 3PAR Account", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.ACCOUNT_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.ACCOUNT_LIST_FORM_TABLE_NAME)
	@Persistent
	private String account;

	@FormField(label = HP3ParConstants.VOLUME_LIST_FORM_LABEL, help = "Volume to edit", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.VOLUME_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.VOLUME_LIST_FORM_TABLE_NAME)
	@Persistent
	private String volume;

	@FormField(label = "New Volume Name", help = "New volume name", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String newVolumeName;

	@FormField(label = HP3ParConstants.COPY_CPG_LIST_FORM_LABEL, help = "Copy CPG for the volume", mandatory = false, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.CPG_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.CPG_LIST_FORM_TABLE_NAME)
	@Persistent
	private String copyCpg;

	@FormField(label = "Comment", help = "Comment", mandatory = false, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String comment;

	/**
	 * Empty default constructor - this method shouldn't be instantiated
	 * directly
	 */
	public EditVolumeConfig() {

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

	/**
	 * @return the copyCpg
	 */
	public String getCopyCpg() {
		return copyCpg;
	}

	/**
	 * @param copyCpg
	 *            the copyCpg to set
	 */
	public void setCopyCpg(String copyCpg) {
		this.copyCpg = copyCpg;
	}

	/**
	 * Get the account name
	 * 
	 * @return Account name to do this on
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Set the account name
	 * 
	 * @param account
	 *            The volume to be created
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * Get the comment
	 * 
	 * @return Comment - might be null (and is optional)
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Set the comment - this is optional
	 * 
	 * @param comment
	 *            Optional commentary
	 */
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