package com.cisco.matday.ucsd.hp3par.lov;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.cloupia.lib.connector.account.AccountTypeEntry;
import com.cloupia.lib.connector.account.PhysicalAccountTypeManager;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

public class HP3ParAccountTabularProvider implements TabularReportGeneratorIf {

	private static Logger logger = Logger.getLogger(HP3ParAccountTabularProvider.class);

	public static final String TABULAR_PROVIDER = "hp3par_account_tabular_provider";
	private static final int NUM_ROWS = 8;

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		logger.warn("HP3PAR Tabular Report API called!");
		// Get list of accounts:
		// for (Iterator<VolumeResponseMembers> i =
		// newlist.getVolume().getMembers().iterator(); i.hasNext();) {
		for (Iterator<AccountTypeEntry> i = PhysicalAccountTypeManager.getInstance().getPhysicalAccountTypes()
				.iterator(); i.hasNext();) {
			AccountTypeEntry a = i.next();
			logger.warn("Got account type: " + a.getAccountType());
		}

		TabularReport report = new TabularReport();
		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Value", "Value");
		model.completedHeader();

		for (int i = 0; i < NUM_ROWS; i++) {
			model.addTextValue("name" + i);
			model.addTextValue("value" + i);
			model.completedRow();
		}

		model.updateReport(report);

		return report;
	}

}
