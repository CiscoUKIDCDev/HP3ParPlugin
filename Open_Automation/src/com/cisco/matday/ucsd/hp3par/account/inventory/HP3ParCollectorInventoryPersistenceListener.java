package com.cisco.matday.ucsd.hp3par.account.inventory;

import com.cloupia.service.cIM.inframgr.collector.controller.PersistenceListener;
import com.cloupia.service.cIM.inframgr.collector.model.ItemResponse;

import org.apache.log4j.Logger;

/**
 * This is listener for persisting the inventory. 
 * 
 */
public class HP3ParCollectorInventoryPersistenceListener extends
		PersistenceListener {
	static Logger logger = Logger.getLogger(HP3ParCollectorInventoryPersistenceListener.class);
	
	@Override
	public void persistItem(ItemResponse arg0) throws Exception {
		logger.info("Persisting inventory...");
		return;
	}

}