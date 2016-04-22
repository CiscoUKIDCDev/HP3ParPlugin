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

import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponse;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseDescriptors;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseMember;
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
public class HP3ParHostSelector implements TabularReportGeneratorIf {

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {

		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);
		List<InfraAccount> objs = store.queryAll();

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("InternalID", "InternalID", true);
		model.addTextColumn("ID", "ID");
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Account", "Account");
		model.addTextColumn("FC Paths", "FC Paths");
		model.addTextColumn("iSCSI Paths", "iSCSI Paths");
		model.addTextColumn("Location", "Location");
		model.addTextColumn("IP Address", "IP Address");
		model.addTextColumn("Operating System", "Operating System");
		model.addTextColumn("Model", "Model");
		model.addTextColumn("Contact", "Contact");
		model.addTextColumn("Comments", "Comments");

		model.completedHeader();

		for (InfraAccount a : objs) {
			PhysicalInfraAccount acc = AccountUtil.getAccountByName(a.getAccountName());
			// Important to check if the account type is null first
			if ((acc != null) && (acc.getAccountType() != null)
					&& (acc.getAccountType().equals(HP3ParConstants.INFRA_ACCOUNT_TYPE))) {

				HostResponse hostList = HP3ParInventory.getHostResponse(a.getAccountName());

				for (HostResponseMember host : hostList.getMembers()) {
					// hostid@accountName@hostName
					String internalId = host.getId() + "@" + a.getAccountName() + "@" + host.getName();

					// Internal ID
					model.addTextValue(internalId);

					// Volume ID
					model.addTextValue(Integer.toString(host.getId()));
					// Name of this volume
					model.addTextValue(host.getName());

					// Account Name
					model.addTextValue(a.getAccountName());

					final int fcPaths = host.getFCPaths().size();
					model.addTextValue(Integer.toString(fcPaths));
					final int scsiPaths = host.getiSCSIPaths().size();
					model.addTextValue(Integer.toString(scsiPaths));

					// Descriptors is optional so may return null, if so
					// initialise with
					// defaults:
					HostResponseDescriptors desc = host.getDescriptors();
					if (desc == null) {
						desc = new HostResponseDescriptors();
					}

					// Descriptors
					model.addTextValue(desc.getLocation());
					model.addTextValue(desc.getIPAddr());
					model.addTextValue(desc.getOs());
					model.addTextValue(desc.getModel());
					model.addTextValue(desc.getContact());
					model.addTextValue(desc.getComment());

					model.completedRow();
				}
			}
		}
		model.updateReport(report);

		return report;
	}

}
