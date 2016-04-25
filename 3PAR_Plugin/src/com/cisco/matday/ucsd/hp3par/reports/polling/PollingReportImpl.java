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
package com.cisco.matday.ucsd.hp3par.reports.polling;

import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
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
public class PollingReportImpl implements TabularReportGeneratorIf {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(PollingReportImpl.class);

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTimeColumn("Start Time", "Start Time");
		model.addTextColumn("Type", "Type");
		model.addTextColumn("Comment", "Comment");
		model.addTimeColumn("End Time", "End Time");

		model.completedHeader();

		List<String> pollingList = HP3ParInventory.getPollingResponse(new HP3ParCredentials(context));

		// Init iterator on last element to trawl backwards
		ListIterator<String> i = pollingList.listIterator(pollingList.size());

		// Reverse through list (bottom to top)
		while (i.hasPrevious()) {
			// Format:
			// Start@End@Forced@Comment
			String[] pollArray = i.previous().split("@");
			// Start time
			model.addTimeValue(Long.parseLong(pollArray[0]));
			if (pollArray[2].equals("false")) {
				model.addTextValue("Regular poll");
			}
			else {
				model.addTextValue("Forced update");
			}
			model.addTextValue(pollArray[3]);
			// End time
			model.addTimeValue(Long.parseLong(pollArray[1]));

			model.completedRow();
		}

		model.updateReport(report);

		return report;

	}

}
