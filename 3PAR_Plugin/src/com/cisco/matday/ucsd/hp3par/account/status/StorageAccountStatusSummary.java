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
package com.cisco.matday.ucsd.hp3par.account.status;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParAccount;
import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.HP3ParToken;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.cIaaS.netapp.model.StorageAccountStatus;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;

/**
 * Shows the account summary in the converged view
 * 
 * @author Matt Day
 *
 */
public class StorageAccountStatusSummary {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(StorageAccountStatusSummary.class);

	/**
	 * Obtain account summary information
	 * 
	 * @param accountName
	 * @throws Exception
	 */
	public static void accountSummary(String accountName) throws Exception {
		HP3ParAccount acc = HP3ParCredentials.getInternalCredential(accountName);
		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(accountName);
		@SuppressWarnings("unused")
		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(infraAccount);

		StorageAccountStatus accStatus = new StorageAccountStatus();
		accStatus.setAccountName(infraAccount.getAccountName());
		accStatus.setDcName(acc.getPod());
		try {
			HP3ParToken t = new HP3ParToken(new HP3ParCredentials(accountName));
			String token = t.getToken();
			if (token != null) {
				accStatus.setReachable(true);
				t.release();
				accStatus.setLastMessage("Connection OK");
			}
			else {
				accStatus.setReachable(false);
				accStatus.setLastMessage("Could not connect (check username/password)");
			}
		}
		catch (@SuppressWarnings("unused") InvalidHP3ParTokenException e) {
			accStatus.setLastMessage("Could not connect (check username/password)");
			accStatus.setReachable(false);
		}
		catch (@SuppressWarnings("unused") Exception e) {
			accStatus.setLastMessage("Could not connect (is the array down?)");
			accStatus.setReachable(false);
		}

		accStatus.setLicense("");
		accStatus.setLastUpdated(System.currentTimeMillis());

		accStatus.setModel("3PAR");
		accStatus.setServerAddress(acc.getServerAddress());
		accStatus.setVersion("1");
		persistStorageAccountStatus(accStatus);
	}

	/**
	 * Not sure what this does - adding from SDK boilerplate
	 * 
	 * @param ac
	 * @throws Exception
	 */
	public static void persistStorageAccountStatus(StorageAccountStatus ac) throws Exception {
		PersistenceManager pm = ObjStoreHelper.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();

			String query = "accountName == '" + ac.getAccountName() + "'";

			Query q = pm.newQuery(StorageAccountStatus.class, query);
			q.deletePersistentAll();

			pm.makePersistent(ac);
			tx.commit();
		}
		finally {
			try {
				if (tx.isActive()) {
					tx.rollback();
				}
			}
			finally {
				pm.close();
			}
		}
	}
}
