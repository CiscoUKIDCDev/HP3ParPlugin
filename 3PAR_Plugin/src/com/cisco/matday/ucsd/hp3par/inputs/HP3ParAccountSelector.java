package com.cisco.matday.ucsd.hp3par.inputs;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.InfraAccount;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

public class HP3ParAccountSelector implements TabularReportGeneratorIf {

	private static Logger logger = Logger.getLogger(HP3ParAccountSelector.class);

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		logger.warn("Building tabular report of accounts");

		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);
		List<InfraAccount> objs = store.queryAll();

		logger.info("Number of entries: " + objs.size());

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("Account Name", "Account Name");
		model.addTextColumn("IP Address", "IP Address");
		model.addTextColumn("Pod", "Pod");
		model.completedHeader();

		/*
		 * I'm jumping through a LOT of hoops to get the account list here...
		 * Surely there's a better way?
		 * 
		 * InfraAccount.accountTypeAsString() returns "Other" which is useless!
		 */
		for (Iterator<InfraAccount> i = objs.iterator(); i.hasNext();) {
			InfraAccount a = i.next();
			PhysicalInfraAccount acc = AccountUtil.getAccountByName(a.getAccountName());
			logger.info("Found account: " + a.accountTypeAsString());
			logger.info("Physical account reports as: " + acc.getAccountType());
			logger.info("JSON Data: " + acc.getCredential());
			if (acc.getAccountType().equals(HP3ParConstants.INFRA_ACCOUNT_TYPE)) {
				logger.info(a.accountTypeAsString() + " is one of ours");
				model.addTextValue(a.getAccountName());
				model.addTextValue(a.getServer());
				model.addTextValue(a.getDcName());
				model.completedRow();
			}

		}
		model.updateReport(report);

		return report;
	}

}
