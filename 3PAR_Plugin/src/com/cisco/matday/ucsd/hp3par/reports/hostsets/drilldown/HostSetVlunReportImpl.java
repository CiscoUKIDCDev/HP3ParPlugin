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
package com.cisco.matday.ucsd.hp3par.reports.hostsets.drilldown;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.rest.vluns.rest.VlunResponse;
import com.cisco.matday.ucsd.hp3par.rest.vluns.rest.VlunResponseMembers;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * @author Matt Day
 *
 */
public class HostSetVlunReportImpl implements TabularReportGeneratorIf {

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
		model.addTextColumn("Volume", "Volume");
		model.addTextColumn("LUN", "LUN");
		model.addTextColumn("Status", "Status");
		model.addTextColumn("WWN", "WWN");

		final String hostSet = context.getId().split(";")[1].split("@")[2];

		model.completedHeader();

		HP3ParCredentials credentials = new HP3ParCredentials(context);

		VlunResponse list = HP3ParInventory.getVlunResponse(credentials);

		for (VlunResponseMembers vlun : list.getMembers()) {
			// We only want to match on this volume's children
			if (!vlun.getHostname().equals("set:" + hostSet)) {
				continue;
			}
			// ID format:
			// accountName;lun@accountName@hostName@volumeName
			model.addTextValue(credentials.getAccountName() + ";" + vlun.getLun() + "@" + credentials.getAccountName()
					+ "@" + vlun.getHostname() + "@" + vlun.getVolumeName());
			model.addTextValue(vlun.getVolumeName());
			model.addTextValue(Integer.toString(vlun.getLun()));
			model.addTextValue(vlun.isActive() ? "Active" : "Inactive");
			model.addTextValue(vlun.getVolumeWWN());

			model.completedRow();
		}
		model.updateReport(report);

		return report;
	}
}
