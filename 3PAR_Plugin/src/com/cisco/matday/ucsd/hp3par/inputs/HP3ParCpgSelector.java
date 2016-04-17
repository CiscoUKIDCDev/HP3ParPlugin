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

import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponse;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMember;
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
 * Table to allow selection of an HP 3PAR CPGs - it should not be instantiated
 * directly but instead used as a form item
 *
 * @author Matt Day
 *
 */
public class HP3ParCpgSelector implements TabularReportGeneratorIf {

	private static Logger logger = Logger.getLogger(HP3ParCpgSelector.class);

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
		model.addTextColumn("Account", "Account");
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Pod", "Pod");
		model.addTextColumn("Virtual Volumes", "Virtual Volumes");
		model.completedHeader();

		for (InfraAccount a : objs) {
			PhysicalInfraAccount acc = AccountUtil.getAccountByName(a.getAccountName());
			// TODO: This makes the null conditionals later invalid, but put in
			// as a test
			if (acc == null) {
				logger.info("3PAR plugin CPG selector: acc == null");
				continue;
			}
			if (acc.getAccountType() == null) {
				logger.info("3PAR plugin CPG selector: acc.getAccountType() == null");
				continue;
			}
			// Important to check if the account type is null first
			if ((acc.getAccountType() != null) && (acc.getAccountType().equals(HP3ParConstants.INFRA_ACCOUNT_TYPE))) {

				CPGResponse cpgList = HP3ParInventory.getCPGResponse(a.getAccountName());

				for (CPGResponseMember cpg : cpgList.getMembers()) {
					// Bad but we can use this to parse it all out later
					// Format: ID@AccountName@CPGName
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
