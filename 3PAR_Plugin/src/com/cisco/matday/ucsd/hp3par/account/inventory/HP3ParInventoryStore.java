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

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.HP3ParModule;
import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPG;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponse;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.system.json.SystemResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cloupia.model.cIM.InventoryDBItemIf;

/**
 * Stores all persistent data for inventory purposes.
 *
 * Use the HP3ParInventory wrapper to access this as it handles persistence and
 * updates
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "hp3par_inventory_v5")
public class HP3ParInventoryStore implements InventoryDBItemIf {
	private static Logger logger = Logger.getLogger(HP3ParModule.class);

	@Persistent
	private String accountName;

	@Persistent
	private long updated = 0;

	@Persistent(defaultFetchGroup = "true")
	private VolumeResponse volumeList;

	@Persistent(defaultFetchGroup = "true")
	private SystemResponse sysInfo;

	@Persistent(defaultFetchGroup = "true")
	private CPGResponse cpgList;

	/**
	 * Get the API version in case this database needs to be rebuilt in the
	 * future
	 */
	@Persistent
	public static final int API_VERSION = 5;

	/**
	 * Initialise inventory with an account name
	 *
	 * @param accountName
	 *            Name of the account to persist
	 */
	public HP3ParInventoryStore(String accountName) {
		this.accountName = accountName;
		logger.info("Created persistent entry (API version: " + HP3ParInventoryStore.API_VERSION + ")");

		// Touch everything to ensure persistence
		if (this.volumeList == null) {
			logger.info("Setting up volume inventory");
			HP3ParVolumeList volume;
			try {
				volume = new HP3ParVolumeList(new HP3ParCredentials(accountName));
				this.volumeList = volume.getVolume();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (this.sysInfo == null) {
			try {
				logger.info("Setting up system inventory");
				final HP3ParSystem systemInfo = new HP3ParSystem(new HP3ParCredentials(accountName));
				this.sysInfo = systemInfo.getSystem();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (this.cpgList == null) {
			try {
				logger.info("Setting up cpg inventory");
				final HP3ParCPG cpg = new HP3ParCPG(new HP3ParCredentials(accountName));
				this.cpgList = cpg.getCpg();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @return the sysinfo
	 * @throws Exception
	 * @throws InvalidHP3ParTokenException
	 * @throws IOException
	 */
	public SystemResponse getSysInfo() throws Exception {
		return this.sysInfo;
	}

	/**
	 * @return the cpginfo
	 * @throws Exception
	 * @throws InvalidHP3ParTokenException
	 * @throws IOException
	 */
	public CPGResponse getCpgList() throws Exception {
		return this.cpgList;
	}

	/**
	 * @return the volumeInfo
	 * @throws Exception
	 * @throws InvalidHP3ParTokenException
	 * @throws IOException
	 */
	public VolumeResponse getVolumeList() throws Exception {
		return this.volumeList;
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
	 * @return when this was last updated in the DB
	 */
	public long getUpdated() {
		return this.updated;
	}

	/**
	 * Set the new update time
	 *
	 * @param updated
	 */
	public void setUpdated(long updated) {
		this.updated = updated;
	}

	/**
	 * Set the volume response data
	 *
	 * @param volumeInfo
	 */
	public void setVolumeList(VolumeResponse volumeInfo) {
		this.volumeList = volumeInfo;
	}

	/**
	 * Set the system response data
	 *
	 * @param sysInfo
	 */
	public void setSysInfo(SystemResponse sysInfo) {
		this.sysInfo = sysInfo;
	}

	/**
	 * Set the CPG response data
	 *
	 * @param cpgInfo
	 */
	public void setCpgList(CPGResponse cpgInfo) {
		this.cpgList = cpgInfo;
	}

}
