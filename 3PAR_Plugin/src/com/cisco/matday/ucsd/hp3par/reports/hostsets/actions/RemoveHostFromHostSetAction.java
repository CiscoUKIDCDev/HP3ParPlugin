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
package com.cisco.matday.ucsd.hp3par.reports.hostsets.actions;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParSetException;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.tasks.hostsets.HP3ParHostSetExecute;
import com.cisco.matday.ucsd.hp3par.tasks.hostsets.RemoveHostFromHostSetConfig;
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
public class RemoveHostFromHostSetAction extends CloupiaPageAction {

	private static Logger logger = Logger.getLogger(CreateHostSetAction.class);

	// need to provide a unique string to identify this form and action
	private final String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.hostsets.actions.RemoveHostFromHostSetAction";
	private final String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.hostsets.actions.RemoveHostFromHostSetAction";
	private final String LABEL = "Remove";
	private final String DESCRIPTION = "Remove host from host set";

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Create Host custom task
		page.bind(this.FORM_ID, RemoveHostFromHostSetConfig.class);

	}

	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		RemoveHostFromHostSetConfig form = new RemoveHostFromHostSetConfig();

		final String host = query.split(";")[0] + ";" + query.split(";")[1];
		final String hostSet = query.split(";")[0] + ";" + query.split(";")[2];

		form.setHost(host);
		form.setHostSet(hostSet);

		page.getFlist().getByFieldId(this.FORM_ID + ".hostSet").setEditable(false);
		page.getFlist().getByFieldId(this.FORM_ID + ".host").setEditable(false);

		session.getSessionAttributes().put(this.FORM_ID, form);
		page.marshallFromSession(this.FORM_ID);

	}

	@Override
	public int validatePageData(Page page, ReportContext context, WizardSession session) throws Exception {
		Object obj = page.unmarshallToSession(this.FORM_ID);
		RemoveHostFromHostSetConfig config = (RemoveHostFromHostSetConfig) obj;

		// Get credentials from the current context
		HP3ParCredentials c = new HP3ParCredentials(context);
		// TODO this is broken - it will delete anything not included - should
		// create an add method
		HP3ParRequestStatus s = HP3ParHostSetExecute.remove(c, config);

		// Throwing an exception fails the submit and shows the error in the
		// window
		if (!s.isSuccess()) {
			logger.warn("Failed to remove Host:" + s.getError());
			throw new HP3ParSetException("Failed to remove Host: " + s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Host removed OK");
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
		return true;
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
