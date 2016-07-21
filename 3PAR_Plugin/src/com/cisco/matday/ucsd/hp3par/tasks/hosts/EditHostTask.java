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
package com.cisco.matday.ucsd.hp3par.tasks.hosts;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParHostException;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * Edit a host
 *
 * @author Matt Day
 *
 */
public class EditHostTask extends AbstractTask {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(EditHostTask.class);

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {

		// Obtain account information:
		EditHostConfig config = (EditHostConfig) context.loadConfigObject();
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		// Edit the Host:
		HP3ParRequestStatus s = HP3ParHostExecute.edit(c, config);
		// If it wasn't edited error out
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to edit Host: " + s.getError());
			throw new HP3ParHostException("Host deletion failed");
		}
		ucsdLogger.addInfo("Edited Host");
		try {
			final String hostName = config.getHost().split(";")[1].split("@")[2];
			context.getChangeTracker().undoableResourceAdded("assetType", "idString", "Host edited",
					"Undo editing of host: " + config.getNewName(), EditHostConfig.DISPLAY_LABEL,
					new EditHostConfig(config, hostName));
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Failed to register undo task: " + e.getMessage());
		}

		try {
			// Construct Host name in the format:
			// id@Account@Volume
			// Don't know the volume so just use 0 as a workaround
			String hostName = config.getAccount() + ";0@" + config.getAccount() + "@" + config.getNewName();
			context.saveOutputValue(HP3ParConstants.HOST_LIST_FORM_LABEL, hostName);

			String hostSetName = config.getAccount() + ";0@host@" + config.getNewName();
			context.saveOutputValue(HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_LABEL, hostSetName);
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Failed to register outputs: " + e.getMessage());
		}

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new EditHostConfig();
	}

	@Override
	public String getTaskName() {
		return EditHostConfig.DISPLAY_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {
				// Register output type for the volume created
				new TaskOutputDefinition(HP3ParConstants.HOST_LIST_FORM_LABEL,
						HP3ParConstants.HOST_LIST_FORM_TABLE_NAME, HP3ParConstants.HOST_LIST_FORM_LABEL),

				new TaskOutputDefinition(HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_LABEL,
						HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_TABLE_NAME,
						HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_LABEL)
		};
		return ops;
	}

}
