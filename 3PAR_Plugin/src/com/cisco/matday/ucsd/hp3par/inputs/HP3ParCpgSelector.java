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

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.cpg.HP3ParCPG;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMembers;
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

public class HP3ParCpgSelector implements TabularReportGeneratorIf {

	private static Logger logger = Logger.getLogger(HP3ParAccountSelector.class);

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		logger.warn("Building tabular report of accounts");

		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);
		List<InfraAccount> objs = store.queryAll();

		logger.info("Number of entries: " + objs.size());

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("InternalID", "InternalID", true);
		model.addTextColumn("ID", "ID");
		model.addTextColumn("Account", "Account");
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Pod", "Pod");
		model.addTextColumn("Virtual Volumes", "Virtual Volumes");
		model.completedHeader();

		for (Iterator<InfraAccount> i = objs.iterator(); i.hasNext();) {
			InfraAccount a = i.next();
			PhysicalInfraAccount acc = AccountUtil.getAccountByName(a.getAccountName());
			if (acc.getAccountType().equals(HP3ParConstants.INFRA_ACCOUNT_TYPE)) {
				logger.info("Account list got: " + a.getAccountName());

				HP3ParCPG cpglist = new HP3ParCPG(new HP3ParCredentials(acc));

				for (Iterator<CPGResponseMembers> j = cpglist.getCpg().getMembers().iterator(); j.hasNext();) {
					CPGResponseMembers cpg = j.next();

					// Bad but we can use this to parse it all out later
					String internalId = Integer.toString(cpg.getId()) + "@" + a.getAccountName() + "@" + cpg.getName();

					// Internal ID
					model.addTextValue(internalId);
					// ID
					model.addTextValue(Integer.toString(cpg.getId()));
					// Account Name
					model.addTextValue(a.getAccountName());
					// Name
					model.addTextValue(cpg.getName());
					// Pod
					model.addTextValue(a.getDcName());
					// Volume Count
					int totalVol = cpg.getNumTPVVs() + cpg.getNumFPVVs();
					model.addTextValue(Integer.toString(totalVol));
					model.completedRow();
				}
			}

		}
		model.updateReport(report);

		return report;
	}

}
