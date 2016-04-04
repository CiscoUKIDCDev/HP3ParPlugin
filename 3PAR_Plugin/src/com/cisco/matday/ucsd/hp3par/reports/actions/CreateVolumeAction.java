package com.cisco.matday.ucsd.hp3par.reports.actions;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.tasks.volumes.CreateVolumeConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

public class CreateVolumeAction extends CloupiaPageAction {

	private static Logger logger = Logger.getLogger(CreateVolumeAction.class);

	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.CreateVolumeForm";
	private static final String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.CreateVolumeAction";
	private static final String LABEL = "Create Volume";
	private static final String DESCRIPTION = "Create a new Volume";

	/**
	 * this is where you define the layout of the form page the easiest way to
	 * do this is to use this "bind" method
	 * 
	 * @param pagecontext
	 * @param reportcontext
	 */
	@Override
	public void definePage(Page page, ReportContext context) {

		page.bind(FORM_ID, CreateVolumeConfig.class);
	}

	/**
	 * This method loads the form fields and field data to the page.
	 *
	 * @param pagecontext
	 * @param reportcontext
	 * @param wizardsession
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {

		String query = context.getId();
		logger.info("Action button query = " + query);

		CreateVolumeConfig form = new CreateVolumeConfig();
		// form.setVlanID(query);

		session.getSessionAttributes().put(FORM_ID, form);
		page.marshallFromSession(FORM_ID);
	}

	@Override
	public int validatePageData(Page page, ReportContext context, WizardSession session) throws Exception {
		//ObjStore<CreateVolumeConfig> store = ObjStoreHelper.getStore(CreateVolumeConfig.class);

		Object obj = page.unmarshallToSession(FORM_ID);
		CreateVolumeConfig form = (CreateVolumeConfig) obj;
		
		logger.info("Submit succeded!");
		logger.info("Volume name: " + form.getVolumeName());
		
		
		/*

		CreateVolumeActionForm modded = null;
		List<CreateVolumeActionForm> objs = store.queryAll();
		for (CreateVolumeActionForm o : objs) {
			if (o.getVlanID().equals(form.getVlanID())) {
				o.setGroupId(form.getGroupId());
				modded = o;
				break;
			}
		}

		if (modded != null) {
			store.modifySingleObject("vlanID == '" + form.getVlanID() + "'", modded);
		} else {
			store.insert(form);
		}
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
