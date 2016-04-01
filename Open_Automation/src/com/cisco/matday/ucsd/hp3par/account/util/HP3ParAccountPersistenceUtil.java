package com.cisco.matday.ucsd.hp3par.account.util;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParAccount;
import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;

/**
 * This is the sample persistent util class for account. You can write your
 * methods to expose your persistence
 * 
 *
 */

public class HP3ParAccountPersistenceUtil {

	static Logger logger = Logger.getLogger(HP3ParAccountPersistenceUtil.class);

	public static void persistCollectedInventory(String accountName) throws Exception {
		logger.debug("Call in persistCollectedInventory :: inventory  ");
		logger.debug("Account Name " + accountName);

		HP3ParAccount account = HP3ParCredentials.getInternalCredential(accountName);

		if (account != null) {
			logger.debug("Remote Host Ip " + account.getServerAddress());
		}
	}

}
