package com.cisco.matday.ucsd.hp3par.reports;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.cpg.CPGResponseMembers;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPG;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

public class CPGReportImpl implements TabularReportGeneratorIf {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(VolumeReportImpl.class);

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("ID", "ID");
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Allocated Capacity (GiB)", "Allocated Capacity (GiB)");
		model.addTextColumn("Free Capacity (GiB)", "Free Capacity (GiB)");
		model.addTextColumn("Used Capacity (GiB)", "Used Capacity (GiB)");
		model.addTextColumn("Virtual Volumes", "Virtual Volumes");

		model.completedHeader();

		HP3ParCredentials credentials = new HP3ParCredentials(context);

		HP3ParCPG cpglist = new HP3ParCPG(credentials);

		for (Iterator<CPGResponseMembers> i = cpglist.getCpg().getMembers().iterator(); i.hasNext();) {
			CPGResponseMembers cpg = i.next();
			// Get total
			double total = ((cpg.getUsrUsage().getTotalMiB() + cpg.getSAUsage().getTotalMiB()
					+ cpg.getSDUsage().getTotalMiB()) / 1024d);
			double used = ((cpg.getUsrUsage().getUsedMiB() + cpg.getSAUsage().getUsedMiB()
					+ cpg.getSDUsage().getUsedMiB()) / 1024d);
			double free = total - used;

			// ID
			model.addTextValue(Integer.toString(cpg.getId()));
			// Name
			model.addTextValue(cpg.getName());
			// Allocated Capacity
			model.addTextValue(Double.toString(total));
			// Used Capacity
			model.addTextValue(Double.toString(used));
			// Free Capacity
			model.addTextValue(Double.toString(free));
			// Volume Count
			model.addTextValue(Integer.toString(cpg.getNumTPVVs()));
			model.completedRow();
		}

		model.updateReport(report);

		return report;
	}

}
