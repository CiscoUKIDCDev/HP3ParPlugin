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
package com.cisco.matday.ucsd.hp3par.tasks.copy;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.copy.HP3ParCopyRestCall;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParCopyParams;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.DeleteVolumeConfig;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * Executes a task to copy a volume. This should not generally be instantiated
 * by anything other than UCS Director's internal libraries
 * 
 * @author Matt Day
 *
 */
public class CreateVolumeCopyTask extends AbstractTask {
	private static Logger logger = Logger.getLogger(CreateVolumeCopyTask.class);

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		// Obtain account information:
		CreateVolumeCopyConfig config = (CreateVolumeCopyConfig) context.loadConfigObject();
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new Exception("Invalid Volume: " + config.getVolume());
		}
		String volName = volInfo[2];
		// Parse out CPG - it's in the format:
		// ID@AccountName@Name
		String[] cpgInfo = config.getCpg().split("@");
		if (cpgInfo.length != 3) {
			logger.warn("CPG didn't return three items! It returned: " + config.getCpg());
			throw new Exception("Invalid CPG");
		}
		String cpgName = cpgInfo[2];

		String copyCpgName = null;

		if (config.getCopyCpg() != null) {
			String[] copyCpgInfo = config.getCopyCpg().split("@");
			if (copyCpgInfo.length != 3) {
				logger.warn("Copy CPG didn't return three items! It returned: " + config.getCopyCpg());
				throw new Exception("Invalid Copy CPG");
			}
			copyCpgName = copyCpgInfo[2];
		}

		// Build copy parameter list:
		HP3ParCopyParams p = new HP3ParCopyParams(config.getNewVolumeName(), cpgName, config.isOnline(),
				config.isThinProvision(), copyCpgName);

		HP3ParRequestStatus s = HP3ParCopyRestCall.createCopy(c, volName, p);

		// If it wasn't created error out
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to copy volume: " + s.getError());
			throw new Exception("Failed to copy volume" + s.getError());
		}
		
		ucsdLogger.addInfo("Copied volume: " + volName + " to "+ config.getNewVolumeName());

		/*
		 * // Build volume information object: HP3ParVolumeInformation volume =
		 * new HP3ParVolumeInformation(config.getVolumeName(), cpgName,
		 * config.getVolume_size(), config.getComment(),
		 * config.isThin_provision(), copyCpgName); HP3ParRequestStatus s =
		 * HP3ParVolumeRestCall.create(c, volume);
		 * 
		 * // If it wasn't created error out if (!s.isSuccess()) {
		 * ucsdLogger.addError("Failed to create volume:" + s.getError()); throw
		 * new Exception("Failed to create volume"); }
		 * 
		 * ucsdLogger.addInfo("Created volume: " + config.getVolumeName());
		 */
		context.getChangeTracker().undoableResourceAdded("assetType", "idString", "Volume created",
				"Undo creation of volume: " + config.getNewVolumeName(), DeleteVolumeConfig.DISPLAY_LABEL,
				new DeleteVolumeConfig(config));

		try {
			// Construct Volume name in the format:
			// id@Account@Volume
			// Don't know the volume so just use 0 as a workaround
			String newVolName = "0@" + config.getAccount() + "@" + config.getNewVolumeName();
			context.saveOutputValue(HP3ParConstants.VOLUME_LIST_FORM_LABEL, newVolName);
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not register output value " + HP3ParConstants.ACCOUNT_LIST_FORM_LABEL + ": "
					+ e.getMessage());
		}
	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new CreateVolumeCopyConfig();
	}

	@Override
	public String getTaskName() {
		return CreateVolumeCopyConfig.DISPLAY_LABEL;
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