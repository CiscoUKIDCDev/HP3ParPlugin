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

import java.util.ArrayList;
import java.util.List;

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
 * <p>
 *
 * Use the HP3ParInventory wrapper to access this as it handles persistence and
 * updates. You shouldn't generally instantiate this directly.
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "hp3par_inventory_db_v2")
public class HP3ParInventoryDBStore implements InventoryDBItemIf {
	private static Logger logger = Logger.getLogger(HP3ParModule.class);

	@PrimaryKey
	@Column(name = "ID")
	private long id;

	@Persistent
	private String accountName;

	@Persistent
	private long updated = 0;

	// CLOB is an SQL data type of unlimited length strings; perfect for JSON
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

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private List<String> polling;

	/**
	 * Get the API version in case this database needs to be rebuilt in the
	 * future
	 */
	@Persistent
	protected static final int API_VERSION = 2;

	/**
	 * Initialise inventory with an account name
	 *
	 * @param accountName
	 *            Name of the account to persist
	 */
	protected HP3ParInventoryDBStore(String accountName) {
		this.accountName = accountName;
		logger.info("Created persistent entry (DB version: " + HP3ParInventoryDBStore.API_VERSION + ")");

		// Populate all fields

		try {
			logger.info("Setting up volume inventory");
			HP3ParVolumeList volume;
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

			logger.info("Setting up polling history");
			this.polling = new ArrayList<>();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return polling data
	 */
	protected List<String> getPolling() {
		return this.polling;
	}

	/**
	 * @param polling
	 */
	protected void setPolling(List<String> polling) {
		this.polling = polling;
	}

	/**
	 * @return the cpgListJson
	 */
	protected String getCpgListJson() {
		return this.cpgListJson;
	}

	/**
	 * @param cpgListJson
	 *            the cpgListJson to set
	 */
	protected void setCpgListJson(String cpgListJson) {
		this.cpgListJson = cpgListJson;
	}

	/**
	 * @return the volumeListJson
	 */
	protected String getVolumeListJson() {
		return this.volumeListJson;
	}

	/**
	 * @param volumeListJson
	 *            the volumeListJson to set
	 */
	protected void setVolumeListJson(String volumeListJson) {
		this.volumeListJson = volumeListJson;
	}

	/**
	 * @return the sysInfoJson
	 */
	protected String getSysInfoJson() {
		return this.sysInfoJson;
	}

	/**
	 * @param sysInfoJson
	 *            the sysInfoJson to set
	 */
	protected void setSysInfoJson(String sysInfoJson) {
		this.sysInfoJson = sysInfoJson;
	}

	/**
	 * @return host list in json
	 */
	protected String getHostListJson() {
		return this.hostListJson;
	}

	/**
	 * @param hostListJson
	 */
	protected void setHostListJson(String hostListJson) {
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
	protected long getUpdated() {
		return this.updated;
	}

	/**
	 * Set the new update time
	 *
	 * @param updated
	 */
	protected void setUpdated(long updated) {
		this.updated = updated;
	}

	/**
	 * @return the vlunListJson
	 */
	protected String getVlunListJson() {
		return this.vlunListJson;
	}

	/**
	 * @param vlunListJson
	 *            the vlunListJson to set
	 */
	protected void setVlunListJson(String vlunListJson) {
		this.vlunListJson = vlunListJson;
	}

	/**
	 * @return Port list in JSON
	 */
	protected String getPortListJson() {
		return this.portListJson;
	}

	/**
	 * Set the port list JSON
	 *
	 * @param portListJson
	 */
	protected void setPortListJson(String portListJson) {
		this.portListJson = portListJson;
	}

	/**
	 * @return Host Set list in JSON
	 */
	protected String getHostSetListJson() {
		return this.hostSetListJson;
	}

	/**
	 * Set the Host set list JSON
	 *
	 * @param hostSetListJson
	 */
	protected void setHostSetListJson(String hostSetListJson) {
		this.hostSetListJson = hostSetListJson;
	}

}
