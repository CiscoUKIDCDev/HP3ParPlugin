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
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParCpgException;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * Executes a task to delete a cpg. This should not generally be instantiated by
 * anything other than UCS Director's internal libraries
 *
 * @author Matt Day
 *
 */
public class DeleteCpgTask extends AbstractTask {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(DeleteCpgTask.class);

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws HP3ParCpgException, Exception {

		// Obtain account information:
		DeleteCpgConfig config = (DeleteCpgConfig) context.loadConfigObject();
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		// Delete the cpg:
		HP3ParRequestStatus s = HP3ParCpgExecute.delete(c, config);
		// If it wasn't deleted error out
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to delete Cpg: " + s.getError());
			throw new HP3ParCpgException("Cpg deletion failed");
		}
		ucsdLogger.addInfo("Deleted cpg");

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new DeleteCpgConfig();
	}

	@Override
	public String getTaskName() {
		return DeleteCpgConfig.DISPLAY_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

}
