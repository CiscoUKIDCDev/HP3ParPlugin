package com.cisco.matday.ucsd.hp3par.reports.volume.actions;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPGInfo;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeRestCall;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeEditParams;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.EditVolumeConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Creates an action button to edit a volume
 * 
 * @author Matt Day
 *
 */
public class EditVolumeAction extends CloupiaPageAction {

	private static Logger logger = Logger.getLogger(CreateVolumeCopyAction.class);

	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.EditVolumeActionForm";
	private static final String ACTION_ID = "com.cisco.matday.ucsd.hp3par.reports.actions.EditVolumeActionAction";
	private static final String LABEL = "Edit Volume";
	private static final String DESCRIPTION = "Edit Volume";

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Create Volume custom task
		page.bind(FORM_ID, EditVolumeConfig.class);
	}

	/**
	 * This sets up the initial fields and provides default values (in this case
	 * the account name)
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		EditVolumeConfig form = new EditVolumeConfig();
		/*
		 * Unlike CreateVolumeAction, this returns true on isSelectionRequired()
		 * 
		 * This means the context is whatever's in column 0 of the table. In
		 * this case it's in the format:
		 * 
		 * accountName;volumeName;cpgName;copyCpgName
		 */
		String volume = query.split(";")[1];

		// Populate the copy CPG field if it's already set
		String copyCpg = query.split(";")[3];
		if (!copyCpg.equals("-")) {
			// Have to do an API lookup as we need the ID which isn't in the
			// volume REST response:
			HP3ParCredentials login = new HP3ParCredentials(context);
			CPGResponseMember cpg = new HP3ParCPGInfo(login, copyCpg).getMember();
			// Build it in the format for the CPG table:
			// ID@AccountName@CPGName
			copyCpg = cpg.getId() + "@" + login.getAccountName() + "@" + cpg.getName();
			form.setCopyCpg(copyCpg);
		}

		// Pre-populate the account, volume and CPG fields:
		form.setVolume(volume);

		// Pre-populate volume name
		String volumeName = volume.split("@")[2];
		form.setNewVolumeName(volumeName);

		// Set the account and volume fields to read-only (I couldn't find this
		// documented anywhere, maybe there's a better way to do it?)
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
		EditVolumeConfig config = (EditVolumeConfig) obj;

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

		String newVolName = null;

		// If the volume name hasn't changed, set it to null else 3PAR gives an
		// error
		if (!config.getNewVolumeName().equals(volName)) {
			newVolName = config.getNewVolumeName();
		}

		String copyCpgName = null;

		if (config.getCopyCpg() != null) {
			String[] copyCpgInfo = config.getCopyCpg().split("@");
			// Can leave the copy CPG as null if this errors out
			if (copyCpgInfo.length == 3) {
				copyCpgName = copyCpgInfo[2];
			}
		}

		// If the new copy CPG name is the same as the old one, set it to
		// null (3PAR will otherwise return an error)
		if ((!copyCpgName.equals("")) && (!copyCpgName.equals("-"))) {
			CPGResponseMember cpg = new HP3ParCPGInfo(c, copyCpgName).getMember();
			if (cpg.getName().equals(copyCpgName)) {
				logger.debug("Edited CPG is the same as the old one, setting to null");
				copyCpgName = null;
			}
		}

		// Build copy parameter list:
		HP3ParVolumeEditParams p = new HP3ParVolumeEditParams(newVolName, null, config.getComment(), copyCpgName);

		HP3ParRequestStatus s = HP3ParVolumeRestCall.edit(c, volName, p);

		// Throwing an exception fails the submit and shows the error in the
		// window
		if (!s.isSuccess()) {
			logger.warn("Failed to edit volume: " + s.getError());
			throw new Exception("Failed to edit volume: " + s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Volume edited OK");

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
