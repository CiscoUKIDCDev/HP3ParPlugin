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

package com.cisco.matday.ucsd.hp3par.tasks.volumes;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeRestCall;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * Executes a task to delete a volume. This should not generally be instantiated
 * by anything other than UCS Director's internal libraries
 * 
 * @author Matt Day
 *
 */
public class DeleteVolumeTask extends AbstractTask {
	private static Logger logger = Logger.getLogger(DeleteVolumeTask.class);

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {

		// Obtain account information:
		DeleteVolumeConfig config = (DeleteVolumeConfig) context.loadConfigObject();
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new Exception("Invalid Volume: " + config.getVolume());
		}
		String volName = volInfo[2];

		// Delete the volume:
		HP3ParRequestStatus s = HP3ParVolumeRestCall.delete(c, volName);
		// If it wasn't deleted error out
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to delete Volume: " + s.getError());
			throw new Exception("Volume deletion failed");
		}
		ucsdLogger.addInfo("Deleted volume " + volName);

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new DeleteVolumeConfig();
	}

	@Override
	public String getTaskName() {
		return DeleteVolumeConfig.DISPLAY_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

}
