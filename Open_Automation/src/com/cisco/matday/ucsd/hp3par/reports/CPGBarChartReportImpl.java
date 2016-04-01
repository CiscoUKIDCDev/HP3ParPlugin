package com.cisco.matday.ucsd.hp3par.reports;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPG;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponse;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMembers;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.ReportNameValuePair;
import com.cloupia.model.cIM.SnapshotReport;
import com.cloupia.model.cIM.SnapshotReportCategory;
import com.cloupia.service.cIM.inframgr.SnapshotReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;

public class CPGBarChartReportImpl implements SnapshotReportGeneratorIf {
	
	private static Logger logger = Logger.getLogger(CPGBarChartReportImpl.class);

	@Override
	public SnapshotReport getSnapshotReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {
		logger.info("Trying to build bar chart...");
		SnapshotReport report = new SnapshotReport();
		report.setContext(context);
		report.setReportName(reportEntry.getReportLabel());
		report.setNumericalData(true);
		report.setValueAxisName("Volumes");
		report.setPrecision(0);
		
		logger.info("Running REST request");
		CPGResponse cpgList = new HP3ParCPG(new HP3ParCredentials(context)).getCpg();

		logger.info("Running through Name/Value pair");
		ReportNameValuePair[] rnv = new ReportNameValuePair[cpgList.getTotal()];
		int j = 0;
		for (Iterator<CPGResponseMembers> i = cpgList.getMembers().iterator(); i.hasNext();) {
			logger.info("Iterating (row " + j + ")");
			CPGResponseMembers cpg = i.next();
			String name = cpg.getName();
			int tpv = cpg.getNumTPVVs();
			if (name == null) {
				name = "-";
			}
			logger.info("Added :" + name + " == " + tpv);
			rnv[j++] = new ReportNameValuePair(name, tpv);
		}
		
		logger.info("Adding report");

		SnapshotReportCategory cpgs = new SnapshotReportCategory();
		cpgs.setCategoryName("cpgs");
		cpgs.setNameValuePairs(rnv);

		report.setCategories(new SnapshotReportCategory[] {
				cpgs
		});

		return report;
	}

}
