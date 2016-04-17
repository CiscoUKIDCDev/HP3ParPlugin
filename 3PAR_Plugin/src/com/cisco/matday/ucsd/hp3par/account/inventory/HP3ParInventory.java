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

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPG;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponse;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.system.json.SystemResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.model.cIM.InventoryDBItemIf;

/**
 * Stores all persistent data for inventory purposes.
 *
 * This should generally be used instead of the REST calls as it will cache any
 * data. If you need the freshest data, request an update with force set to
 * true.
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "hp3par_inventory")
public class HP3ParInventory implements InventoryDBItemIf {
	@Persistent
	private String accountName;

	@Persistent
	private long updated;

	@Persistent
	private VolumeResponse volumeInfo;

	@Persistent
	private SystemResponse sysInfo;

	@Persistent
	private CPGResponse cpgInfo;

	private static Logger logger = Logger.getLogger(HP3ParInventory.class);

	/**
	 * Initialise inventory with an account name
	 *
	 * @param accountName
	 *            Name of the account to persist
	 */
	public HP3ParInventory(String accountName) {
		this.accountName = accountName;
		this.updated = 0;
	}

	/**
	 * @return the sysinfo
	 * @throws Exception
	 * @throws InvalidHP3ParTokenException
	 * @throws IOException
	 */
	public SystemResponse getSysInfo() throws Exception {
		logger.info("Returning system info");
		// Update first if outdated:
		this.update(false);
		return this.sysInfo;
	}

	/**
	 * @return the cpginfo
	 * @throws Exception
	 * @throws InvalidHP3ParTokenException
	 * @throws IOException
	 */
	public CPGResponse getCpgInfo() throws Exception {
		logger.info("Returning cpg info");
		// Update first if outdated:
		this.update(false);
		return this.cpgInfo;
	}

	/**
	 * @return the volumeInfo
	 * @throws Exception
	 * @throws InvalidHP3ParTokenException
	 * @throws IOException
	 */
	public VolumeResponse getVolumeInfo() throws Exception {
		logger.info("Returning volume info");
		// Update first if outdated:
		this.update(false);
		return this.volumeInfo;
	}

	@Override
	public String getAccountName() {
		return this.accountName;
	}

	@Override
	public void setAccountName(String accountName) {
		this.accountName = accountName;
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
		if ((!force) && ((c - this.updated) < HP3ParConstants.INVENTORY_LIFE)) {
			logger.info("Inventory not out of date, so skipping update");
			return;
		}
		logger.info("Inventory out of date (or forced to update)");
		logger.info("Last update: " + this.updated);
		logger.info("Time now: " + c);
		logger.info("Difference = " + (c - this.updated) + " allowed delta = " + HP3ParConstants.INVENTORY_LIFE);

		this.updated = c;
		logger.info("Pulling down volume inventory");
		@SuppressWarnings("deprecation")
		final HP3ParVolumeList volumeList = new HP3ParVolumeList(new HP3ParCredentials(this.accountName));
		this.volumeInfo = volumeList.getVolume();
		logger.info("Pulling down system inventory");
		@SuppressWarnings("deprecation")
		final HP3ParSystem systemInfo = new HP3ParSystem(new HP3ParCredentials(this.accountName));
		this.sysInfo = systemInfo.getSystem();
		logger.info("Pulling down cpg inventory");
		@SuppressWarnings("deprecation")
		final HP3ParCPG cpg = new HP3ParCPG(new HP3ParCredentials(this.accountName));
		this.cpgInfo = cpg.getCpg();

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
	public static HP3ParInventory get(String accountName) throws Exception {
		final ObjStore<HP3ParInventory> store = ObjStoreHelper.getStore(HP3ParInventory.class);
		final List<HP3ParInventory> invStore = store.queryAll();
		HP3ParInventory inv = null;

		for (final HP3ParInventory i : invStore) {
			if (accountName.equals(i.getAccountName())) {
				logger.info("Found persistence: " + i.getAccountName());
				return i;
			}
		}
		logger.info("Creating new inventory for account " + accountName);
		inv = new HP3ParInventory(accountName);
		store.insert(inv);
		return inv;
	}

	/**
	 * Get the volume response data
	 *
	 * @param accountName
	 * @return volume response list
	 * @throws Exception
	 */
	public static VolumeResponse getVolumeResponse(String accountName) throws Exception {
		return HP3ParInventory.get(accountName).getVolumeInfo();
	}

	/**
	 * Get the system response data
	 *
	 * @param accountName
	 * @return volume response list
	 * @throws Exception
	 */
	public static SystemResponse getSystemResponse(String accountName) throws Exception {
		return HP3ParInventory.get(accountName).getSysInfo();
	}

	/**
	 * Get the CPG response data
	 *
	 * @param accountName
	 * @return volume response list
	 * @throws Exception
	 */
	public static CPGResponse getCPGResponse(String accountName) throws Exception {
		return HP3ParInventory.get(accountName).getCpgInfo();
	}

	/**
	 * Update
	 *
	 * @param accountName
	 * @param force
	 *            - force the updates
	 * @throws Exception
	 */
	public static void update(String accountName, boolean force) throws Exception {
		HP3ParInventory.get(accountName).update(force);
	}

}
