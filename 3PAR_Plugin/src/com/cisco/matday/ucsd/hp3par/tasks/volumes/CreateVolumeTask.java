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

import com.cisco.matday.ucsd.hp3par.HP3ParModule;
import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.volumes.CreateVolumeRestCall;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeInformation;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeStatus;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

public class CreateVolumeTask extends AbstractTask {
	private static Logger logger = Logger.getLogger(HP3ParModule.class);

	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		// Obtain account information:
		CreateVolumeConfig config = (CreateVolumeConfig) context.loadConfigObject();
		HP3ParCredentials c = new HP3ParCredentials(config.getAccount());

		// Parse out CPG - it's in the format:
		// ID@AccountName@Name
		String[] cpgInfo = config.getCpg().split("@");
		if (cpgInfo.length != 3) {
			logger.warn("CPG didn't return three items! It returned: " + config.getCpg());
			throw new Exception("Invalid CPG");
		}
		String cpgName = cpgInfo[2];

		// Build volume information object:
		HP3ParVolumeInformation volume = new HP3ParVolumeInformation(config.getVolumeName(), cpgName,
				config.getVolume_size(), config.getComment(), config.isThin_provision());
		HP3ParVolumeStatus s = CreateVolumeRestCall.create(c, volume);

		// If it wasn't created error out
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to create volume:" + s.getError());
			throw new Exception("Failed to create volume");
		}

		ucsdLogger.addInfo("Created volume: " + config.getVolumeName());

		context.getChangeTracker().undoableResourceAdded("assetType", "idString", "Volume created",
				"Undo creation of volume: " + config.getVolumeName(), DeleteVolumeConfig.DISPLAY_LABEL,
				new DeleteVolumeConfig(config));

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new CreateVolumeConfig();
	}

	@Override
	public String getTaskName() {
		return CreateVolumeConfig.DISPLAY_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

}
