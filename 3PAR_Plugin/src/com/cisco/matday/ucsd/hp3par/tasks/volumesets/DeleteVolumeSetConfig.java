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
package com.cisco.matday.ucsd.hp3par.tasks.volumesets;

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
@PersistenceCapable(detachable = "true", table = "HP3Par_delete_volume_set")
public class DeleteVolumeSetConfig implements TaskConfigIf {
	/**
	 * Task display label
	 */
	public static final String DISPLAY_LABEL = "3PAR Delete Volume Set";

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = HP3ParConstants.VOLUMESET_LIST_FORM_LABEL, help = "HP 3PAR Volume Set", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.VOLUMESET_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.VOLUMESET_LIST_FORM_TABLE_NAME)
	@Persistent
	private String volumeSet;

	/**
	 * Empty default constructor - this method shouldn't be instantiated
	 * directly
	 */
	public DeleteVolumeSetConfig() {

	}

	/**
	 * Rollback constructor - this method shouldn't be instantiated directly
	 *
	 * @param config
	 *            Config to rollback
	 */
	public DeleteVolumeSetConfig(CreateVolumeSetConfig config) {
		this.volumeSet = "0@" + config.getAccount() + "@" + config.getVolumeSetName();
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

	@Override
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	@Override
	public void setConfigEntryId(long configEntryId) {
		this.configEntryId = configEntryId;
	}

	/**
	 * @return Account name
	 */
	public String getAccount() {
		return this.volumeSet.split(";")[0];
	}

	/**
	 * @return volume set
	 */
	public String getVolumeSet() {
		return this.volumeSet;
	}

	/**
	 * @param volumeSet
	 */
	public void setVolumeSet(String volumeSet) {
		this.volumeSet = volumeSet;
	}

}
