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
package com.cisco.matday.ucsd.hp3par.tasks.vluns;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParVlunException;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * Create a VLUN
 *
 * @author Matt Day
 *
 */
public class CreateVlunTask extends AbstractTask {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(CreateVlunTask.class);

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {

		// Obtain account information:
		CreateVlunConfig config = (CreateVlunConfig) context.loadConfigObject();
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		// Create the VLUN
		HP3ParRequestStatus s = HP3ParVlunExecute.create(c, config);
		// If it wasn't createderror out
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to create VLUN: " + s.getError());
			throw new HP3ParVlunException("VLUN creation failed");
		}
		ucsdLogger.addInfo("Created VLUN");

		// Configure rollback
		context.getChangeTracker().undoableResourceAdded("assetType", "idString", "VLUN created",
				"Undo creation of VLUN", DeleteVlunConfig.DISPLAY_LABEL, new DeleteVlunConfig(config));

		// Register output
		try {
			// Construct VLUN name in the format:
			// Format: accountName;lun@accountName@volumeName@hostname
			final String hostName = config.getHost().split("@")[3];
			final String volumeName = config.getVolume().split("@")[2];

			String output = config.getAccount() + ";" + config.getLun() + "@" + config.getAccount() + "@" + volumeName
					+ "@" + hostName;
			context.saveOutputValue(HP3ParConstants.VLUN_LIST_FORM_LABEL, output);
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not register output value " + HP3ParConstants.ACCOUNT_LIST_FORM_LABEL + ": "
					+ e.getMessage());
		}

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new CreateVlunConfig();
	}

	@Override
	public String getTaskName() {
		return CreateVlunConfig.DISPLAY_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {
				// Register output type for the volume created
				new TaskOutputDefinition(HP3ParConstants.VLUN_LIST_FORM_LABEL,
						HP3ParConstants.VLUN_LIST_FORM_TABLE_NAME, HP3ParConstants.VLUN_LIST_FORM_LABEL),
		};
		return ops;
	}

}
