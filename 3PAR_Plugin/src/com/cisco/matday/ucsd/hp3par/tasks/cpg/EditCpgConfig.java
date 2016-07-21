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
package com.cisco.matday.ucsd.hp3par.tasks.cpg;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task for the 3PAR Cpg deletion task
 * <p>
 * This shouldn't be instantiated directly, instead it should be included as a
 * form field or task config
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "HP3Par_edit_cpg")
public class EditCpgConfig implements TaskConfigIf {
	/**
	 * Task display label
	 */
	public static final String DISPLAY_LABEL = "3PAR Edit CPG";

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = HP3ParConstants.CPG_LIST_FORM_LABEL, help = "Cpg to edit", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.CPG_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.CPG_LIST_FORM_TABLE_NAME)
	@Persistent
	private String cpg;

	@FormField(label = "New CPG Name", help = "New Name for your CPG", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = HP3ParConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String newName;

	/**
	 * Empty default constructor - this method shouldn't be instantiated
	 * directly
	 */
	public EditCpgConfig() {

	}

	/**
	 * Rollback constructor - this method shouldn't be instantiated directly
	 * 
	 * @param config
	 *            config to rollback from
	 * @param originalName
	 *            Original CPG name to return to
	 */
	public EditCpgConfig(EditCpgConfig config, String originalName) {
		final String cpgName = "0@" + config.getAccount() + "@" + config.getNewName();
		this.newName = originalName;
		this.cpg = cpgName;
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
	 * Get the account name
	 *
	 * @return Account name to do this on
	 */
	public String getAccount() {
		// Cpg is in the fomrat id@Account@Cpg
		return this.cpg.split("@")[1];
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
	 * Get the Cpg name
	 *
	 * @return Cpg details (formatted id@account@cpgName)
	 */
	public String getCpg() {
		return this.cpg;
	}

	/**
	 * Set the Cpg name
	 *
	 * @param cpg
	 *            Must be formatted id@account@cpgName
	 */
	public void setCpg(String cpg) {
		this.cpg = cpg;
	}

	/**
	 * @return the newName
	 */
	public String getNewName() {
		return this.newName;
	}

	/**
	 * @param newName
	 *            the newName to set
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}

}
