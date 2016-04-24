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
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPGList;
import com.cisco.matday.ucsd.hp3par.rest.hosts.HP3ParHostList;
import com.cisco.matday.ucsd.hp3par.rest.hostsets.HP3ParHostSetList;
import com.cisco.matday.ucsd.hp3par.rest.ports.HP3ParPortList;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.vluns.HP3ParVlunList;
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

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String hostListJson;

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String vlunListJson;

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String portListJson;

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String hostSetListJson;

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

		// Populate all fields
		logger.info("Setting up volume inventory");
		HP3ParVolumeList volume;
		try {
			volume = new HP3ParVolumeList(new HP3ParCredentials(accountName));
			this.volumeListJson = volume.toJson();

			logger.info("Setting up system inventory");
			final HP3ParSystem systemInfo = new HP3ParSystem(new HP3ParCredentials(accountName));
			this.sysInfoJson = systemInfo.toJson();

			logger.info("Setting up cpg inventory");
			final HP3ParCPGList cpg = new HP3ParCPGList(new HP3ParCredentials(accountName));
			this.cpgListJson = cpg.toJson();

			logger.info("Setting up host inventory");
			final HP3ParHostList host = new HP3ParHostList(new HP3ParCredentials(accountName));
			this.hostListJson = host.toJson();

			logger.info("Setting up host set inventory");
			final HP3ParHostSetList hostSet = new HP3ParHostSetList(new HP3ParCredentials(accountName));
			this.hostSetListJson = hostSet.toJson();

			logger.info("Setting up vlun inventory");
			final HP3ParVlunList vlun = new HP3ParVlunList(new HP3ParCredentials(accountName));
			this.vlunListJson = vlun.toJson();

			logger.info("Setting up port inventory");
			final HP3ParPortList ports = new HP3ParPortList(new HP3ParCredentials(accountName));
			this.portListJson = ports.toJson();
		}
		catch (Exception e) {
			e.printStackTrace();
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

	/**
	 * @return host list in json
	 */
	public String getHostListJson() {
		return this.hostListJson;
	}

	/**
	 * @param hostListJson
	 */
	public void setHostListJson(String hostListJson) {
		this.hostListJson = hostListJson;
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
	 * @return the vlunListJson
	 */
	public String getVlunListJson() {
		return this.vlunListJson;
	}

	/**
	 * @param vlunListJson
	 *            the vlunListJson to set
	 */
	public void setVlunListJson(String vlunListJson) {
		this.vlunListJson = vlunListJson;
	}

	/**
	 * @return Port list in JSON
	 */
	public String getPortListJson() {
		return this.portListJson;
	}

	/**
	 * Set the port list JSON
	 *
	 * @param portListJson
	 */
	public void setPortListJson(String portListJson) {
		this.portListJson = portListJson;
	}

	/**
	 * @return Host Set list in JSON
	 */
	public String getHostSetListJson() {
		return this.hostSetListJson;
	}

	/**
	 * Set the Host set list JSON
	 *
	 * @param hostSetListJson
	 */
	public void setHostSetListJson(String hostSetListJson) {
		this.hostSetListJson = hostSetListJson;
	}

}
