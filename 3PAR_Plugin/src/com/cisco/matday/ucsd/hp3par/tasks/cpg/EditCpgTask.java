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

package com.cisco.matday.ucsd.hp3par.tasks.cpg;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParCpgException;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.tasks.volumesets.DeleteVolumeSetConfig;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * Executes a task to edit a cpg. This should not generally be instantiated by
 * anything other than UCS Director's internal libraries
 *
 * @author Matt Day
 *
 */
public class EditCpgTask extends AbstractTask {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(EditCpgTask.class);

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws HP3ParCpgException, Exception {

		// Obtain account information:
		EditCpgConfig config = (EditCpgConfig) context.loadConfigObject();
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		// Edit the cpg:
		HP3ParRequestStatus s = HP3ParCpgExecute.edit(c, config);
		// If it wasn't editd error out
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to edit Cpg: " + s.getError());
			throw new HP3ParCpgException("Cpg deletion failed");
		}
		ucsdLogger.addInfo("Edited cpg");

		try {
			final String cpgName = config.getCpg().split("@")[2];
			context.getChangeTracker().undoableResourceAdded("assetType", "idString", "Volume created",
					"Undo creation of volume: " + config.getNewName(), DeleteVolumeSetConfig.DISPLAY_LABEL,
					new EditCpgConfig(config, cpgName));
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not register undo task: " + e.getMessage());
		}
		try {
			// Construct Cpg name in the format:
			// id@Account@Volume
			// Don't know the volume so just use 0 as a workaround
			String cpgName = "0@" + config.getAccount() + "@" + config.getNewName();
			context.saveOutputValue(HP3ParConstants.CPG_LIST_FORM_LABEL, cpgName);
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not register outputs for task: " + e.getMessage());
		}
	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new EditCpgConfig();
	}

	@Override
	public String getTaskName() {
		return EditCpgConfig.DISPLAY_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {
				// Register output type for the volume created
				new TaskOutputDefinition(HP3ParConstants.CPG_LIST_FORM_LABEL, HP3ParConstants.CPG_LIST_FORM_TABLE_NAME,
						HP3ParConstants.CPG_LIST_FORM_LABEL),
		};
		return ops;
	}

}
