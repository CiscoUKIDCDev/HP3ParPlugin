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
package com.cisco.matday.ucsd.hp3par.tasks.hostsets;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task to add a single host to a host set
 * <p>
 * This shouldn't be instantiated directly, instead it should be included as a
 * form field or task config
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "HP3Par_remove_host_from_host_set")
public class RemoveHostFromHostSetConfig implements TaskConfigIf {

	/**
	 * Task display label
	 */
	public static final String DISPLAY_LABEL = "3PAR Remove from Host Set";

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = HP3ParConstants.HOSTSET_LIST_FORM_LABEL, help = "Host set", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.HOSTSET_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.HOSTSET_LIST_FORM_TABLE_NAME)
	@Persistent
	private String hostSet;

	@FormField(label = "Host", help = "Hosts", multiline = false, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = HP3ParConstants.HOST_LIST_FORM_PROVIDER)
	@UserInputField(type = HP3ParConstants.HOST_LIST_FORM_TABLE_NAME)
	@Persistent
	private String host;

	/**
	 * Default constructor (not for rollback)
	 */
	public RemoveHostFromHostSetConfig() {
		super();
	}

	/**
	 * For rollback command
	 *
	 * @param c
	 *            configuration
	 */
	public RemoveHostFromHostSetConfig(AddHostToHostSetConfig c) {
		this.hostSet = c.getHostSet();
		this.host = c.getHost();
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
	 * @return the hostSet
	 */
	public String getHostSet() {
		return this.hostSet;
	}

	/**
	 * @param hostSet
	 *            the hostSet to set
	 */
	public void setHostSet(String hostSet) {
		this.hostSet = hostSet;
	}

	/**
	 * @return the hosts
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * @param host
	 *            the hosts to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return Account name
	 */
	public String getAccount() {
		return this.hostSet.split(";")[0];
	}

}
