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
package com.cisco.matday.ucsd.hp3par.reports.hostsets.actions;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParHostSetException;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.tasks.hostsets.EditHostSetConfig;
import com.cisco.matday.ucsd.hp3par.tasks.hostsets.HP3ParHostSetExecute;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Add a host to a host selection
 * 
 * @author Matt Day
 *
 */
public class AddHostToHostSetAction extends CloupiaPageAction {

	private static Logger logger = Logger.getLogger(CreateHostSetAction.class);

	// need to provide a unique string to identify this form and action
	private final String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.hostsets.actions.AddHostToHostSetAction";
	private final String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.hostsets.actions.AddHostToHostSetAction";
	private final String LABEL = "Add";
	private final String DESCRIPTION = "Create a new Host set";

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Create Host custom task
		page.bind(this.FORM_ID, EditHostSetConfig.class);

	}

	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		EditHostSetConfig form = new EditHostSetConfig();

		form.setHosts(query);

		// The form will be in the format Account;Pod - grab the former:
		// String account = query.split(";")[0];

		// Pre-populate the account field:
		// form.setAccount(account);

		// Set the account field to read-only (I couldn't find this documented
		// anywhere, maybe there's a better way to do it?)
		// page.getFlist().getByFieldId(this.FORM_ID +
		// ".account").setEditable(false);

		session.getSessionAttributes().put(this.FORM_ID, form);
		page.marshallFromSession(this.FORM_ID);

	}

	@Override
	public int validatePageData(Page page, ReportContext context, WizardSession session) throws Exception {
		Object obj = page.unmarshallToSession(this.FORM_ID);
		EditHostSetConfig config = (EditHostSetConfig) obj;

		// Get credentials from the current context
		HP3ParCredentials c = new HP3ParCredentials(context);
		// TODO this is broken - it will delete anything not included - should
		// create an add method
		HP3ParRequestStatus s = HP3ParHostSetExecute.edit(c, config);

		// Throwing an exception fails the submit and shows the error in the
		// window
		if (!s.isSuccess()) {
			logger.warn("Failed to create Host:" + s.getError());
			throw new HP3ParHostSetException("Failed to create Host: " + s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Host " + config.getHostSetName() + " created OK");
		return PageIf.STATUS_OK;
	}

	@Override
	public boolean isDoubleClickAction() {
		return false;
	}

	@Override
	public boolean isDrilldownAction() {
		return false;
	}

	@Override
	public boolean isMultiPageAction() {
		return false;
	}

	@Override
	public boolean isSelectionRequired() {
		return false;
	}

	@Override
	public String getActionId() {
		return this.ACTION_ID;
	}

	@Override
	public int getActionType() {
		return ConfigTableAction.ACTION_TYPE_POPUP_FORM;
	}

	@Override
	public String getDescription() {
		return this.DESCRIPTION;
	}

	@Override
	public String getFormId() {
		return this.FORM_ID;
	}

	@Override
	public String getLabel() {
		return this.LABEL;
	}

	@Override
	public String getTitle() {
		return this.LABEL;
	}

}
