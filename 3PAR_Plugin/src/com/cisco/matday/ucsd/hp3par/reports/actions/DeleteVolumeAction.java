package com.cisco.matday.ucsd.hp3par.reports.actions;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.volumes.DeleteVolumeRestCall;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeStatus;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.DeleteVolumeConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

public class DeleteVolumeAction extends CloupiaPageAction {

	private static Logger logger = Logger.getLogger(DeleteVolumeAction.class);

	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.DeleteVolumeForm";
	private static final String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.DeleteVolumeAction";
	private static final String LABEL = "Delete Volume";
	private static final String DESCRIPTION = "Delete Volume";

	/**
	 * this is where you define the layout of the form page the easiest way to
	 * do this is to use this "bind" method
	 * 
	 * @param pagecontext
	 * @param reportcontext
	 */
	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form as the task
		page.bind(FORM_ID, DeleteVolumeConfig.class);
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
		HP3ParVolumeStatus s = DeleteVolumeRestCall.delete(c, volName);
		// If it wasn't deleted error out
		if (!s.isSuccess()) {
			logger.warn("Failed to delete Volume: " + s.getError());
			throw new Exception("Volume deletion failed");
		}
		logger.info("Deleted volume " + volName);

		/*
		 * HP3ParCredentials c = new
		 * HP3ParCredentials(AccountUtil.getAccountByName(form.getAccount()));
		 * 
		 * String[] cpgInfo = form.getCpg().split("@"); if (cpgInfo.length != 3)
		 * { logger.warn("CPG didn't return three items! It returned: " +
		 * form.getCpg()); throw new Exception("Invalid CPG"); } String cpgName
		 * = cpgInfo[2];
		 * 
		 * // Build volume information object: HP3ParVolumeInformation volume =
		 * new HP3ParVolumeInformation(form.getVolumeName(), cpgName,
		 * form.getVolume_size(), form.getComment(), form.isThin_provision());
		 * HP3ParVolumeStatus s = DeleteVolumeRestCall.Delete(c, volume);
		 * 
		 * if (!s.isSuccess()) { logger.warn("Failed to Delete volume:" +
		 * s.getError()); throw new Exception("Failed to Delete volume"); }
		 * 
		 * logger.info("Deleted volume: " + form.getVolumeName());
		 */

		/*
		 * 
		 * DeleteVolumeActionForm modded = null; List<DeleteVolumeActionForm>
		 * objs = store.queryAll(); for (DeleteVolumeActionForm o : objs) { if
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
