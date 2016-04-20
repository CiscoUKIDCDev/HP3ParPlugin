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

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPG;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponse;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.system.json.SystemResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;
import com.cloupia.fw.objstore.ObjStoreHelper;

/**
 * Manages inventory data including storing and managing it
 *
 * Everything should be accessed via the static members as they guarantee
 * releasing resources and writing back to the database
 *
 * @author Matt Day
 *
 */
public class HP3ParInventory {

	private static Logger logger = Logger.getLogger(HP3ParInventory.class);

	private HP3ParInventoryStore invStore = null;

	/**
	 * Create a new inventory instance
	 *
	 * @param accountName
	 * @throws Exception
	 *
	 */
	private HP3ParInventory(String accountName) throws Exception {
		logger.info("Opening persistent store for account: " + accountName);
		final String queryString = "accountName == '" + accountName + "'";
		final PersistenceManager pm = ObjStoreHelper.getPersistenceManager();
		pm.getFetchPlan().setFetchSize(HP3ParConstants.JDO_DEPTH);
		logger.info(
				"Fetch depth: " + pm.getFetchPlan().getFetchSize() + " (should be " + HP3ParConstants.JDO_DEPTH + ")");
		final Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Query query = pm.newQuery(HP3ParInventoryStore.class, queryString);
			// Suppress unchecked cast warnings here as the query has an
			// explicit class definition
			@SuppressWarnings("unchecked")
			Collection<HP3ParInventoryStore> invStoreCollection = (Collection<HP3ParInventoryStore>) query.execute();
			for (HP3ParInventoryStore store : invStoreCollection) {
				if (accountName.equals(store.getAccountName())) {
					logger.info("Account found: " + store.getAccountName());
					this.invStore = store;
				}
				else {
					logger.info("Found account (not going to use): " + store.getAccountName());
				}
			}
			tx.commit();
		}
		catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			logger.info("Exeption when doing this! " + e.getMessage());
		}
		finally {
			if (tx.isActive()) {
				tx.commit();
			}
			pm.close();
		}
		if (this.invStore == null) {
			logger.warn("No account found! Attempting to create and store");
			this.create(accountName);
		}

	}

	private void create(String accountName) {
		logger.info("Creating persistent store for account " + accountName);
		PersistenceManager pm = ObjStoreHelper.getPersistenceManager();
		pm.getFetchPlan().setFetchSize(HP3ParConstants.JDO_DEPTH);
		logger.info(
				"Fetch depth: " + pm.getFetchPlan().getFetchSize() + " (should be " + HP3ParConstants.JDO_DEPTH + ")");
		Transaction tx = pm.currentTransaction();
		try {
			logger.info("Creating new data store: " + accountName);
			this.invStore = new HP3ParInventoryStore(accountName);
			tx.begin();
			pm.makePersistent(this.invStore);
			tx.commit();
		}
		catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			logger.info("Exeption when doing this! " + e.getMessage());
		}
		finally {
			if (tx.isActive()) {
				tx.commit();
			}
			logger.info("Closing connection");
			pm.close();
		}
	}

	/**
	 * Update the inventory held in the database and optionally force a new
	 * query even if the data is not out of date
	 *
	 * @param force
	 *            set to true if the inventory should be refreshed even if it's
	 *            not timed out
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 * @throws Exception
	 */
	private void update(boolean force) throws Exception {
		final String accountName = this.invStore.getAccountName();
		final String queryString = "accountName == '" + accountName + "'";
		logger.info("Updating persistent store for account " + accountName);
		PersistenceManager pm = ObjStoreHelper.getPersistenceManager();
		pm.getFetchPlan().setFetchSize(HP3ParConstants.JDO_DEPTH);
		logger.info(
				"Fetch depth: " + pm.getFetchPlan().getFetchSize() + " (should be " + HP3ParConstants.JDO_DEPTH + ")");
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Query query = pm.newQuery(HP3ParInventoryStore.class, queryString);
			// Suppress unchecked cast warnings here as the query has an
			// explicit class definition
			@SuppressWarnings("unchecked")
			Collection<HP3ParInventoryStore> invStoreCollection = (Collection<HP3ParInventoryStore>) query.execute();
			HP3ParInventoryStore store = invStoreCollection.iterator().next();
			if (store == null) {
				logger.warn("Cannot find " + accountName + " in inventory! Rolling back and creating new");
				if (tx.isActive()) {
					tx.rollback();
				}
				// Attempt to create it:
				this.create(accountName);
				return;
			}
			final Date d = new Date();
			final long c = d.getTime();
			if ((!force) && ((c - store.getUpdated()) < HP3ParConstants.INVENTORY_LIFE)) {
				if (tx.isActive()) {
					tx.commit();
				}
				return;
			}
			store.setUpdated(c);

			final HP3ParVolumeList volumeList = new HP3ParVolumeList(new HP3ParCredentials(store.getAccountName()));
			store.setVolumeList(volumeList.getVolume());

			final HP3ParSystem systemInfo = new HP3ParSystem(new HP3ParCredentials(store.getAccountName()));
			store.setSysInfo(systemInfo.getSystem());

			final HP3ParCPG cpg = new HP3ParCPG(new HP3ParCredentials(store.getAccountName()));
			store.setCpgList(cpg.getCpg());
			this.invStore = store;
			tx.commit();
		}
		catch (Exception e) {
			logger.warn("Exception updating database! " + e.getMessage());
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		finally {
			if (tx.isActive()) {
				tx.commit();
			}
			pm.close();
		}
	}

	private VolumeResponse getVol() throws Exception {
		return this.getStore().getVolumeList();
	}

	private CPGResponse getCpg() throws Exception {
		return this.getStore().getCpgList();
	}

	private SystemResponse getSys() throws Exception {
		return this.getStore().getSysInfo();
	}

	/**
	 * Return information on a specific volume
	 *
	 * @param accountName
	 * @param volumeName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static final VolumeResponseMember getVolumeInfo(String accountName, String volumeName)
			throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();

		for (final VolumeResponseMember i : (inv.getVol().getMembers())) {
			if (volumeName.equals(i.getName())) {
				return i;
			}
		}
		logger.warn("Volume not found in cache: " + volumeName);
		return null;

	}

	/**
	 * Return information on a specific CPG
	 *
	 * @param accountName
	 * @param cpgName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static CPGResponseMember getCpgInfo(String accountName, String cpgName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();

		// Copy original
		// final Gson gson = new Gson();
		// CPGResponse copy = gson.fromJson(inv.getCpg().getJson(),
		// CPGResponse.class);

		// CPGResponse copy = inv.getCpg();

		for (final CPGResponseMember i : inv.getCpg().getMembers()) {
			if (cpgName.equals(i.getName())) {
				return i;
			}
		}
		logger.warn("CPG not found in cache: " + cpgName);
		return null;
	}

	/**
	 * Update the inventory held in the database if it is not out of date
	 *
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 * @throws Exception
	 */
	private void update() throws Exception {
		this.update(false);
	}

	/**
	 * @return raw db store
	 */
	private HP3ParInventoryStore getStore() {
		return this.invStore;
	}

	/**
	 * Get the volume response data
	 *
	 * @param accountName
	 * @return volume response list
	 * @throws Exception
	 */
	public synchronized static VolumeResponse getVolumeResponse(String accountName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();
		// Copy the object from original JSON
		return inv.getVol();
	}

	/**
	 * Get the system response data
	 *
	 * @param accountName
	 * @return volume response list
	 * @throws Exception
	 */
	public synchronized static SystemResponse getSystemResponse(String accountName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();
		return inv.getSys();
		// Copy the object from original JSON
		// final Gson gson = new Gson();
		// SystemResponse copy = gson.fromJson(inv.getSys().getJson(),
		// SystemResponse.class);
		// return copy;
	}

	/**
	 * Get the CPG response data
	 *
	 * @param accountName
	 * @return volume response list
	 * @throws Exception
	 */
	public synchronized static CPGResponse getCPGResponse(String accountName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();
		// final Gson gson = new Gson();
		// CPGResponse copy = gson.fromJson(inv.getCpg().getJson(),
		// CPGResponse.class);
		// return copy;
		return inv.getCpg();
	}

	/**
	 * Update from the 3PAR array to the local cache if it's timed out
	 *
	 * @param accountName
	 * @param force
	 *            - force the updates
	 * @throws Exception
	 */
	public synchronized static void update(String accountName, boolean force) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update(force);
	}
}
