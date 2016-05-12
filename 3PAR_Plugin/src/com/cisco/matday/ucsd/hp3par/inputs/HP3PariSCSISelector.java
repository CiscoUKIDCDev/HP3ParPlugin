/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package com.cisco.matday.ucsd.hp3par.inputs;

import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponse;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseiSCSIPaths;
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

/**
 * Table to allow selection of an HP 3PAR volumes - it should not be
 * instantiated directly but instead used as a form item
 *
 * @author Matt Day
 *
 */
public class HP3PariSCSISelector implements TabularReportGeneratorIf {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(HP3PariSCSISelector.class);

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {

		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		// Internal ID is hidden from normal view and is used by tasks later
		model.addTextColumn("Internal ID", "Internal ID", true);

		// Don't show host name if it's selected in context
		model.addTextColumn("HostName", "HostName", (context.getId().contains("@")) ? true : false);

		model.addTextColumn("iSCSI Name", "iSCSI Name");
		model.addTextColumn("Type", "Type");
		model.addTextColumn("iSCSI IP Address", "iSCSI IP Address");
		model.completedHeader();

		// Check if this is for a single host or all of them
		if (context.getId().contains("@")) {
			model = generateForSpecificHost(model, context);
		}
		else {
			model = HP3PariSCSISelector.generateForAllAccounts(model);
		}

		model.updateReport(report);
		return report;
	}

	private static TabularReportInternalModel generateForSpecificHost(TabularReportInternalModel model,
			ReportContext context) throws Exception {
		final String accountName = context.getId().split(";")[0];
		final String hostName = context.getId().split(";")[1].split("@")[0];
		final HP3ParCredentials credentials = new HP3ParCredentials(accountName);
		List<HostResponseiSCSIPaths> scsiPaths = HP3ParInventory.getHostInfo(credentials, hostName).getiSCSIPaths();

		for (HostResponseiSCSIPaths path : scsiPaths) {
			// Format
			// accountName;wwn@type
			model.addTextValue(accountName + ";" + hostName + "@" + path.getName() + "@iscsi");
			model.addTextValue(hostName);
			model.addTextValue(path.getName());
			model.addTextValue("iSCSI");
			model.addTextValue(path.getIPAddr());

			model.completedRow();
		}
		return model;

	}

	private static TabularReportInternalModel generateForAllAccounts(TabularReportInternalModel model)
			throws Exception {
		ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);
		List<InfraAccount> objs = store.queryAll();
		for (InfraAccount a : objs) {
			PhysicalInfraAccount acc = AccountUtil.getAccountByName(a.getAccountName());
			// Important to check if the account type is null first
			if ((acc != null) && (acc.getAccountType() != null)
					&& (acc.getAccountType().equals(HP3ParConstants.INFRA_ACCOUNT_TYPE))) {
				final HP3ParCredentials credentials = new HP3ParCredentials(a.getAccountName());
				HostResponse hostList = HP3ParInventory.getHostResponse(credentials);

				for (HostResponseMember host : hostList.getMembers()) {
					final String hostName = host.getName();
					List<HostResponseiSCSIPaths> scsiPaths = HP3ParInventory.getHostInfo(credentials, hostName)
							.getiSCSIPaths();

					for (HostResponseiSCSIPaths path : scsiPaths) {
						// Format
						// accountName;wwn@type
						model.addTextValue(a.getAccountName() + ";" + hostName + "@" + path.getName() + "@iscsi");
						model.addTextValue(hostName);
						model.addTextValue(path.getName());
						model.addTextValue("iSCSI");
						model.addTextValue(path.getIPAddr());

						model.completedRow();
					}
				}
			}
		}
		return model;
	}

}
