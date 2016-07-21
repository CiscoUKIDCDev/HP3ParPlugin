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
package com.cisco.matday.ucsd.hp3par.reports.hostsets;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.rest.sets.json.SetResponse;
import com.cisco.matday.ucsd.hp3par.rest.sets.json.SetResponseMember;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Implements a host report list
 *
 * @author Matt Day
 *
 */
public class HostSetReportImpl implements TabularReportGeneratorIf {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(HostSetReportImpl.class);

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("Internal ID", "Internal ID", true);
		model.addTextColumn("ID", "ID");
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Members", "Members");
		model.addTextColumn("Comment", "Comments");

		model.completedHeader();

		HP3ParCredentials credentials = new HP3ParCredentials(context);

		SetResponse hostSetList = HP3ParInventory.getHostSetResponse(new HP3ParCredentials(context));

		for (SetResponseMember hostSet : hostSetList.getMembers()) {

			// Internal ID, format:
			// accountName;hostid@accountName@hostName
			model.addTextValue(credentials.getAccountName() + ";" + hostSet.getId() + "@" + credentials.getAccountName()
					+ "@" + hostSet.getName() + ";hostset");

			// Bad but we can use this to parse it all out later
			// ID
			model.addTextValue(Integer.toString(hostSet.getId()));
			model.addTextValue(hostSet.getName());
			String members = "";
			// Name
			for (String member : hostSet.getSetMembers()) {
				members += member + ", ";
			}
			// Remove trailing ', '
			if (members.length() > 0) {
				members = members.substring(0, members.length() - 2);
			}
			model.addTextValue(members);
			model.addTextValue(hostSet.getComment());

			model.completedRow();
		}

		model.updateReport(report);

		return report;

	}

}
