/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
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

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponse;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseDescriptors;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.sets.json.SetResponse;
import com.cisco.matday.ucsd.hp3par.rest.sets.json.SetResponseMember;
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
public class HP3ParHostAndHostSetSelector implements TabularReportGeneratorIf {

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
		model.addTextColumn("Type", "Type");
		model.addTextColumn("Account", "Account");
		model.addTextColumn("Comment", "Comment");

		model.completedHeader();

		for (InfraAccount a : objs) {
			PhysicalInfraAccount acc = AccountUtil.getAccountByName(a.getAccountName());
			// Important to check if the account type is null first
			if ((acc != null) && (acc.getAccountType() != null)
					&& (acc.getAccountType().equals(HP3ParConstants.INFRA_ACCOUNT_TYPE))) {

				// Internal ID format:
				// accountName;id@type@name

				final HP3ParCredentials credentials = new HP3ParCredentials(a.getAccountName());
				HostResponse hostList = HP3ParInventory.getHostResponse(new HP3ParCredentials(a.getAccountName()));
				SetResponse setList = HP3ParInventory.getHostSetResponse(new HP3ParCredentials(a.getAccountName()));

				for (HostResponseMember host : hostList.getMembers()) {
					// account;hostid@accountName@hostName
					String internalId = a.getAccountName() + ";" + host.getId() + "@host@" + host.getName();

					// Internal ID
					model.addTextValue(internalId);

					// Host ID
					model.addTextValue(Integer.toString(host.getId()));

					// Host Name
					model.addTextValue(host.getName());

					// Type
					model.addTextValue("Host");

					// Account Name
					model.addTextValue(a.getAccountName());

					HostResponseDescriptors desc = host.getDescriptors();
					if (desc == null) {
						desc = new HostResponseDescriptors();
					}
					model.addTextValue(desc.getComment());

					model.completedRow();
				}

				for (SetResponseMember hostSet : setList.getMembers()) {
					// Internal ID, format:
					// accountName;hostid@type@hostName

					// Internal ID
					model.addTextValue(
							credentials.getAccountName() + ";" + hostSet.getId() + "@hostset@" + hostSet.getName());

					// ID
					model.addTextValue(Integer.toString(hostSet.getId()));

					// Name
					model.addTextValue(hostSet.getName());

					model.addTextValue("Host Set");

					model.addTextValue(a.getAccountName());

					model.addTextValue(hostSet.getComment());

					model.completedRow();
				}
			}
		}
		model.updateReport(report);

		return report;
	}

}
