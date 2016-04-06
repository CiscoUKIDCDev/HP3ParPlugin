/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package com.cisco.matday.ucsd.hp3par.tasks.copy;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task for the 3PAR Volume snapshot creation task
 * <p>
 * This shouldn't be instantiated directly, instead it should be included as a
 * form field or task config
 * 
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "HP3Par_create_volume_snapshot")
public class CreateVolumeCopyConfig implements TaskConfigIf {
	/**
	 * Task display label
	 */
	public static final String DISPLAY_LABEL = "3PAR Copy Volume";

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = HP3ParConstants.ACCOUNT_LIST_FORM_LABEL, help = "HP 3PAR Account", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.ACCOUNT_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.ACCOUNT_LIST_FORM_TABLE_NAME)
	@Persistent
	private String account;

	@FormField(label = HP3ParConstants.VOLUME_LIST_FORM_LABEL, help = "Volume to copy", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.VOLUME_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.VOLUME_LIST_FORM_TABLE_NAME)
	@Persistent
	private String volume;

	@FormField(label = "New Volume Name", help = "Name for your new volume", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String newVolumeName;

	@FormField(label = HP3ParConstants.CPG_LIST_FORM_LABEL, help = "CPG to place the volume", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.CPG_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.CPG_LIST_FORM_TABLE_NAME)
	@Persistent
	private String cpg;

	@FormField(label = HP3ParConstants.COPY_CPG_LIST_FORM_LABEL, help = "Copy CPG for the volume", mandatory = false, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.CPG_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.CPG_LIST_FORM_TABLE_NAME)
	@Persistent
	private String copyCpg;

	@FormField(label = "Online", help = "Online", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	@UserInputField(type = "Boolean")
	@Persistent
	private boolean online = true;

	@FormField(label = "Thin Provision", help = "Thin Provision", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	@UserInputField(type = "Boolean")
	@Persistent
	private boolean thinProvision = false;

	@FormField(label = "Comment", help = "Comment", mandatory = false, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String comment;

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Empty default constructor - this method shouldn't be instantiated
	 * directly
	 */
	public CreateVolumeCopyConfig() {

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
	 * Get the Volume name
	 * 
	 * @return Volume details (formatted id@account@volumeName)
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * Set the Volume name
	 * 
	 * @param volume
	 *            Must be formatted id@account@volumeName
	 */
	public void setVolume(String volume) {
		this.volume = volume;
	}

	@Override
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	@Override
	public void setConfigEntryId(long configEntryId) {
		this.configEntryId = configEntryId;
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
	 * @return the cpg
	 */
	public String getCpg() {
		return cpg;
	}

	/**
	 * @param cpg
	 *            the cpg to set
	 */
	public void setCpg(String cpg) {
		this.cpg = cpg;
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
	 * @return the thinProvision
	 */
	public boolean isThinProvision() {
		return thinProvision;
	}

	/**
	 * @param thinProvision
	 *            the thinProvision to set
	 */
	public void setThinProvision(boolean thinProvision) {
		this.thinProvision = thinProvision;
	}

}
