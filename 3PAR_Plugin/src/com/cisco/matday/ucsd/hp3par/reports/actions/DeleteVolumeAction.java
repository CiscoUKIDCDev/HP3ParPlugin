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
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeStatus;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.DeleteVolumeConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Action button implemenation to delete a volume based on context
 * 
 * @author Matt Day
 *
 */
public class DeleteVolumeAction extends CloupiaPageAction {

	private static Logger logger = Logger.getLogger(DeleteVolumeAction.class);

	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.DeleteVolumeForm";
	private static final String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.DeleteVolumeAction";
	private static final String LABEL = "Delete Volume";
	private static final String DESCRIPTION = "Delete Volume";

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form as the task
		page.bind(FORM_ID, DeleteVolumeConfig.class);
	}

	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {

		String query = context.getId();
		DeleteVolumeConfig form = new DeleteVolumeConfig();
		logger.info("3Par current pageFormID = " + page.getCurrentFormId());
		logger.info("3Par Query = " + query);

		// The query will be AccountName;VolumeName - grab them both
		String account = query.split(";")[0];
		String volume = query.split(";")[1];
		// Pre-populate the account field:
		form.setAccount(account);
		form.setVolume(volume);

		session.getSessionAttributes().put(FORM_ID, form);
		page.marshallFromSession(FORM_ID);
	}

	@Override
	public int validatePageData(Page page, ReportContext context, WizardSession session) throws Exception {
		// ObjStore<DeleteVolumeConfig> store =
		// ObjStoreHelper.getStore(DeleteVolumeConfig.class);

		Object obj = page.unmarshallToSession(FORM_ID);
		DeleteVolumeConfig form = (DeleteVolumeConfig) obj;

		logger.info("Submit succeded!");
		logger.info("Volume name: " + form.getVolume());

		HP3ParCredentials c = new HP3ParCredentials(form.getAccount());

		String[] volInfo = form.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + form.getVolume());
			throw new Exception("Invalid Volume: " + form.getVolume());
		}
		String volName = volInfo[2];

		// Delete the volume:
		HP3ParVolumeStatus s = HP3ParVolumeRestCall.delete(c, volName);
		// If it wasn't deleted error out
		if (!s.isSuccess()) {
			logger.warn("Failed to delete Volume: " + s.getError());
			throw new Exception("Volume deletion failed");
		}
		logger.info("Deleted volume " + volName);

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
