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
package com.cisco.matday.ucsd.hp3par.reports.cpg.actions;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParCpgException;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.tasks.cpg.DeleteCpgConfig;
import com.cisco.matday.ucsd.hp3par.tasks.cpg.HP3ParCpgExecute;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Action button implemenation to delete a cpg based on context
 *
 * @author Matt Day
 *
 */
public class DeleteCpgAction extends CloupiaPageAction {

	private static Logger logger = Logger.getLogger(DeleteCpgAction.class);

	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.DeleteCpgForm";
	private static final String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.DeleteCpgAction";
	private static final String LABEL = "Delete";
	private static final String DESCRIPTION = "Delete Cpg";

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Delete Cpg custom task
		page.bind(FORM_ID, DeleteCpgConfig.class);
	}

	/**
	 * This sets up the initial fields and provides default values (in this case
	 * the account and cpg names)
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {

		String query = context.getId();
		DeleteCpgConfig form = new DeleteCpgConfig();

		/*
		 * Unlike CreateCpgAction, this returns true on isSelectionRequired()
		 *
		 * This means the context is whatever's in column 0 of the table. In
		 * this case it's in the format:
		 *
		 * accountName;cpgName
		 */
		String cpg = query.split(";")[1];

		// Pre-populate the account and cpg fields:
		form.setCpg(cpg);

		// Set the account and cpg fields to read-only (I couldn't find this
		// documented anywhere, maybe there's a better way to do it?)
		page.getFlist().getByFieldId(FORM_ID + ".cpg").setEditable(false);

		session.getSessionAttributes().put(FORM_ID, form);
		page.marshallFromSession(FORM_ID);
	}

	/**
	 * This should do basic error checking (UCSD will enforce mandatory fields)
	 * and attempt to execute the task.
	 *
	 * Throwing an exception here will show the message as an error dialogue.
	 */
	@Override
	public int validatePageData(Page page, ReportContext context, WizardSession session) throws Exception {
		Object obj = page.unmarshallToSession(FORM_ID);
		DeleteCpgConfig config = (DeleteCpgConfig) obj;

		// Get credentials from the current context
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		// Delete the cpg:
		HP3ParRequestStatus s = HP3ParCpgExecute.delete(c, config);

		// If it wasn't deleted error out
		if (!s.isSuccess()) {
			logger.warn("Failed to delete Cpg: " + s.getError());
			// The exception warning here is used as the failure message
			throw new HP3ParCpgException("Cpg deletion failed: " + s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Deleted cpg");
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
		return ACTION_ID;
	}

	@Override
	public int getActionType() {
		return ConfigTableAction.ACTION_TYPE_POPUP_FORM;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getFormId() {
		return FORM_ID;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public String getTitle() {
		return LABEL;
	}

}
