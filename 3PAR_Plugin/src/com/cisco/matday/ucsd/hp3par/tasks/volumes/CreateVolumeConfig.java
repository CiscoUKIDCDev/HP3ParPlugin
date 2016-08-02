/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
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
package com.cisco.matday.ucsd.hp3par.tasks.volumes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task for the 3PAR Volume creation task
 * <p>
 * This shouldn't be instantiated directly, instead it should be included as a
 * form field or task config
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "HP3Par_create_volume")
public class CreateVolumeConfig implements TaskConfigIf {
	/**
	 * Task display label
	 */
	public static final String DISPLAY_LABEL = "3PAR Create Volume";

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = HP3ParConstants.ACCOUNT_LIST_FORM_LABEL, help = "HP 3PAR Account", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.ACCOUNT_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.ACCOUNT_LIST_FORM_TABLE_NAME)
	@Persistent
	private String account;

	@FormField(label = HP3ParConstants.CPG_LIST_FORM_LABEL, help = "CPG to place the volume", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.CPG_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.CPG_LIST_FORM_TABLE_NAME)
	@Persistent
	private String cpg;

	@FormField(label = HP3ParConstants.COPY_CPG_LIST_FORM_LABEL, help = "Copy CPG for the volume", mandatory = false, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.CPG_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.CPG_LIST_FORM_TABLE_NAME)
	@Persistent
	private String copyCpg;

	@FormField(label = "Volume Name", help = "Name for your new volume", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String volumeName;

	@FormField(label = "Volume Size MiB", help = "Size of your new volume in MiB", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private long volume_size;

	@FormField(label = "Thin Provision", help = "Thin Provision", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	@UserInputField(type = "Boolean")
	@Persistent
	private boolean thin_provision = false;

	@FormField(label = "Comment", help = "Comment", mandatory = false)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String comment;

	/**
	 * Empty default constructor - this method shouldn't be instantiated
	 * directly
	 */
	public CreateVolumeConfig() {

	}

	@Override
	public long getActionId() {
		return this.actionId;
	}

	@Override
	public long getConfigEntryId() {
		return this.configEntryId;
	}

	@Override
	public String getDisplayLabel() {
		return DISPLAY_LABEL;
	}

	/**
	 * Get the desired volume
	 *
	 * @return The volume to be created
	 */
	public String getVolumeName() {
		return this.volumeName;
	}

	/**
	 * Set the desired volume
	 *
	 * @param volumeName
	 *            The volume to be created
	 */
	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

	/**
	 * Get the desired volume size
	 *
	 * @return The size of the volume to be created
	 */
	public long getVolume_size() {
		return this.volume_size;
	}

	/**
	 * Set the desired volume size
	 *
	 * @param volume_size
	 *            The volume to be created
	 */
	public void setVolume_size(long volume_size) {
		this.volume_size = volume_size;
	}

	/**
	 * @return the copyCpg
	 */
	public String getCopyCpg() {
		return this.copyCpg;
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
		return this.account;
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
	 * Get the CPG
	 *
	 * @return CPG details (formatted id@account@cpgName)
	 */
	public String getCpg() {
		return this.cpg;
	}

	/**
	 * Set the CPG name
	 *
	 * @param cpg
	 *            The cpg to create the volume on - must be formatted
	 *            id@account@cpgName
	 */
	public void setCpg(String cpg) {
		this.cpg = cpg;
	}

	/**
	 * Get the comment
	 *
	 * @return Comment - might be null (and is optional)
	 */
	public String getComment() {
		return this.comment;
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

	/**
	 * Get if this should be thin provisioned or not
	 *
	 * @return True if thin provisioning is desired
	 */
	public boolean isThin_provision() {
		return this.thin_provision;
	}

	/**
	 * Set if this should be thin provisioned
	 *
	 * @param thin_provision
	 *            true to thin provision
	 */
	public void setThin_provision(boolean thin_provision) {
		this.thin_provision = thin_provision;
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
