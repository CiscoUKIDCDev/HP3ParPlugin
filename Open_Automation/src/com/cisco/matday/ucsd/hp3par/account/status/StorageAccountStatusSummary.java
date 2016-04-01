package com.cisco.matday.ucsd.hp3par.account.status;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.cisco.cuic.api.client.JSON;
import com.cisco.matday.ucsd.hp3par.account.HP3ParAccount;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.cIaaS.netapp.model.StorageAccountStatus;
import com.cloupia.lib.connector.account.AbstractInfraAccount;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;

public class StorageAccountStatusSummary {

	public static void accountSummary(String accountName) throws Exception {
		HP3ParAccount acc = getInternalCredential(accountName);
		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(accountName);
		@SuppressWarnings("unused")
		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(infraAccount);

		StorageAccountStatus accStatus = new StorageAccountStatus();
		accStatus.setAccountName(infraAccount.getAccountName());
		accStatus.setDcName(acc.getPod());
		// VERSION
		accStatus.setReachable(true);
		accStatus.setLicense("");
		accStatus.setLastMessage("Connection is verified");
		accStatus.setLastUpdated(System.currentTimeMillis());
		accStatus.setModel("");
		accStatus.setServerAddress(acc.getServerAddress());
		accStatus.setVersion("");
		persistStorageAccountStatus(accStatus);
	}

	private static HP3ParAccount getInternalCredential(String accountName) throws Exception {
		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(accountName);
		String json = infraAccount.getCredential();
		AbstractInfraAccount specificAcc = (AbstractInfraAccount) JSON.jsonToJavaObject(json, HP3ParAccount.class);
		specificAcc.setAccount(infraAccount);

		return (HP3ParAccount) specificAcc;

	}

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
