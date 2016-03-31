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
			logger.error("Couldn't find account " + accountName);
			throw new Exception("Unable to find the account:" + accountName);
		}

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("ID", "ID");
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Size GB", "Size GB");
		model.addTextColumn("Provisioning", "Provisioning");
		model.addTextColumn("User CPG", "User CPG");

		model.completedHeader();

		HP3ParCredentials credentials = new HP3ParCredentials(acc);

		HP3ParVolumeList list = new HP3ParVolumeList(credentials);

		for (Iterator<VolumeResponseMembers> i = list.getVolume().getMembers().iterator(); i.hasNext();) {
			VolumeResponseMembers volume = i.next();
			// Volume ID
			model.addTextValue(Integer.toString(volume.getId()));
			// Name of this volume
			model.addTextValue(volume.getName());
			
			// Round off the size to gb with double precision
			Double volSize = (double) (volume.getSizeMiB() / 1024d);
			model.addTextValue(volSize.toString());
			
			// Get the provisioning type (1 = full, 2 = thin):
			int provisioning = volume.getProvisioningType();
			if (provisioning == 1) {
				model.addTextValue("Full");
			}
			else if (provisioning == 2) {
				model.addTextValue("Thin");
			}
			else {
				model.addTextValue("Unknown");
			}
			
			model.addTextValue(volume.getUserCPG());
			
			model.completedRow();
		}
		model.updateReport(report);

		return report;
	}

}
