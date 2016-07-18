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

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

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

	@Persistent
	private String originalName;

	/**
	 * Initialise and remember the original volume name
	 */
	public EditVolumeConfig() {
		this.originalName = this.newVolumeName;
	}

	/**
	 * Allow rollback of volume editing
	 *
	 * @param config
	 */
	public EditVolumeConfig(EditVolumeConfig config) {
		// Construct volume ID
		String volParse = "0@" + config.getAccount() + "@" + config.getNewVolumeName();
		this.setNewVolumeName(config.getVolume().split("@")[2]);
		this.setVolume(volParse);
	}

	/**
	 * @return the volume
	 */
	public String getVolume() {
		return this.volume;
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
		return this.newVolumeName;
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

	/**
	 * @return Original name of the volume (pre-editing)
	 */
	public String getOriginalName() {
		return this.originalName;
	}

	/**
	 * @param originalName
	 *            Set the original name of the volume (pre-editing)
	 */
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
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
		// Volume is in the fomrat id@Account@Volume
		return this.volume.split("@")[1];
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

	@Override
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	@Override
	public void setConfigEntryId(long configEntryId) {
		this.configEntryId = configEntryId;
	}

}
