package com.cisco.matday.ucsd.hp3par.reports.actions;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.reports.drilldown.VolumeDetails;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

@SuppressWarnings("javadoc")
public class VolumeDoubleClickActionReport extends CloupiaPageAction {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(CreateVolumeAction.class);

	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.VolumeDoubleClickActionReport";
	private static final String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.VolumeDoubleClickActionReport";
	private static final String LABEL = "More info";
	private static final String DESCRIPTION = "More info";

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Create Volume custom task
		page.bind(FORM_ID, VolumeDetails.class);
	}

	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		// String query = context.getId();
		VolumeDetails form = new VolumeDetails();

		session.getSessionAttributes().put(FORM_ID, form);
		page.marshallFromSession(FORM_ID);
	}

	@Override
	public int validatePageData(Page page, ReportContext context, WizardSession session) throws Exception {
		// TODO Auto-generated method stub
		return PageIf.STATUS_OK;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ACTION_ID;
	}

	@Override
	public int getActionType() {
		return ConfigTableAction.ACTION_TYPE_DRILL_DOWN;
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
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getTitle() {
		return LABEL;
	}

	@Override
	public boolean isDoubleClickAction() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isDrilldownAction() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isSelectionRequired() {
		// TODO Auto-generated method stub
		return false;
	}

}
