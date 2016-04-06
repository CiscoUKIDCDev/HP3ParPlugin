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
package com.cisco.matday.ucsd.hp3par.reports.volume.actions;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.copy.HP3ParCopyRestCall;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParCopyParams;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.tasks.copy.CreateVolumeCopyConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Action button implemenation to create a volume
 * 
 * @author Matt Day
 *
 */
public class CreateVolumeCopyAction extends CloupiaPageAction {

	private static Logger logger = Logger.getLogger(CreateVolumeCopyAction.class);

	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.CreateVolumeCopyForm";
	private static final String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.CreateVolumeCopyAction";
	private static final String LABEL = "Copy Volume";
	private static final String DESCRIPTION = "Copy Volume";

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Create Volume custom task
		page.bind(FORM_ID, CreateVolumeCopyConfig.class);
	}

	/**
	 * This sets up the initial fields and provides default values (in this case
	 * the account name)
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		CreateVolumeCopyConfig form = new CreateVolumeCopyConfig();
		/*
		 * Unlike CreateVolumeAction, this returns true on isSelectionRequired()
		 * 
		 * This means the context is whatever's in column 0 of the table. In
		 * this case it's in the format:
		 * 
		 * accountName;volumeName
		 */
		String account = query.split(";")[0];
		String volume = query.split(";")[1];

		// Pre-populate the account and volume fields:
		form.setAccount(account);
		form.setVolume(volume);

		// Set the account and volume fields to read-only (I couldn't find this
		// documented anywhere, maybe there's a better way to do it?)
		page.getFlist().getByFieldId(FORM_ID + ".account").setEditable(false);
		page.getFlist().getByFieldId(FORM_ID + ".volume").setEditable(false);

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
		CreateVolumeCopyConfig config = (CreateVolumeCopyConfig) obj;

		// Get credentials from the current context
		HP3ParCredentials c = new HP3ParCredentials(context);

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new Exception("Invalid Volume: " + config.getVolume());
		}
		String volName = volInfo[2];
		// Parse out CPG - it's in the format:
		// ID@AccountName@Name
		String[] cpgInfo = config.getCpg().split("@");
		if (cpgInfo.length != 3) {
			logger.warn("CPG didn't return three items! It returned: " + config.getCpg());
			throw new Exception("Invalid CPG");
		}
		String cpgName = cpgInfo[2];

		String copyCpgName = null;

		if (config.getCopyCpg() != null) {
			String[] copyCpgInfo = config.getCopyCpg().split("@");
			// Can leave the copy CPG as null if this errors out
			if (copyCpgInfo.length == 3) {
				copyCpgName = copyCpgInfo[2];
			}
		}

		// Build copy parameter list:
		HP3ParCopyParams p = new HP3ParCopyParams(config.getNewVolumeName(), cpgName, config.isOnline(),
				config.isThinProvision(), copyCpgName);

		HP3ParRequestStatus s = HP3ParCopyRestCall.createCopy(c, volName, p);

		// Throwing an exception fails the submit and shows the error in the
		// window
		if (!s.isSuccess()) {
			logger.warn("Failed to copy volume: " + s.getError());
			throw new Exception("Failed to copy volume: " + s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Volume " + config.getNewVolumeName() + " copied OK");
		
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
