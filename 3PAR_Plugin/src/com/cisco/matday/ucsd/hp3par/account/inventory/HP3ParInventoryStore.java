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

import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponse;
import com.cisco.matday.ucsd.hp3par.rest.system.json.SystemResponse;
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
@PersistenceCapable(detachable = "true", table = "hp3par_inventory")
public class HP3ParInventoryStore implements InventoryDBItemIf {
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

	/**
	 * Initialise inventory with an account name
	 *
	 * @param accountName
	 *            Name of the account to persist
	 */
	public HP3ParInventoryStore(String accountName) {
		this.accountName = accountName;
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
	public CPGResponse getCpgInfo() throws Exception {
		return this.cpgInfo;
	}

	/**
	 * @return the volumeInfo
	 * @throws Exception
	 * @throws InvalidHP3ParTokenException
	 * @throws IOException
	 */
	public VolumeResponse getVolumeInfo() throws Exception {
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
	public void setVolumeInfo(VolumeResponse volumeInfo) {
		this.volumeInfo = volumeInfo;
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
	public void setCpgInfo(CPGResponse cpgInfo) {
		this.cpgInfo = cpgInfo;
	}

}
