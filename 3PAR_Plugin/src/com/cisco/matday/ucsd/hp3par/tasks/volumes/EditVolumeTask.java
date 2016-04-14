package com.cisco.matday.ucsd.hp3par.tasks.volumes;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
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
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(EditVolumeTask.class);

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {

		// Obtain account information:
		EditVolumeConfig config = (EditVolumeConfig) context.loadConfigObject();
		// Get credentials from the current context
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		HP3ParRequestStatus s = HP3ParVolumeExecute.edit(c, config);

		// If it wasn't deleted error out
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to edit Volume: " + s.getError());
			throw new Exception("Volume edit failed: " + s.getError());
		}
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
