package com.cisco.matday.ucsd.hp3par.account.status;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParAccount;
import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.cIaaS.netapp.model.StorageAccountStatus;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;

public class StorageAccountStatusSummary {
	private static Logger logger = Logger.getLogger(StorageAccountStatusSummary.class);

	public static void accountSummary(String accountName) throws Exception {
		logger.info("pppppppppppppppppppppppppppppppppp");
		logger.info("Getting info on " + accountName);
		HP3ParAccount acc = HP3ParCredentials.getInternalCredential(accountName);
		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(accountName);
		@SuppressWarnings("unused")
		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(infraAccount);

		StorageAccountStatus accStatus = new StorageAccountStatus();
		if (accStatus.getModel() != null) {
			logger.info("Model reporting as: " + accStatus.getModel());
		}
		else {
			logger.info("Model is null");
		}
		if (accStatus.getLicense() != null) {
			logger.info("License reporting as: " + accStatus.getLicense());
		}
		else {
			logger.info("License is null");
		}
		accStatus.setAccountName(infraAccount.getAccountName());
		accStatus.setDcName(acc.getPod());
		// VERSION
		accStatus.setReachable(true);
		accStatus.setLicense("");
		accStatus.setLastMessage("Connection is verified");
		accStatus.setLastUpdated(System.currentTimeMillis());

		accStatus.setModel("3PAR");
		accStatus.setServerAddress(acc.getServerAddress());
		accStatus.setVersion("1");
		persistStorageAccountStatus(accStatus);
		logger.info("pppppppppppppppppppppppppppppppppp");
	}

	/*
	 * private static HP3ParAccount getInternalCredential(String accountName)
	 * throws Exception { PhysicalInfraAccount infraAccount =
	 * AccountUtil.getAccountByName(accountName); String json =
	 * infraAccount.getCredential(); AbstractInfraAccount specificAcc =
	 * (AbstractInfraAccount) JSON.jsonToJavaObject(json, HP3ParAccount.class);
	 * specificAcc.setAccount(infraAccount);
	 * 
	 * return (HP3ParAccount) specificAcc;
	 * 
	 * }
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
