package com.cisco.matday.ucsd.hp3par.account.inventory;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParAccount;
import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.util.HP3ParAccountPersistenceUtil;
import com.cloupia.lib.connector.InventoryContext;
import com.cloupia.lib.connector.InventoryEventListener;
import com.cloupia.lib.connector.account.AccountTypeEntry;
import com.cloupia.lib.connector.account.PhysicalAccountManager;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;

public class HP3ParInventoryListener implements InventoryEventListener {
	private static Logger logger = Logger.getLogger(HP3ParInventoryListener.class);

	@Override
	public void afterInventoryDone(String accountName, InventoryContext ctx) throws Exception {
		logger.info("Completed HP 3PAR inventory collection...");
		HP3ParAccountPersistenceUtil.persistCollectedInventory(accountName);

		AccountTypeEntry entry = PhysicalAccountManager.getInstance().getAccountTypeEntryByName(accountName);
		PhysicalConnectivityStatus connectivityStatus = null;
		if (entry != null) {
			connectivityStatus = entry.getTestConnectionHandler().testConnection(accountName);
		}

		HP3ParAccount acc = HP3ParCredentials.getInternalCredential(accountName);

		if (acc != null && connectivityStatus != null) {
			logger.info("Inventory collected successfully");
		}

	}

	@Override
	public void beforeInventoryStart(String accountName, InventoryContext ctx) throws Exception {
		logger.info("Running HP 3PAR inventory collection...");

	}

}
