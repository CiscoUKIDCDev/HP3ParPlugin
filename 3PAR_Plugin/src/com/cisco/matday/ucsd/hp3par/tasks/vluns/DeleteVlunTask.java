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
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParVlunException;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * Delete a VLUN
 *
 * @author Matt Day
 *
 */
public class DeleteVlunTask extends AbstractTask {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(DeleteVlunTask.class);

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {

		ucsdLogger.addInfo("Starting log task");

		// Obtain account information:
		DeleteVlunConfig config = (DeleteVlunConfig) context.loadConfigObject();

		try {
			ucsdLogger.addWarning("LUN: " + config.getVlun());
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not get LUN: " + e.getMessage());
		}

		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		// Delete the VLUN
		HP3ParRequestStatus s = HP3ParVlunExecute.delete(c, config);
		// If it wasn't delted error out
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to delete VLUN: " + s.getError());
			throw new HP3ParVlunException("VLUN deletion failed");
		}
		ucsdLogger.addInfo("Deleted VLUN");

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new DeleteVlunConfig();
	}

	@Override
	public String getTaskName() {
		return DeleteVlunConfig.DISPLAY_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		return null;
	}

}
