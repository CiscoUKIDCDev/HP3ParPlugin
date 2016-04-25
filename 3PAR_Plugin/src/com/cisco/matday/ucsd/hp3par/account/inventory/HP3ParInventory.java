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

	private void create(HP3ParCredentials credentials) {
		final String accountName = credentials.getAccountName();
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
	 * @param c
	 *            Time
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 * @throws Exception
	 */
	private void update(boolean force, long c) throws Exception {
		final String accountName = this.invStore.getAccountName();
		final HP3ParCredentials login = new HP3ParCredentials(accountName);
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
			final String update = c + "@" + d.getTime() + "@" + force + "@" + "Inventory update";
			store.getPolling().add(update);

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

	private HostSetResponse getHostSet() throws Exception {
		return new HP3ParHostSetList(this.getStore().getHostSetListJson()).getHostSets();
	}

	private PortResponse getPorts() throws Exception {
		return new HP3ParPortList(this.getStore().getPortListJson()).getPorts();
	}

	private List<String> getPolling() throws Exception {
		return this.getStore().getPolling();
	}

	/**
	 * Return information on a specific volume
	 *
	 * @param credentials
	 * @param volumeName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static final VolumeResponseMember getVolumeInfo(HP3ParCredentials credentials,
			String volumeName) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
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
	 * @param credentials
	 * @param cpgName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static CPGResponseMember getCpgInfo(HP3ParCredentials credentials, String cpgName)
			throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
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
	 * @param credentials
	 * @param hostName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static HostResponseMember getHostInfo(HP3ParCredentials credentials, String hostName)
			throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
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
	 * Return information on a specific Host
	 *
	 * @param credentials
	 * @param hostSetName
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static HostSetResponseMember getHostSetInfo(HP3ParCredentials credentials, String hostSetName)
			throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update();

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
	 * @param credentials
	 * @param portPos
	 * @return Volume information
	 * @throws Exception
	 */
	public synchronized static PortResponseMember getPortInfo(HP3ParCredentials credentials, String portPos)
			throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update();

		for (final PortResponseMember i : inv.getPorts().getMembers()) {
			if (portPos.equals(i.getPortPosAsString())) {
				return i;
			}
		}
		logger.warn("Port not found in cache: " + portPos);
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
		final Date d = new Date();
		this.update(false, d.getTime());
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
	 * @param credentials
	 * @return volume response list
	 * @throws Exception
	 */
	public synchronized static VolumeResponse getVolumeResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update();
		return inv.getVol();
	}

	/**
	 * Get the system response data
	 *
	 * @param credentials
	 * @return volume response list
	 * @throws Exception
	 */
	public synchronized static SystemResponse getSystemResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update();
		return inv.getSys();
	}

	/**
	 * Get the CPG response data
	 *
	 * @param credentials
	 * @return volume response list
	 * @throws Exception
	 */
	public synchronized static CPGResponse getCPGResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update();
		return inv.getCpg();
	}

	/**
	 * Get the Host response data
	 *
	 * @param credentials
	 * @return host response list
	 * @throws Exception
	 */
	public synchronized static HostResponse getHostResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update();
		return inv.getHost();
	}

	/**
	 * Get the Host Set response data
	 *
	 * @param credentials
	 * @return host response list
	 * @throws Exception
	 */
	public synchronized static HostSetResponse getHostSetResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update();
		return inv.getHostSet();
	}

	/**
	 * Get the VLUN response data
	 *
	 * @param credentials
	 * @return host response list
	 * @throws Exception
	 */
	public synchronized static VlunResponse getVlunResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update();
		return inv.getVlun();
	}

	/**
	 * Get polling log
	 *
	 * @param credentials
	 * @return Get the polling data
	 * @throws Exception
	 */
	public synchronized static List<String> getPollingResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update();
		return inv.getPolling();
	}

	/**
	 * Get the Port response data
	 *
	 * @param credentials
	 * @return host response list
	 * @throws Exception
	 */
	public synchronized static PortResponse getPortResponse(HP3ParCredentials credentials) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update();
		return inv.getPorts();
	}

	/**
	 * Deletes old persistent data and re-creates on startup
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
		inv.update();
	}

	/**
	 * Update from the 3PAR array to the local cache if it's timed out
	 *
	 * @param credentials
	 * @param force
	 *            - force the updates
	 * @throws Exception
	 */
	public synchronized static void update(HP3ParCredentials credentials, boolean force) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		final Date d = new Date();
		inv.update(force, d.getTime());
	}

	/**
	 * Update from the 3PAR array to the local cache if it's timed out
	 *
	 * @param credentials
	 * @param force
	 *            Force the updates
	 * @param c
	 *            Time from which to source update
	 * @throws Exception
	 */
	public synchronized static void update(HP3ParCredentials credentials, boolean force, long c) throws Exception {
		HP3ParInventory inv = new HP3ParInventory(credentials);
		inv.update(force, c);
	}

}
