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
package com.cisco.matday.ucsd.hp3par.reports.hosts;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponse;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseDescriptors;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseMember;
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
public class HostReportImpl implements TabularReportGeneratorIf {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(HostReportImpl.class);

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
		model.addTextColumn("Persona", "Persona");
		model.addTextColumn("FC Paths", "FC Paths");
		model.addTextColumn("iSCSI Paths", "iSCSI Paths");
		model.addTextColumn("Location", "Location");
		model.addTextColumn("IP Address", "IP Address");
		model.addTextColumn("Operating System", "Operating System");
		model.addTextColumn("Model", "Model");
		model.addTextColumn("Contact", "Contact");
		model.addTextColumn("Comments", "Comments");

		model.completedHeader();

		HP3ParCredentials credentials = new HP3ParCredentials(context);

		HostResponse hostList = HP3ParInventory.getHostResponse(new HP3ParCredentials(context));

		for (HostResponseMember host : hostList.getMembers()) {

			// Internal ID, format:
			// accountName;hostid@accountName@hostName
			model.addTextValue(credentials.getAccountName() + ";" + host.getId() + "@" + credentials.getAccountName()
					+ "@" + host.getName());
			// Bad but we can use this to parse it all out later
			// ID
			model.addTextValue(Integer.toString(host.getId()));
			// Name
			model.addTextValue(host.getName());

			model.addTextValue(HostResponseMember.getPersona(host.getPersona()));

			final int fcPaths = host.getFCPaths().size();
			model.addTextValue(Integer.toString(fcPaths));
			final int scsiPaths = host.getiSCSIPaths().size();
			model.addTextValue(Integer.toString(scsiPaths));

			// Descriptors is optional so may return null, if so initialise with
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

		model.updateReport(report);

		return report;

	}

}
