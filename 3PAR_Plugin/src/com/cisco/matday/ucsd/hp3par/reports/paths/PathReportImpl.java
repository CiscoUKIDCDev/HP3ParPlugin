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
package com.cisco.matday.ucsd.hp3par.reports.paths;

import java.util.List;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponse;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseFCPaths;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseiSCSIPaths;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Implementation of host paths report
 *
 * @author Matt Day
 *
 */
public class PathReportImpl implements TabularReportGeneratorIf {

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
		model.addTextColumn("WWN/iSCSI Name", "WWN/iSCSI Name");
		model.addTextColumn("Type", "Type");
		model.addTextColumn("iSCSI IP Address", "iSCSI IP Address");

		model.completedHeader();

		// Internal ID, format:
		// accountName;hostid@accountName@hostName
		final String accountName = context.getId().split(";")[0];

		HostResponse hostList = HP3ParInventory.getHostResponse(new HP3ParCredentials(context).getAccountName());

		for (HostResponseMember host : hostList.getMembers()) {
			final String hostName = host.getName();
			List<HostResponseFCPaths> fcPaths = HP3ParInventory.getHostInfo(accountName, hostName).getFCPaths();
			List<HostResponseiSCSIPaths> scsiPaths = HP3ParInventory.getHostInfo(accountName, hostName).getiSCSIPaths();

			for (HostResponseFCPaths path : fcPaths) {
				// Format
				// accountName;wwn@type
				model.addTextValue(accountName + ";" + path.getWwn() + "@fc");

				model.addTextValue(path.getWwn());
				model.addTextValue("Fibre Channel");
				model.addTextValue("N/A");

				model.completedRow();
			}

			for (HostResponseiSCSIPaths path : scsiPaths) {
				// Format
				// accountName;wwn@type
				model.addTextValue(accountName + ";" + path.getName() + "@iscsi");

				model.addTextValue(path.getName());
				model.addTextValue("iSCSI");
				model.addTextValue(path.getIPAddr());

				model.completedRow();
			}
		}

		model.updateReport(report);

		return report;
	}

}
