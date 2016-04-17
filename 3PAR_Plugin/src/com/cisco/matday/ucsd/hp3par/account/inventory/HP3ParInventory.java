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

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.matday.ucsd.hp3par.account.HP3ParAccount;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;

/**
 * Stores all persistent data for inventory purposes
 * 
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "hp3par_inventory")
public class HP3ParInventory {
	@Persistent
	private HP3ParAccount account;

	@Persistent
	private VolumeResponse volumeInfo;

	/**
	 * @return the account
	 */
	public HP3ParAccount getAccount() {
		return this.account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(HP3ParAccount account) {
		this.account = account;
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

}
