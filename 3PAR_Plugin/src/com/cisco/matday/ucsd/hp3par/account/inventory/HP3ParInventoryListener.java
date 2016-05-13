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
package com.cisco.matday.ucsd.hp3par.account.inventory;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParAccountDBStore;
import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.util.HP3ParAccountPersistenceUtil;
import com.cloupia.lib.connector.InventoryContext;
import com.cloupia.lib.connector.InventoryEventListener;
import com.cloupia.lib.connector.account.AccountTypeEntry;
import com.cloupia.lib.connector.account.PhysicalAccountManager;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;

/**
 * Boilerplate from SDK - not sure what it does
 *
 * @author Matt Day
 *
 */
public class HP3ParInventoryListener implements InventoryEventListener {
	private static Logger logger = Logger.getLogger(HP3ParInventoryListener.class);

	@Override
	public void afterInventoryDone(String accountName, InventoryContext ctx) throws Exception {
		logger.info("Completed HP 3PAR inventory collection...");
		HP3ParAccountPersistenceUtil.persistCollectedInventory(accountName);

		final AccountTypeEntry entry = PhysicalAccountManager.getInstance().getAccountTypeEntryByName(accountName);
		PhysicalConnectivityStatus connectivityStatus = null;
		if (entry != null) {
			connectivityStatus = entry.getTestConnectionHandler().testConnection(accountName);
		}

		final HP3ParAccountDBStore acc = HP3ParCredentials.getInternalCredential(accountName);

		if ((acc != null) && (connectivityStatus != null)) {
			logger.info("Inventory collected successfully");
		}

	}

	@Override
	public void beforeInventoryStart(String accountName, InventoryContext ctx) throws Exception {
		logger.info("Running HP 3PAR inventory collection...");

	}

}
