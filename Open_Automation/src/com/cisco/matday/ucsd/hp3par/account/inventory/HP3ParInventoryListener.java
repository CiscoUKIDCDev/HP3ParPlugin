package com.cisco.matday.ucsd.hp3par.account.inventory;

import org.apache.log4j.Logger;

import com.cloupia.lib.connector.InventoryContext;
import com.cloupia.lib.connector.InventoryEventListener;

public class HP3ParInventoryListener implements InventoryEventListener {
	private static Logger logger = Logger.getLogger(HP3ParInventoryListener.class);

	@Override
	public void afterInventoryDone(String accountName, InventoryContext ctx) throws Exception {
		logger.info("Completed HP 3PAR inventory collection...");
		// TODO Auto-generated method stub
		

	}

	@Override
	public void beforeInventoryStart(String accountName, InventoryContext ctx) throws Exception {
		logger.info("Running HP 3PAR inventory collection...");
		// TODO Auto-generated method stub

	}

}
