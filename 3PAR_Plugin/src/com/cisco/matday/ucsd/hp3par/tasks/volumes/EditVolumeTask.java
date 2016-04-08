package com.cisco.matday.ucsd.hp3par.tasks.volumes;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPGInfo;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeRestCall;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeEditParams;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * Executes a task to edit a volume. This should not generally be instantiated
 * by anything other than UCS Director's internal libraries
 * 
 * @author Matt Day
 *
 */
public class EditVolumeTask extends AbstractTask {
	private static Logger logger = Logger.getLogger(EditVolumeTask.class);

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {

		// Obtain account information:
		EditVolumeConfig config = (EditVolumeConfig) context.loadConfigObject();
		// Get credentials from the current context
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

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

		// If it wasn't deleted error out
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to edit Volume: " + s.getError());
			throw new Exception("Volume edit failed: " + s.getError());
		}
		ucsdLogger.addInfo("Edited volume " + volName);
		try {
			// Construct Volume name in the format:
			// id@Account@Volume
			// Don't know the volume so just use 0 as a workaround
			String tableVolName = "0@" + config.getAccount() + "@" + config.getNewVolumeName();
			context.saveOutputValue(HP3ParConstants.VOLUME_LIST_FORM_LABEL, tableVolName);
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not register output value " + HP3ParConstants.ACCOUNT_LIST_FORM_LABEL + ": "
					+ e.getMessage());
		}

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new EditVolumeConfig();
	}

	@Override
	public String getTaskName() {
		return EditVolumeConfig.DISPLAY_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {
				// Register output type for the volume created
				new TaskOutputDefinition(HP3ParConstants.VOLUME_LIST_FORM_LABEL,
						HP3ParConstants.VOLUME_LIST_FORM_TABLE_NAME, HP3ParConstants.VOLUME_LIST_FORM_LABEL),
		};
		return ops;
	}

}
