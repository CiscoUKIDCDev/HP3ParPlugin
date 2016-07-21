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
 * Config file to grow volumes
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "HP3Par_grow_volume")
public class GrowVolumeConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = HP3ParConstants.VOLUME_LIST_FORM_LABEL, help = "Volume to grow", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.VOLUME_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.VOLUME_LIST_FORM_TABLE_NAME)
	@Persistent
	private String volume;

	@FormField(label = "Additional Growth Size (MiB)", help = "Ammount in MiB to add to the existing volume", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private int newSize;

	/**
	 * Task display label
	 */
	public static final String DISPLAY_LABEL = "3PAR Grow Volume";

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
	 * @return the newSize
	 */
	public int getNewSize() {
		return this.newSize;
	}

	/**
	 * @param newSize
	 *            the newSize to set
	 */
	public void setNewSize(int newSize) {
		this.newSize = newSize;
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

	@Override
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	@Override
	public void setConfigEntryId(long configEntryId) {
		this.configEntryId = configEntryId;
	}

}
