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
package com.cisco.matday.ucsd.hp3par.reports.summary;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.model.cIM.ReportDefinition;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaNonTabularReport;

/**
 * Build a simple overview table with stats about the array
 * 
 * @author Matt Day
 *
 */
public class OverviewTable extends CloupiaNonTabularReport {
	/**
	 * Unique identifier for this report
	 */
	public final static String REPORT_NAME = "com.cisco.matday.ucsd.hp3par.reports.OverviewTable";
	private static final String REPORT_LABEL = "Summary";

	/**
	 * This method returns implementation class of the summary report
	 * 
	 * @return implementation class
	 */
	@Override
	public Class<OverviewTableImpl> getImplementationClass() {
		return OverviewTableImpl.class;
	}

	/**
	 * Create overview table
	 */
	public OverviewTable() {
		super();
		this.setMgmtColumnIndex(1);
	}

	/**
	 * This method returns the report label to be display in UI
	 * 
	 * @return label of report
	 */
	@Override
	public String getReportLabel() {
		return REPORT_LABEL;
	}

	/**
	 * Important - override default menu ID number
	 */
	@Override
	public int getMenuID() {
		return 51;
	}

	/**
	 * @return This method returns report name ,each report should have unique
	 *         name
	 */
	@Override
	public String getReportName() {
		return REPORT_NAME;
	}

	/**
	 * @return This report returns boolean value true/false. it returns false if
	 *         report is not easy(report has implementation class)
	 */
	@Override
	public boolean isEasyReport() {
		return false;
	}

	/**
	 * @return This report returns boolean value true/false. Returns true if it
	 *         is leaf report otherwise it returns false
	 */
	@Override
	public boolean isLeafReport() {
		return false;
	}

	/**
	 * @return This method returns type of report like summary/pie chart/Line
	 *         chart/tabular etc
	 */
	@Override
	public int getReportType() {
		return ReportDefinition.REPORT_TYPE_SUMMARY;
	}

	/**
	 * @return This method returns type of report
	 */
	@Override
	public int getReportHint() {
		return ReportDefinition.REPORT_HINT_VERTICAL_TABLE_WITH_GRAPHS;
	}

	/**
	 * @return This method returns boolean value true/false. if it returns true
	 *         then only form will be shown in UI
	 */
	@Override
	public boolean isManagementReport() {
		return true;
	}

	@Override
	public ContextMapRule[] getMapRules() {
		DynReportContext context = ReportContextRegistry.getInstance()
				.getContextByName(HP3ParConstants.INFRA_ACCOUNT_TYPE);
		ContextMapRule rule = new ContextMapRule();
		rule.setContextName(context.getId());
		rule.setContextType(context.getType());

		ContextMapRule[] rules = new ContextMapRule[1];
		rules[0] = rule;

		return rules;
	}

}
