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
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeRestCall;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeEditParams;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeParams;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;

/**
 * Provides the task logic to perform volume actions on a 3PAR array for both
 * tasks and action buttons
 *
 * @author matt
 *
 */
public class HP3ParVolumeExecute {
	private static Logger logger = Logger.getLogger(HP3ParVolumeExecute.class);

	/**
	 * Create a new volume
	 *
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws Exception
	 *             if the operation was unsuccessful
	 */
	public static HP3ParRequestStatus create(HP3ParCredentials c, CreateVolumeConfig config) throws Exception {
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

		// Build volume information object:
		HP3ParVolumeParams volume = new HP3ParVolumeParams(config.getVolumeName(), cpgName, config.getVolume_size(),
				config.getComment(), config.isThin_provision(), copyCpgName);
		return HP3ParVolumeRestCall.create(c, volume);

	}

	/**
	 * Delete an existing volume
	 *
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws Exception
	 *             if the operation was unsuccessful
	 */
	public static HP3ParRequestStatus delete(HP3ParCredentials c, DeleteVolumeConfig config) throws Exception {

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new Exception("Invalid Volume: " + config.getVolume());
		}
		String volName = volInfo[2];
		return HP3ParVolumeRestCall.delete(c, volName);

	}

	/**
	 * Edit a volume
	 *
	 * @param c
	 *            Credentials for the account to perform this on
	 * @param config
	 *            Configuration settings
	 * @return Status of the operation
	 * @throws Exception
	 *             if the operation was unsuccessful
	 */
	public static HP3ParRequestStatus edit(HP3ParCredentials c, EditVolumeConfig config) throws Exception {

		// Get the volume name, it's in the format:
		// id@account@name
		String[] volInfo = config.getVolume().split("@");
		if (volInfo.length != 3) {
			logger.warn("Volume didn't return three items! It returned: " + config.getVolume());
			throw new Exception("Invalid Volume: " + config.getVolume());
		}
		String volName = volInfo[2];

		String newVolName = null;

		// If the volume name hasn't changed, set it to null else 3PAR gives an
		// error
		if (!config.getNewVolumeName().equals(volName)) {
			newVolName = config.getNewVolumeName();
		}

		String copyCpgName = null;

		if (config.getCopyCpg() != null) {
			String[] copyCpgInfo = config.getCopyCpg().split("@");
			// Can leave the copy CPG as null if this errors out
			if (copyCpgInfo.length == 3) {
				copyCpgName = copyCpgInfo[2];
			}
		}

		CPGResponseMember cpg = HP3ParInventory.getCpgInfo(c.getAccountName(), copyCpgName);
		// VolumeResponseMember volinfo = new HP3ParVolumeInfo(c,
		// config.getOriginalName()).getMember();
		VolumeResponseMember volinfo = HP3ParInventory.getVolumeInfo(c.getAccountName(), config.getOriginalName());
		logger.info("Vol REST Lookup: " + volinfo.getUserCPG());
		logger.info("CPG REST lookup: " + cpg.getName());
		logger.info("Copy CPG Name: " + copyCpgName);

		// If the new copy CPG name is the same as the old one, set it to
		// null (3PAR will otherwise return an error)
		if ((!"".equals(copyCpgName)) && (!"-".equals(copyCpgName))) {
			if (volinfo.getUserCPG().equals(copyCpgName)) {
				logger.info("Edited CPG is the same as the old one, setting to null");
				copyCpgName = null;
			}
		}

		// Build copy parameter list:
		HP3ParVolumeEditParams p = new HP3ParVolumeEditParams(newVolName, null, config.getComment(), copyCpgName);

		return HP3ParVolumeRestCall.edit(c, volName, p);
	}

}
