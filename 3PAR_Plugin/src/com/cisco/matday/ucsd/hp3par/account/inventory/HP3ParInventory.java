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

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cloupia.model.cIM.InventoryDBItemIf;

/**
 * Stores all persistent data for inventory purposes
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "hp3par_inventory")
public class HP3ParInventory implements InventoryDBItemIf {
	@Persistent
	private String accountName;

	@Persistent
	private VolumeResponse volumeInfo;

	@Persistent
	private final long updated;

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
	 * @return the volumeInfo
	 */
	public VolumeResponse getVolumeInfo() {
		return this.volumeInfo;
	}

	/**
	 * @param volumeInfo
	 *            the volumeInfo to set
	 */
	public void setVolumeInfo(VolumeResponse volumeInfo) {
		this.volumeInfo = volumeInfo;
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
	 * @throws HttpException
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 * @throws Exception
	 */
	public void update(boolean force) throws HttpException, IOException, InvalidHP3ParTokenException, Exception {
		if (!force) {
			final Date d = new Date();
			final long c = d.getTime();
			if ((c - this.updated) < HP3ParConstants.INVENTORY_LIFE) {
				return;
			}
		}
		final HP3ParVolumeList list = new HP3ParVolumeList(new HP3ParCredentials(this.accountName));
		this.volumeInfo = list.getVolume();
	}

	/**
	 * Update the inventory held in the database if it is not out of date
	 *
	 * @throws HttpException
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 * @throws Exception
	 */
	public void update() throws HttpException, IOException, InvalidHP3ParTokenException, Exception {
		this.update(false);
	}

}
