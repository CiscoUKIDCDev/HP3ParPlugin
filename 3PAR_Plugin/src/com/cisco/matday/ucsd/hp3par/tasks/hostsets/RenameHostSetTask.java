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
package com.cisco.matday.ucsd.hp3par.tasks.hostsets;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParSetException;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * Create host implementation task
 *
 * @author Matt Day
 *
 */
public class RenameHostSetTask extends AbstractTask {

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		RenameHostSetConfig config = (RenameHostSetConfig) context.loadConfigObject();
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		HP3ParRequestStatus s = HP3ParHostSetExecute.rename(c, config);

		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to rename host set: " + s.getError());
			throw new HP3ParSetException("Failed to rename host set: " + s.getError());
		}

		ucsdLogger.addInfo("Renamed host set");

		try {
			final String hostSetName = config.getHostSet().split(";")[1].split("@")[2];
			context.getChangeTracker().undoableResourceAdded("assetType", "idString", "Host set created",
					"Undo renaming of host set: " + config.getHostSetName(), RenameHostSetConfig.DISPLAY_LABEL,
					new RenameHostSetConfig(config, hostSetName));
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Failed to register undo task: " + e.getMessage());
		}

		try {
			// Construct Host name in the format:
			// id@Account@HosetSet
			// Don't know the volume so just use 0 as a workaround
			String hostName = c.getAccountName() + "0@" + config.getAccount() + "@" + config.getHostSetName()
					+ ";hostset";
			context.saveOutputValue(HP3ParConstants.HOSTSET_LIST_FORM_LABEL, hostName);

			String hostSetName = c.getAccountName() + "0@set@" + config.getHostSetName();
			context.saveOutputValue(HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_LABEL, hostSetName);
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Failed to register outputs: " + e.getMessage());
		}
	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new RenameHostSetConfig();
	}

	@Override
	public String getTaskName() {
		return RenameHostSetConfig.DISPLAY_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {
				// Register output type for the host set created
				new TaskOutputDefinition(HP3ParConstants.HOSTSET_LIST_FORM_LABEL,
						HP3ParConstants.HOSTSET_LIST_FORM_TABLE_NAME, HP3ParConstants.HOSTSET_LIST_FORM_LABEL),
				new TaskOutputDefinition(HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_LABEL,
						HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_TABLE_NAME,
						HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_LABEL)
		};
		return ops;
	}
}
