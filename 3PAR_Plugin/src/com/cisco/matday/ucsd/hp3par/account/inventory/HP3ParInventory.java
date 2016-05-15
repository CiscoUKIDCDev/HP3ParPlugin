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

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.HP3ParToken;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPGList;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponse;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.hosts.HP3ParHostList;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponse;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.hostsets.HP3ParHostSetList;
import com.cisco.matday.ucsd.hp3par.rest.hostsets.json.HostSetResponse;
import com.cisco.matday.ucsd.hp3par.rest.hostsets.json.HostSetResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.ports.HP3ParPortList;
import com.cisco.matday.ucsd.hp3par.rest.ports.json.PortResponse;
import com.cisco.matday.ucsd.hp3par.rest.ports.json.PortResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.system.json.SystemResponse;
import com.cisco.matday.ucsd.hp3par.rest.vluns.HP3ParVlunList;
import com.cisco.matday.ucsd.hp3par.rest.vluns.rest.VlunResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;

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
	 * The constructor is private meaning you can only access this via the
	 * static members
	 *
	 * @param accountName
	 * @throws Exception
	 *
	 */
	private HP3ParInventory(HP3ParCredentials credentials) throws Exception {
		final String accountName = credentials.getAccountName();
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
			// Account was deleted most likely during init
			this.create(credentials);
		}

	}

	// Create a new inventory store in the DB (i.e. when creating a new account)
	private void create(HP3ParCredentials credentials) throws Exception {
		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(credentials.getAccountName());
		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(infraAccount);

		final String accountName = credentials.getAccountName();
		logger.info("Creating persistent store for account " + accountName);
		try {
			// Test token:
			HP3ParToken token = new HP3ParToken(credentials);
			if (token.getToken() == null) {
				status.setConnectionOK(false);
				logger.warn("Could not get valid token - marking connection as invalid");
				return;
			}
			token.release();
			ObjStore<HP3ParInventoryDBStore> invStoreCollection = ObjStoreHelper.getStore(HP3ParInventoryDBStore.class);
			logger.info("Creating new data store: " + accountName);
			this.invStore = new HP3ParInventoryDBStore(accountName);
			invStoreCollection.insert(this.invStore);
			status.setConnectionOK(true);
		}
		catch (Exception e) {
			status.setConnectionOK(false);
			logger.info("Exeption when doing this! " + e.getMessage());
		}
	}

	// Update account in the database - the boolean force means it will upgrade
	// regardless of timeout (e.g. you'd want to force an update after creating
	// a new Volume so it reflects in the inventory)
	private void update(boolean force, String reason) throws Exception {
		final Date d = new Date();
		this.update(force, d.getTime(), reason);
	}

	// Update account in the database - the boolean force means it will upgrade
	// regardless of timeout (e.g. you'd want to force an update after creating
	// a new Volume so it reflects in the inventory)

	// You can also pass the time the update was requested to this method if you
	// wish to have it different from the current system time for whatever
	// reason
	private void update(boolean force, long c, String reason) throws Exception {
		final String accountName = this.invStore.getAccountName();
		final HP3ParCredentials login = new HP3ParCredentials(accountName);
		final String queryString = "accountName == '" + accountName + "'";

		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(accountName);
		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(infraAccount);

		try {

			ObjStore<HP3ParInventoryDBStore> invStoreCollection = ObjStoreHelper.getStore(HP3ParInventoryDBStore.class);
			HP3ParInventoryDBStore store = null;
			try {
				store = invStoreCollection.query(queryString).iterator().next();
			}
			catch (Exception e) {
				logger.warn("Possibly stale entry from older API - deleting & re-creating. " + e.getMessage());
				invStoreCollection.delete(queryString);
				this.create(login);
				return;
			}

			if (store == null) {
				logger.warn("Cannot find " + accountName + " in inventory! Rolling back and creating new");
				// Attempt to create it:
				this.create(login);
				return;
			}

			if ((!force) && ((c - store.getUpdated()) < HP3ParConstants.INVENTORY_LIFE)) {
				return;
			}
			logger.info("Updating persistent store for account " + accountName);
			store.setUpdated(c);

			final HP3ParVolumeList volumeList = new HP3ParVolumeList(login);
			store.setVolumeListJson(volumeList.toJson());

			final HP3ParSystem systemInfo = new HP3ParSystem(login);
			store.setSysInfoJson(systemInfo.toJson());

			final HP3ParCPGList cpg = new HP3ParCPGList(login);
			store.setCpgListJson(cpg.toJson());

			final HP3ParHostList host = new HP3ParHostList(login);
			store.setHostListJson(host.toJson());

			final HP3ParHostSetList hostSetList = new HP3ParHostSetList(login);
			store.setHostSetListJson(hostSetList.toJson());

			final HP3ParVlunList vlun = new HP3ParVlunList(login);
			store.setVlunListJson(vlun.toJson());

			final HP3ParPortList port = new HP3ParPortList(login);
			store.setPortListJson(port.toJson());

			final Date d = new Date();
			final String update = c + "@" + d.getTime() + "@" + force + "@" + reason;

			log(store, update);

			this.invStore = store;
			invStoreCollection.modifySingleObject(queryString, this.invStore);

			status.setConnectionOK(true);

		}
		catch (Exception e) {
			logger.warn("Exception updating database! " + e.getMessage());

			status.setConnectionOK(false);
		}
	}

	private static void log(HP3ParInventoryDBStore store, String message) {
		store.getPolling().add(message);

		// Remove oldest entry if longer than the allowed log length
		if (store.getPolling().size() > HP3ParConstants.MAX_POLLING_LOG_ENTRIES) {
			store.getPolling().remove(0);
		}
	}

	// Return the full JSON representation of a volume query
	private VolumeResponse getVol() throws Exception {
		return new HP3ParVolumeList(this.getStore().getVolumeListJson()).getVolume();
	}

	// Return the full JSON representation of a VLUN query
	private VlunResponse getVlun() throws Exception {
		return new HP3ParVlunList(this.getStore().getVlunListJson()).getVlun();
	}

	// Return the full JSON representation of a CPG query
	private CPGResponse getCpg() throws Exception {
		return new HP3ParCPGList(this.getStore().getCpgListJson()).getCpg();
	}

	// Return the full JSON representation of a system query
	private SystemResponse getSys() throws Exception {
		return new HP3ParSystem(this.getStore().getSysInfoJson()).getSystem();
	}

	// Return the full JSON representation of a host query
	private HostResponse getHost() throws Exception {
		return new HP3ParHostList(this.getStore().getHostListJson()).getHost();
	}

	// Return the full JSON representation of a host set query
	private HostSetResponse getHostSet() throws Exception {
		return new HP3ParHostSetList(this.getStore().getHostSetListJson()).getHostSets();
	}

	// Return the full JSON representation of a port query
	private PortResponse getPorts() throws Exception {
		return new HP3ParPortList(this.getStore().getPortListJson()).getPorts();
	}

	// Return all the polling data
	private List<String> getPolling() throws Exception {
		return this.getStore().getPolling();
	}

	/**
	 * Return the HP3ParInventoryDBStore object which contains all the
	 * variables. This should not be passed directly outside of this class
	 *
	 * @return raw db store
	 */
	private HP3ParInventoryDBStore getStore() {
		return this.invStore;
	}

	/**
	 * Return information on a specific volume
	 *
	 * It will return null if the volume is not found
	 *
	 * @param credentials
	 * @param volumeName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static final VolumeResponseMember getVolumeInfo(HP3ParCredentials credentials,
			String volumeName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");

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
	 * It will return null if it is not found
	 *
	 *
	 * @param credentials
	 * @param cpgName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static CPGResponseMember getCpgInfo(HP3ParCredentials credentials, String cpgName)
			throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");

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
	 * It will return null if it is not found
	 *
	 * @param credentials
	 * @param hostName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static HostResponseMember getHostInfo(HP3ParCredentials credentials, String hostName)
			throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");

		for (final HostResponseMember i : inv.getHost().getMembers()) {
			if (hostName.equals(i.getName())) {
				return i;
			}
		}
		logger.warn("Host not found in cache: " + hostName);
		return null;
	}

	/**
	 * Return information on a specific host set
	 *
	 * It will return null if it is not found
	 *
	 * @param credentials
	 * @param hostSetName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static HostSetResponseMember getHostSetInfo(HP3ParCredentials credentials, String hostSetName)
			throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");

		for (final HostSetResponseMember i : inv.getHostSet().getMembers()) {
			if (hostSetName.equals(i.getName())) {
				return i;
			}
		}
		logger.warn("Host set not found in cache: " + hostSetName);
		return null;
	}

	/**
	 * Return information on a specific port
	 *
	 * It will return null if it is not found
	 *
	 * @param credentials
	 * @param portPos
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static PortResponseMember getPortInfo(HP3ParCredentials credentials, String portPos)
			throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");

		for (final PortResponseMember i : inv.getPorts().getMembers()) {
			if (portPos.equals(i.getPortPosAsString())) {
				return i;
			}
		}
		logger.warn("Port not found in cache: " + portPos);
		return null;
	}

	/**
	 * Return information on all volumes
	 *
	 * @param credentials
	 * @return volume response list
	 * @throws Exception
	 */
	public synchronized static VolumeResponse getVolumeResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");
		return inv.getVol();
	}

	/**
	 * Return system information
	 *
	 * @param credentials
	 * @return volume response list
	 * @throws Exception
	 */
	public synchronized static SystemResponse getSystemResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");
		return inv.getSys();
	}

	/**
	 * Return information on all CPGs
	 *
	 * @param credentials
	 * @return volume response list
	 * @throws Exception
	 */
	public synchronized static CPGResponse getCPGResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");
		return inv.getCpg();
	}

	/**
	 * Return information on all hosts
	 *
	 * @param credentials
	 * @return host response list
	 * @throws Exception
	 */
	public synchronized static HostResponse getHostResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");
		return inv.getHost();
	}

	/**
	 * Return information on all host sets
	 *
	 * @param credentials
	 * @return host response list
	 * @throws Exception
	 */
	public synchronized static HostSetResponse getHostSetResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");
		return inv.getHostSet();
	}

	/**
	 * Return information on all VLUNs
	 *
	 * @param credentials
	 * @return host response list
	 * @throws Exception
	 */
	public synchronized static VlunResponse getVlunResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");
		return inv.getVlun();
	}

	/**
	 * Get the inventory polling log
	 *
	 * @param credentials
	 * @return Get the polling data
	 * @throws Exception
	 */
	public synchronized static List<String> getPollingResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");
		return inv.getPolling();
	}

	/**
	 * Return information on all ports
	 *
	 * @param credentials
	 * @return host response list
	 * @throws Exception
	 */
	public synchronized static PortResponse getPortResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Periodic inventory collection");
		return inv.getPorts();
	}

	/**
	 * Deletes any previous data stored by the plugin and re-creates. Used on
	 * startup as we don't want to persist anything between system reloads (and
	 * it makes upgrading/API changes easier)
	 * <p>
	 *
	 * This should generally only be called during plugin initialisation. You
	 * should use this.update(credentials, true) otherwise to force a new
	 * collection
	 *
	 * @param credentials
	 * @throws Exception
	 */
	public synchronized static void init(HP3ParCredentials credentials) throws Exception {
		final String accountName = credentials.getAccountName();
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
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(false, "Initial collection");
	}

	/**
	 * Update from the 3PAR array to the local cache if it's timed out
	 *
	 * @param credentials
	 * @param force
	 *            Set to true in order to update regardless of whether the data
	 *            was out of date - for example after creating a volume you want
	 *            to ensure you have all the details in the inventory
	 * @param reason
	 *            Reason for the update (for the log)
	 * @throws Exception
	 */
	public synchronized static void update(HP3ParCredentials credentials, boolean force, String reason)
			throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		final Date d = new Date();
		inv.update(force, d.getTime(), reason);
	}

}
