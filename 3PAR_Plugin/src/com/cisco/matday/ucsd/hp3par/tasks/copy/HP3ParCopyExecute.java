package com.cisco.matday.ucsd.hp3par.tasks.copy;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.copy.HP3ParCopyRestCall;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParCopyParams;
import com.cisco.matday.ucsd.hp3par.rest.copy.json.HP3ParSnapshotParams;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;

public class HP3ParCopyExecute {
	private static Logger logger = Logger.getLogger(HP3ParCopyExecute.class);

	public static HP3ParRequestStatus copy (HP3ParCredentials c, CreateVolumeCopyConfig config) throws Exception {

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
	
	public static HP3ParRequestStatus snapshot (HP3ParCredentials c, CreateVolumeSnapshotConfig config) throws Exception {

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
