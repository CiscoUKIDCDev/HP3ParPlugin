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

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task for the 3PAR Volume deletion task
 * <p>
 * This shouldn't be instantiated directly, instead it should be included as a
 * form field or task config
 * 
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "HP3Par_delete_volume")
public class DeleteVolumeConfig implements TaskConfigIf {
	/**
	 * Task display label
	 */
	public static final String DISPLAY_LABEL = "Delete 3PAR Volume";

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = HP3ParConstants.ACCOUNT_LIST_FORM_LABEL, help = "HP 3PAR Account", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.ACCOUNT_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.ACCOUNT_LIST_FORM_TABLE_NAME)
	@Persistent
	private String account;

	@FormField(label = HP3ParConstants.VOLUME_LIST_FORM_LABEL, help = "Volume to delete", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.VOLUME_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.VOLUME_LIST_FORM_TABLE_NAME)
	@Persistent
	private String volume;

	private static Logger logger = Logger.getLogger(DeleteVolumeConfig.class);

	/**
	 * Empty default constructor - this method shouldn't be instantiated
	 * directly
	 */
	public DeleteVolumeConfig() {

	}

	/**
	 * Rollback constructor - used specifically for tasks that can call this to
	 * perform rollback - shouldn't be instantiated directly
	 */
	public DeleteVolumeConfig(CreateVolumeConfig config) {
		logger.info("Rolling back task - deleting volume: " + config.getVolumeName());
		this.account = config.getAccount();
		String volParse = "0@" + config.getAccount() + "@" + config.getVolumeName();
		logger.info("Format: " + volParse);
		this.volume = volParse;
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
	 * @return Account name to do this on 
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * Set the account name
	 * @param account The volume to be created 
	 */
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
	
	/**
	 * Get the Volume name
	 * @return Volume details (formatted id@account@volumeName)
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * Set the Volume name
	 * @param volume Must be formatted id@account@volumeName
	 */
	public void setVolume(String volume) {
		this.volume = volume;
	}

}
