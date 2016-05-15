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
package com.cisco.matday.ucsd.hp3par.account.inventory;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParAccountException;
import com.cisco.matday.ucsd.hp3par.rest.HP3ParToken;
import com.cisco.matday.ucsd.hp3par.rest.system.json.SystemResponse;
import com.cloupia.model.cIM.ConvergedStackComponentDetail;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reports.contextresolve.ConvergedStackComponentBuilderIf;

/**
 * Implements the stack view
 *
 * @author Matt Day
 *
 */
public class HP3ParConvergedStackBuilder implements ConvergedStackComponentBuilderIf {

	static Logger logger = Logger.getLogger(HP3ParConvergedStackBuilder.class);

	/**
	 * Overridden method from SDK
	 *
	 * @param contextId
	 *            account context Id
	 *
	 * @return: returns ConvergedStackComponentDetail instance
	 */
	@Override
	public ConvergedStackComponentDetail buildConvergedStackComponent(String contextId)
			throws HP3ParAccountException, Exception {
		String accountName = null;
		if (contextId != null) {
			// As the contextId returns as: "account Name;POD Name"
			accountName = contextId.split(";")[0];
		}
		if (accountName == null) {
			throw new HP3ParAccountException("Unable to find the account name");
		}

		SystemResponse systemInfo = null;

		HP3ParCredentials credentials = new HP3ParCredentials(accountName);

		// Test connectivity
		boolean ok = false;
		try {
			HP3ParToken token = new HP3ParToken(credentials);
			if (token.getToken() != null) {
				ok = true;
				token.release();
			}
		}
		catch (@SuppressWarnings("unused") Exception e) {
			// Do nothing; status is already 'false'
		}

		try {
			systemInfo = HP3ParInventory.getSystemResponse(credentials);
		}
		catch (Exception e) {
			logger.warn("Couldn't populate account: " + e.getMessage());
			systemInfo = new SystemResponse();
			systemInfo.setName("HP 3PAR");
			systemInfo.setSystemVersion("N/A");
			systemInfo.setIPv4Addr("");
			systemInfo.setModel("");
			systemInfo.setTotalCapacityMiB(0);
		}

		final ConvergedStackComponentDetail detail = new ConvergedStackComponentDetail();

		detail.setModel(systemInfo.getModel());

		detail.setOsVersion(systemInfo.getSystemVersion());
		detail.setVendorLogoUrl("/app/uploads/openauto/3Par_Icon.png");
		detail.setMgmtIPAddr(systemInfo.getIPv4Addr());
		detail.setStatus(ok ? "OK" : "Down");
		detail.setVendorName("HP3PAR");

		detail.setLabel("System Name:" + systemInfo.getName());

		detail.setIconUrl("/app/uploads/openauto/3Par_Icon.png");
		// setting account context type
		detail.setContextType(
				ReportContextRegistry.getInstance().getContextByName(HP3ParConstants.INFRA_ACCOUNT_TYPE).getType());
		// setting context value that should be passed to report implementation
		detail.setContextValue(contextId);
		detail.setLayerType(3);
		detail.setComponentSummaryList(HP3ParConvergedStackBuilder.getSummaryReports(systemInfo, credentials));
		return detail;
	}

	private static List<String> getSummaryReports(SystemResponse systemInfo, HP3ParCredentials credentials)
			throws Exception {
		final List<String> rpSummaryList = new ArrayList<>();
		rpSummaryList.add("System Name ," + systemInfo.getName());
		rpSummaryList.add("Account Name ," + credentials.getAccountName());
		rpSummaryList.add("Nodes ," + systemInfo.getTotalNodes());
		rpSummaryList.add("Capacity (GiB) ," + (systemInfo.getTotalCapacityMiB() / 1024d));
		rpSummaryList.add("Free (GiB) ," + (systemInfo.getFreeCapacityMiB() / 1024d));

		return rpSummaryList;

	}
}
