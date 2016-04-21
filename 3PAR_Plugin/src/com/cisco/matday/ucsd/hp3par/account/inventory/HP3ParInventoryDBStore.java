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

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.HP3ParModule;
import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
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
@PersistenceCapable(detachable = "true", table = "hp3par_inventory_db_v1")
public class HP3ParInventoryDBStore implements InventoryDBItemIf {
	private static Logger logger = Logger.getLogger(HP3ParModule.class);

	@PrimaryKey
	@Column(name = "ID")
	private long id;

	@Persistent
	private String accountName;

	@Persistent
	private long updated = 0;

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String volumeListJson;

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String sysInfoJson;

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String cpgListJson;

	/**
	 * Get the API version in case this database needs to be rebuilt in the
	 * future
	 */
	@Persistent
	public static final int API_VERSION = 1;

	/**
	 * Initialise inventory with an account name
	 *
	 * @param accountName
	 *            Name of the account to persist
	 */
	public HP3ParInventoryDBStore(String accountName) {
		this.accountName = accountName;
		logger.info("Created persistent entry (API version: " + HP3ParInventoryDBStore.API_VERSION + ")");

		// Touch everything to ensure persistence
		if (this.volumeListJson == null) {
			logger.info("Setting up volume inventory");
			HP3ParVolumeList volume;
			try {
				volume = new HP3ParVolumeList(new HP3ParCredentials(accountName));
				this.volumeListJson = volume.toJson();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (this.sysInfoJson == null) {
			try {
				logger.info("Setting up system inventory");
				final HP3ParSystem systemInfo = new HP3ParSystem(new HP3ParCredentials(accountName));
				this.sysInfoJson = systemInfo.toJson();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (this.cpgListJson == null) {
			try {
				logger.info("Setting up cpg inventory");
				final HP3ParCPG cpg = new HP3ParCPG(new HP3ParCredentials(accountName));
				this.cpgListJson = cpg.toJson();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @return the cpgListJson
	 */
	public String getCpgListJson() {
		return this.cpgListJson;
	}

	/**
	 * @param cpgListJson
	 *            the cpgListJson to set
	 */
	public void setCpgListJson(String cpgListJson) {
		this.cpgListJson = cpgListJson;
	}

	/**
	 * @return the volumeListJson
	 */
	public String getVolumeListJson() {
		return this.volumeListJson;
	}

	/**
	 * @param volumeListJson
	 *            the volumeListJson to set
	 */
	public void setVolumeListJson(String volumeListJson) {
		this.volumeListJson = volumeListJson;
	}

	/**
	 * @return the sysInfoJson
	 */
	public String getSysInfoJson() {
		return this.sysInfoJson;
	}

	/**
	 * @param sysInfoJson
	 *            the sysInfoJson to set
	 */
	public void setSysInfoJson(String sysInfoJson) {
		this.sysInfoJson = sysInfoJson;
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

}