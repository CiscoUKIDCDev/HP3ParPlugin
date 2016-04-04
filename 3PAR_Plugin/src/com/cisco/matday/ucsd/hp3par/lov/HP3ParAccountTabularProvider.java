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
package com.cisco.matday.ucsd.hp3par.lov;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.cloupia.lib.connector.account.AccountTypeEntry;
import com.cloupia.lib.connector.account.PhysicalAccountTypeManager;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * I've deprecated this as I'm not sure what it actually does! Until I can
 * figure it out, I'll leave it this way to flag up in code that might use it...
 * 
 * @author matt
 *
 */
@Deprecated
public class HP3ParAccountTabularProvider implements TabularReportGeneratorIf {

	private static Logger logger = Logger.getLogger(HP3ParAccountTabularProvider.class);

	public static final String TABULAR_PROVIDER = "hp3par_account_tabular_provider";
	private static final int NUM_ROWS = 8;

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		logger.warn("HP3PAR Tabular Report API called!");
		// Get list of accounts:
		// for (Iterator<VolumeResponseMembers> i =
		// newlist.getVolume().getMembers().iterator(); i.hasNext();) {
		for (Iterator<AccountTypeEntry> i = PhysicalAccountTypeManager.getInstance().getPhysicalAccountTypes()
				.iterator(); i.hasNext();) {
			AccountTypeEntry a = i.next();
			logger.warn("Got account type: " + a.getAccountType());
		}

		TabularReport report = new TabularReport();
		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Value", "Value");
		model.completedHeader();

		for (int i = 0; i < NUM_ROWS; i++) {
			model.addTextValue("name" + i);
			model.addTextValue("value" + i);
			model.completedRow();
		}

		model.updateReport(report);

		return report;
	}

}
