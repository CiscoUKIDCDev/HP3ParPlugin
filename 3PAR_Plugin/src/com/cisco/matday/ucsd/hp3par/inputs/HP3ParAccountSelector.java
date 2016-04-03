package com.cisco.matday.ucsd.hp3par.inputs;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
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

		logger.warn("Singleton: " + store.getSingleton());
		logger.warn("Number of entries: " + objs.size());
		
	
		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("Account Name", "Account Name");
		model.addTextColumn("IP Address", "IP Address");
		model.addTextColumn("Model", "Model");
		model.addTextColumn("Type", "Type");
		model.addTextColumn("SubType", "SubType");
		model.addTextColumn("DC Name", "DC Name");
		model.completedHeader();

		for (Iterator<InfraAccount> i = objs.iterator(); i.hasNext(); ) {
			// TODO - maybe just cast to HP3ParAccount and catch exceptions?
			InfraAccount a = i.next();
			logger.warn("Account list got: " + a.getAccountName());
			model.addTextValue(a.getAccountName());
			model.addTextValue(a.getServer());
			model.addTextValue(a.getModel());
			model.addTextValue(Integer.toString(a.getAccountType()));
			model.addTextValue(a.getAccountSubType());
			model.addTextValue(a.getDcName());

			/*logger.warn("Account list got: " + a.getAccount().getAccountName());
			model.addTextValue(a.getAccount().getAccountName());
			model.addTextValue(a.getArray_address());
			model.addTextValue(a.getAccount().getHwModel());*/
			model.completedRow();
		}
		model.updateReport(report);


		return report;
	}

}
