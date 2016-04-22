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

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPGList;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponse;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.hosts.HP3ParHostList;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponse;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.system.json.SystemResponse;
import com.cisco.matday.ucsd.hp3par.rest.vluns.HP3ParVlunList;
import com.cisco.matday.ucsd.hp3par.rest.vluns.rest.VlunResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;
import com.cloupia.fw.objstore.ObjStore;
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

	private HP3ParInventoryDBStore invStore = null;

	/**
	 * Create a new inventory instance
	 *
	 * @param accountName
	 * @throws Exception
	 *
	 */
	private HP3ParInventory(String accountName) throws Exception {
		final String queryString = "accountName == '" + accountName + "'";
		try {

			ObjStore<HP3ParInventoryDBStore> invStoreCollection = ObjStoreHelper.getStore(HP3ParInventoryDBStore.class);

			for (HP3ParInventoryDBStore store : invStoreCollection.query(queryString)) {
				if (accountName.equals(store.getAccountName())) {
					this.invStore = store;
				}
			}
		}
		catch (Exception e) {
			logger.warn("Exeption when doing this! " + e.getMessage());
		}
		if (this.invStore == null) {
			logger.warn("No account found! Attempting to create and store");
			this.create(accountName);
		}

	}

	private void create(String accountName) {
		logger.info("Creating persistent store for account " + accountName);
		try {
			ObjStore<HP3ParInventoryDBStore> invStoreCollection = ObjStoreHelper.getStore(HP3ParInventoryDBStore.class);
			logger.info("Creating new data store: " + accountName);
			this.invStore = new HP3ParInventoryDBStore(accountName);
			invStoreCollection.insert(this.invStore);
		}
		catch (Exception e) {

			logger.info("Exeption when doing this! " + e.getMessage());
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

		try {

			ObjStore<HP3ParInventoryDBStore> invStoreCollection = ObjStoreHelper.getStore(HP3ParInventoryDBStore.class);
			HP3ParInventoryDBStore store = null;
			try {
				store = invStoreCollection.query(queryString).iterator().next();
			}
			catch (Exception e) {
				logger.warn("Possibly stale entry from older API - deleting & re-creating. " + e.getMessage());
				invStoreCollection.delete(queryString);
				this.create(accountName);
				return;
			}

			if (store == null) {
				logger.warn("Cannot find " + accountName + " in inventory! Rolling back and creating new");
				// Attempt to create it:
				this.create(accountName);
				return;
			}
			final Date d = new Date();
			final long c = d.getTime();
			if ((!force) && ((c - store.getUpdated()) < HP3ParConstants.INVENTORY_LIFE)) {
				return;
			}
			logger.info("Updating persistent store for account " + accountName);
			store.setUpdated(c);

			HP3ParCredentials login = new HP3ParCredentials(store.getAccountName());

			final HP3ParVolumeList volumeList = new HP3ParVolumeList(login);
			store.setVolumeListJson(volumeList.toJson());

			final HP3ParSystem systemInfo = new HP3ParSystem(login);
			store.setSysInfoJson(systemInfo.toJson());

			final HP3ParCPGList cpg = new HP3ParCPGList(login);
			store.setCpgListJson(cpg.toJson());

			final HP3ParHostList host = new HP3ParHostList(login);
			store.setHostListJson(host.toJson());

			final HP3ParVlunList vlun = new HP3ParVlunList(login);
			store.setVlunListJson(vlun.toJson());

			this.invStore = store;
			invStoreCollection.modifySingleObject(queryString, this.invStore);

		}
		catch (Exception e) {
			logger.warn("Exception updating database! " + e.getMessage());
		}
	}

	private VolumeResponse getVol() throws Exception {
		return new HP3ParVolumeList(this.getStore().getVolumeListJson()).getVolume();
	}

	private VlunResponse getVlun() throws Exception {
		return new HP3ParVlunList(this.getStore().getVlunListJson()).getVlun();
	}

	private CPGResponse getCpg() throws Exception {
		return new HP3ParCPGList(this.getStore().getCpgListJson()).getCpg();
	}

	private SystemResponse getSys() throws Exception {
		return new HP3ParSystem(this.getStore().getSysInfoJson()).getSystem();
	}

	private HostResponse getHost() throws Exception {
		return new HP3ParHostList(this.getStore().getHostListJson()).getHost();
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

		for (final CPGResponseMember i : inv.getCpg().getMembers()) {
			if (cpgName.equals(i.getName())) {
				return i;
			}
		}
		logger.warn("CPG not found in cache: " + cpgName);
		return null;
	}

	/**
	 * Return information on a specific Host
	 *
	 * @param accountName
	 * @param hostName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static HostResponseMember getHostInfo(String accountName, String hostName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();

		for (final HostResponseMember i : inv.getHost().getMembers()) {
			if (hostName.equals(i.getName())) {
				return i;
			}
		}
		logger.warn("Host not found in cache: " + hostName);
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
	private HP3ParInventoryDBStore getStore() {
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
		return inv.getCpg();
	}

	/**
	 * Get the Host response data
	 *
	 * @param accountName
	 * @return host response list
	 * @throws Exception
	 */
	public synchronized static HostResponse getHostResponse(String accountName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();
		return inv.getHost();
	}

	/**
	 * Get the Host response data
	 *
	 * @param accountName
	 * @return host response list
	 * @throws Exception
	 */
	public synchronized static VlunResponse getVlunResponse(String accountName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();
		return inv.getVlun();
	}

	/**
	 * Deletes old persistent data and re-creates on startup
	 *
	 * @param accountName
	 * @throws Exception
	 */
	public synchronized static void init(String accountName) throws Exception {
		logger.info("Initialising inventory for account: " + accountName);
		final String queryString = "accountName == '" + accountName + "'";
		try {
			ObjStore<HP3ParInventoryDBStore> invStoreCollection = ObjStoreHelper.getStore(HP3ParInventoryDBStore.class);
			logger.warn("Deleting old data");
			invStoreCollection.delete(queryString);
		}
		catch (Exception e) {
			logger.info("Could not delete old data - maybe it doesn't exist. " + e.getMessage());
		}
		HP3ParInventory inv = new HP3ParInventory(accountName);
		inv.update();
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
