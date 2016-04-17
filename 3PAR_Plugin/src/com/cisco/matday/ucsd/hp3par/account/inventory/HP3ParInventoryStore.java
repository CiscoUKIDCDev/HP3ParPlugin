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
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
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
@PersistenceCapable(detachable = "true", table = "hp3par_inventory_v3")
public class HP3ParInventoryStore implements InventoryDBItemIf {
	private static Logger logger = Logger.getLogger(HP3ParModule.class);

	@Persistent
	private String accountName;

	@Persistent
	private long updated = 0;

	@Persistent
	private String volumeInfo;

	@Persistent
	private String sysInfo;

	@Persistent
	private String cpgInfo;

	/**
	 * Get the API version in case this database needs to be rebuilt in the
	 * future
	 */
	@Persistent
	public static final int API_VERSION = 3;

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
		if (this.volumeInfo == null) {
			logger.info("Setting up volume inventory");
			HP3ParVolumeList volumeList;
			try {
				volumeList = new HP3ParVolumeList(new HP3ParCredentials(accountName));
				this.volumeInfo = volumeList.toJson();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (this.sysInfo == null) {
			try {
				logger.info("Setting up system inventory");
				final HP3ParSystem systemInfo = new HP3ParSystem(new HP3ParCredentials(accountName));
				this.sysInfo = systemInfo.toJson();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (this.cpgInfo == null) {
			try {
				logger.info("Setting up cpg inventory");
				final HP3ParCPG cpg = new HP3ParCPG(new HP3ParCredentials(accountName));
				this.cpgInfo = cpg.toJson();
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
	public String getSysInfo() throws Exception {
		return this.sysInfo;
	}

	/**
	 * @return the cpginfo
	 * @throws Exception
	 * @throws InvalidHP3ParTokenException
	 * @throws IOException
	 */
	public String getCpgInfo() throws Exception {
		return this.cpgInfo;
	}

	/**
	 * @return the volumeInfo
	 * @throws Exception
	 * @throws InvalidHP3ParTokenException
	 * @throws IOException
	 */
	public String getVolumeInfo() throws Exception {
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
	public void setVolumeInfo(String volumeInfo) {
		this.volumeInfo = volumeInfo;
	}

	/**
	 * Set the system response data
	 *
	 * @param sysInfo
	 */
	public void setSysInfo(String sysInfo) {
		this.sysInfo = sysInfo;
	}

	/**
	 * Set the CPG response data
	 *
	 * @param cpgInfo
	 */
	public void setCpgInfo(String cpgInfo) {
		this.cpgInfo = cpgInfo;
	}

}
