package com.cisco.matday.ucsd.hp3par.reports;

import java.util.Iterator;

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

	@Override
	public SnapshotReport getSnapshotReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {
		SnapshotReport report = new SnapshotReport();
		report.setContext(context);
		report.setReportName(reportEntry.getReportLabel());
		report.setNumericalData(true);
		report.setValueAxisName("Volumes");
		report.setPrecision(0);

		CPGResponse cpgList = new HP3ParCPG(new HP3ParCredentials(context)).getCpg();

		ReportNameValuePair[] rnv = new ReportNameValuePair[cpgList.getTotal()];
		int j = 0;
		for (Iterator<CPGResponseMembers> i = cpgList.getMembers().iterator(); i.hasNext();) {
			CPGResponseMembers cpg = i.next();
			rnv[j++] = new ReportNameValuePair(cpg.getName(), cpg.getNumTPVVs());
		}

		SnapshotReportCategory cpgs = new SnapshotReportCategory();
		cpgs.setCategoryName("cpgs");
		cpgs.setNameValuePairs(rnv);

		report.setCategories(new SnapshotReportCategory[] {
				cpgs
		});

		return report;
	}

}
