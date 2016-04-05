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
package com.cisco.matday.ucsd.hp3par.reports.actions;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeRestCall;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeInformation;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeStatus;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.CreateVolumeConfig;
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
public class CreateVolumeAction extends CloupiaPageAction {

	private static Logger logger = Logger.getLogger(CreateVolumeAction.class);

	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.CreateVolumeForm";
	private static final String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.CreateVolumeAction";
	private static final String LABEL = "Create Volume";
	private static final String DESCRIPTION = "Create a new Volume";

	@Override
	public void definePage(Page page, ReportContext context) {

		page.bind(FORM_ID, CreateVolumeConfig.class);
	}

	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {

		String query = context.getId();
		CreateVolumeConfig form = new CreateVolumeConfig();
		// The form will be in the format Account;Pod - grab the former:
		String account = query.split(";")[0];
		// Pre-populate the account field:
		form.setAccount(account);

		session.getSessionAttributes().put(FORM_ID, form);
		page.marshallFromSession(FORM_ID);
	}

	@Override
	public int validatePageData(Page page, ReportContext context, WizardSession session) throws Exception {
		// ObjStore<CreateVolumeConfig> store =
		// ObjStoreHelper.getStore(CreateVolumeConfig.class);

		Object obj = page.unmarshallToSession(FORM_ID);
		CreateVolumeConfig form = (CreateVolumeConfig) obj;

		HP3ParCredentials c = new HP3ParCredentials(context);

		String[] cpgInfo = form.getCpg().split("@");
		if (cpgInfo.length != 3) {
			logger.warn("CPG didn't return three items! It returned: " + form.getCpg());
			throw new Exception("Invalid CPG");
		}
		String cpgName = cpgInfo[2];

		// Build volume information object:
		HP3ParVolumeInformation volume = new HP3ParVolumeInformation(form.getVolumeName(), cpgName,
				form.getVolume_size(), form.getComment(), form.isThin_provision());
		HP3ParVolumeStatus s = HP3ParVolumeRestCall.create(c, volume);

		if (!s.isSuccess()) {
			logger.warn("Failed to create volume:" + s.getError());
			throw new Exception("Failed to create volume");
		}

		/*
		 * 
		 * CreateVolumeActionForm modded = null; List<CreateVolumeActionForm>
		 * objs = store.queryAll(); for (CreateVolumeActionForm o : objs) { if
		 * (o.getVlanID().equals(form.getVlanID())) {
		 * o.setGroupId(form.getGroupId()); modded = o; break; } }
		 * 
		 * if (modded != null) { store.modifySingleObject("vlanID == '" +
		 * form.getVlanID() + "'", modded); } else { store.insert(form); }
		 */

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
