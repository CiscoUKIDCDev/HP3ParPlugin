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
package com.cisco.matday.ucsd.hp3par.tasks.hosts;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.tasks.copy.CreateVolumeCopyConfig;
import com.cisco.matday.ucsd.hp3par.tasks.cpg.HP3ParHostExecute;
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
public class CreateHostTask extends AbstractTask {

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		CreateHostConfig config = (CreateHostConfig) context.loadConfigObject();
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		// HP3ParRequestStatus s = HP3ParCopyExecute.copy(c, config);
		HP3ParRequestStatus s = HP3ParHostExecute.create(c, config);

		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to create host: " + s.getError());
			throw new Exception("Failed to create host" + s.getError());
		}

		ucsdLogger.addInfo("Created host");

		context.getChangeTracker().undoableResourceAdded("assetType", "idString", "Volume created",
				"Undo creation of volume: " + config.getHostName(), DeleteHostConfig.DISPLAY_LABEL,
				new DeleteHostConfig(config));

		// Construct Volume name in the format:
		// id@Account@Volume
		// Don't know the volume so just use 0 as a workaround
		String hostName = "0@" + config.getAccount() + "@" + config.getHostName();
		context.saveOutputValue(HP3ParConstants.VOLUME_LIST_FORM_LABEL, hostName);
	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new CreateVolumeCopyConfig();
	}

	@Override
	public String getTaskName() {
		return CreateHostConfig.DISPLAY_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {
				// Register output type for the volume created
				new TaskOutputDefinition(HP3ParConstants.HOST_LIST_FORM_LABEL,
						HP3ParConstants.HOST_LIST_FORM_TABLE_NAME, HP3ParConstants.HOST_LIST_FORM_LABEL)
		};
		return ops;
	}
}