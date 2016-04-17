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
import java.util.Date;
import java.util.List;

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
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.google.gson.Gson;

/**
 * Manages inventory data including storing and managing it
 *
 * @author Matt Day
 *
 */
public class HP3ParInventory {

	private static Logger logger = Logger.getLogger(HP3ParInventory.class);

	private HP3ParInventoryStore store = null;

	/**
	 * Create a new inventory instance
	 *
	 * @param accountName
	 * @throws Exception
	 */
	public HP3ParInventory(String accountName) throws Exception {
		final ObjStore<HP3ParInventoryStore> objStore = ObjStoreHelper.getStore(HP3ParInventoryStore.class);
		final List<HP3ParInventoryStore> invStore = objStore.query("accountName == '" + accountName + "'");

		logger.info("Persistent inventory data list query returned: " + invStore.size());

		for (final HP3ParInventoryStore i : invStore) {
			if (accountName.equals(i.getAccountName())) {
				logger.info("Found persistence: " + i.getAccountName());
				this.store = i;
				return;
			}
		}
		logger.info("Persistence not found. Creating new inventory for account " + accountName);
		this.store = new HP3ParInventoryStore(accountName);
		objStore.insert(this.store);
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
	public void update(boolean force) throws Exception {
		logger.info("Inventory update requested; forced: " + force);
		final Date d = new Date();
		final long c = d.getTime();
		if ((!force) && ((c - this.store.getUpdated()) < HP3ParConstants.INVENTORY_LIFE)) {
			logger.info("Inventory not out of date, so skipping update");
			return;
		}
		logger.info("Inventory out of date (or forced to update)");
		logger.info("Last update: " + this.store.getUpdated());
		logger.info("Time now: " + c);
		logger.info(
				"Difference = " + (c - this.store.getUpdated()) + " allowed delta = " + HP3ParConstants.INVENTORY_LIFE);

		this.store.setUpdated(c);
		logger.info("Pulling down volume inventory");
		final HP3ParVolumeList volumeList = new HP3ParVolumeList(new HP3ParCredentials(this.store.getAccountName()));
		this.store.setVolumeInfo(volumeList.toJson());
		logger.info("Pulling down system inventory");
		final HP3ParSystem systemInfo = new HP3ParSystem(new HP3ParCredentials(this.store.getAccountName()));
		this.store.setSysInfo(systemInfo.toJson());
		logger.info("System info name: " + systemInfo.getSystem().getName());
		logger.info("Pulling down cpg inventory");
		final HP3ParCPG cpg = new HP3ParCPG(new HP3ParCredentials(this.store.getAccountName()));
		this.store.setCpgInfo(cpg.toJson());

		final ObjStore<HP3ParInventoryStore> objStore = ObjStoreHelper.getStore(HP3ParInventoryStore.class);
		// Update store:
		logger.info("Storing updated inventory in persistent database");
		try {
			objStore.modifySingleObject("accountName == '" + this.store.getAccountName() + "'", this.store);
		}
		catch (Exception e) {
			logger.warn("Update failed: " + e.getMessage());
			throw new Exception(e);
		}

	}

	/**
	 * Return information on a specific volume
	 *
	 * @param accountName
	 * @param volumeName
	 * @return Volume information
	 * @throws Exception
	 */
	public static VolumeResponseMember getVolumeInfo(String accountName, String volumeName) throws Exception {
		logger.info("Searching cache for specific volume: " + volumeName);
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();

		Gson gson = new Gson();

		for (final VolumeResponseMember i : (gson.fromJson(inv.getStore().getVolumeInfo(), VolumeResponse.class))
				.getMembers()) {
			if (volumeName.equals(i.getName())) {
				logger.info("Found details on volume: " + volumeName);
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
	public static CPGResponseMember getCpgInfo(String accountName, String cpgName) throws Exception {
		logger.info("Searching cache for specific CPG: " + cpgName);
		HP3ParInventory inv = new HP3ParInventory(accountName);
		Gson gson = new Gson();

		inv.update();
		for (final CPGResponseMember i : (gson.fromJson(inv.getStore().getCpgInfo(), CPGResponse.class)).getMembers()) {
			if (cpgName.equals(i.getName())) {
				logger.info("Found details on CPG: " + cpgName);
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
	public void update() throws Exception {
		this.update(false);
	}

	/**
	 * Get the inventory for an account name
	 *
	 * @param accountName
	 *            Name of the account
	 * @return Inventory
	 * @throws Exception
	 */
	public static HP3ParInventoryStore get(String accountName) throws Exception {
		final ObjStore<HP3ParInventoryStore> store = ObjStoreHelper.getStore(HP3ParInventoryStore.class);
		final List<HP3ParInventoryStore> invStore = store.queryAll();
		HP3ParInventoryStore inv = null;

		for (final HP3ParInventoryStore i : invStore) {
			if (accountName.equals(i.getAccountName())) {
				logger.info("Found persistence: " + i.getAccountName());
				return i;
			}
		}
		logger.info("Creating new inventory for account " + accountName);
		inv = new HP3ParInventoryStore(accountName);
		store.insert(inv);
		return inv;
	}

	/**
	 * @return raw db store
	 */
	public HP3ParInventoryStore getStore() {
		return this.store;
	}

	/**
	 * Get the volume response data
	 *
	 * @param accountName
	 * @return volume response list
	 * @throws Exception
	 */
	public static VolumeResponse getVolumeResponse(String accountName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();
		Gson gson = new Gson();
		return gson.fromJson(inv.getStore().getVolumeInfo(), VolumeResponse.class);
	}

	/**
	 * Get the system response data
	 *
	 * @param accountName
	 * @return volume response list
	 * @throws Exception
	 */
	public static SystemResponse getSystemResponse(String accountName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();
		if (inv.getStore().getSysInfo() == null) {
			logger.warn("Warning! System info is still null! Running forced update");
			inv.update(true);
		}
		Gson gson = new Gson();
		return gson.fromJson(inv.getStore().getSysInfo(), SystemResponse.class);
	}

	/**
	 * Get the CPG response data
	 *
	 * @param accountName
	 * @return volume response list
	 * @throws Exception
	 */
	public static CPGResponse getCPGResponse(String accountName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();
		Gson gson = new Gson();
		return gson.fromJson(inv.getStore().getCpgInfo(), CPGResponse.class);
	}

	/**
	 * Update from the 3PAR array to the local cache if it's timed out
	 *
	 * @param accountName
	 * @param force
	 *            - force the updates
	 * @throws Exception
	 */
	public static void update(String accountName, boolean force) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update(force);
	}
}
