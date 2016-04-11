package com.cisco.matday.ucsd.hp3par.tasks.volumes;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeRestCall;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.HP3ParVolumeParams;

public class HP3ParVolumeExecute {
	private static Logger logger = Logger.getLogger(HP3ParVolumeExecute.class);

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

}
