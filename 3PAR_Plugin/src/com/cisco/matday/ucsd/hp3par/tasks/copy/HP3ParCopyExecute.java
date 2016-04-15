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
import com.cisco.matday.ucsd.hp3par.rest.copy.HP3ParCopyRestCall;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParCopyParams;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParSnapshotParams;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;

/**
 * Provides the task logic to perform copy actions on a 3PAR array for both
 * tasks and action buttons
 * 
 * @author matt
 *
 */
public class HP3ParCopyExecute {
	private static Logger logger = Logger.getLogger(HP3ParCopyExecute.class);

	/**
	 * Copy an existing volume
	 * 
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws Exception
	 *             if the operation was unsuccessful
	 */
	public static HP3ParRequestStatus copy(HP3ParCredentials c, CreateVolumeCopyConfig config) throws Exception {

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
			if (copyCpgInfo.length == 3) {
				copyCpgName = copyCpgInfo[2];
			}
		}

		// Build copy parameter list:
		HP3ParCopyParams p = new HP3ParCopyParams(config.getNewVolumeName(), cpgName, config.isOnline(),
				config.isThinProvision(), copyCpgName);

		return HP3ParCopyRestCall.createCopy(c, volName, p);

	}

	/**
	 * Snapshot an existing volume
	 * 
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws Exception
	 *             if the operation was unsuccessful
	 */
	public static HP3ParRequestStatus snapshot(HP3ParCredentials c, CreateVolumeSnapshotConfig config)
			throws Exception {

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new Exception("Invalid Volume: " + config.getVolume());
		}
		String volName = volInfo[2];

		HP3ParSnapshotParams p = new HP3ParSnapshotParams(config.getSnapshotName(), config.isReadOnly(),
				config.getComment());

		return HP3ParCopyRestCall.createSnapshot(c, volName, p);

	}

}
