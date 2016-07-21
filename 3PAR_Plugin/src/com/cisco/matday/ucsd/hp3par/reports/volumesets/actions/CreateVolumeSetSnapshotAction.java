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
package com.cisco.matday.ucsd.hp3par.reports.volumesets.actions;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParVolumeException;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.tasks.volumesets.CreateVolumeSetSnapshotConfig;
import com.cisco.matday.ucsd.hp3par.tasks.volumesets.HP3ParVolumeSetExecute;
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
public class CreateVolumeSetSnapshotAction extends CloupiaPageAction {

	private static Logger logger = Logger.getLogger(CreateVolumeSetSnapshotAction.class);

	// need to provide a unique string to identify this form and action
	private final static String PREFIX = "com.cisco.matday.ucsd.hp3par.reports.volume.actions.CreateVolumeSetSnapshotAction";
	private String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.CreateVolumeSetSnapshotForm";
	private String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.CreateVolumeSetSnapshotAction";
	private static final String LABEL = "Snapshot";
	private static final String DESCRIPTION = "Create Snapshot";

	private boolean selection;
	private boolean needsContext;

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Create Volume custom task
		page.bind(this.FORM_ID, CreateVolumeSetSnapshotConfig.class);
	}

	/**
	 * Create a volume snapshot (with defaults)
	 */
	public CreateVolumeSetSnapshotAction() {
		this.init(true, true);
	}

	/**
	 * Create a volume snapshot (with defaults)
	 *
	 * @param selection
	 * @param needsContext
	 */
	public CreateVolumeSetSnapshotAction(boolean selection, boolean needsContext) {
		this.init(selection, needsContext);
	}

	private void init(boolean sel, boolean context) {
		this.selection = sel;
		this.needsContext = context;
		this.FORM_ID = PREFIX + this.needsContext + this.selection;
		this.ACTION_ID = this.FORM_ID + "_ACTION";
	}

	/**
	 * This sets up the initial fields and provides default values (in this case
	 * the account name)
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		CreateVolumeSetSnapshotConfig form = new CreateVolumeSetSnapshotConfig();
		if (this.needsContext) {
			// Pre-populate the account and volume fields:
			form.setVolumeSet(query);

			// Set the account and volume fields to read-only (I couldn't find
			// this
			// documented anywhere, maybe there's a better way to do it?)
			page.getFlist().getByFieldId(this.FORM_ID + ".volumeSet").setEditable(false);
		}

		form.setSnapshotName("@vvname@-@G@-@m@-@d@");

		session.getSessionAttributes().put(this.FORM_ID, form);
		page.marshallFromSession(this.FORM_ID);

	}

	/**
	 * This should do basic error checking (UCSD will enforce mandatory fields)
	 * and attempt to execute the task.
	 *
	 * Throwing an exception here will show the message as an error dialogue.
	 */
	@Override
	public int validatePageData(Page page, ReportContext context, WizardSession session) throws Exception {
		Object obj = page.unmarshallToSession(this.FORM_ID);
		CreateVolumeSetSnapshotConfig config = (CreateVolumeSetSnapshotConfig) obj;

		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		HP3ParRequestStatus s = HP3ParVolumeSetExecute.snapshot(c, config);

		// Throwing an exception fails the submit and shows the error in the
		// window
		if (!s.isSuccess()) {
			logger.warn("Failed to snapshot volume set: " + s.getError());
			throw new HP3ParVolumeException("Failed to snapshot volume set: " + s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Volume set " + config.getSnapshotName() + " snapshotted OK");

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
		return this.selection;
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
		return DESCRIPTION;
	}

	@Override
	public String getFormId() {
		return this.FORM_ID;
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
