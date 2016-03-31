package com.cisco.matday.ucsd.hp3par.reports;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.VolumeResponseMembers;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

public class VolumeReportImpl<E> implements TabularReportGeneratorIf {

	private static Logger logger = Logger.getLogger(VolumeReportImpl.class);

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		String accountName = "";
		String contextId = context.getId();
		
		if (contextId != null) {
			// As the contextId returns as: "account Name;POD Name"
			accountName = contextId.split(";")[0];
		}

		PhysicalInfraAccount acc = AccountUtil.getAccountByName(accountName);
		if (acc == null) {
			throw new Exception("Unable to find the account:" + accountName);
		}
		
		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Size GB", "Size GB");
		model.completedHeader();

		HP3ParCredentials credentials = new HP3ParCredentials(acc);
		
		HP3ParVolumeList list = new HP3ParVolumeList(credentials);
	
		
		for (Iterator<VolumeResponseMembers> i = list.getVolume().getMembers().iterator(); i.hasNext();) {
			VolumeResponseMembers volume = i.next();
			model.addTextValue(volume.getName());
			Double volSize = (double)(volume.getSizeMiB() / 1024d);
			model.addTextValue(volSize.toString());
			model.completedRow();
		}
		model.updateReport(report);

		return report;
	}

}
