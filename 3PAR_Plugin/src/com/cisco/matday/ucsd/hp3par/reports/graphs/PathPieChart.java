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
package com.cisco.matday.ucsd.hp3par.reports.graphs;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.model.cIM.ReportDefinition;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaNonTabularReport;

/**
 * Bar chart view to show CPGs by volume
 *
 * @author Matt Day
 *
 */
public class PathPieChart extends CloupiaNonTabularReport {

	// private static final String NAME =
	// "com.cisco.matday.ucsd.hp3par.reports.CPGBarChartReport";
	private static final String NAME = "com.cisco.matday.ucsd.hp3par.reports.graphs.PathBarChartReport";
	private static final String LABEL = "Path Types";
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(PathPieChart.class);

	/**
	 * @return BarChartReport implementation class type
	 */
	@Override
	public Class<PathPieChartReport> getImplementationClass() {
		return PathPieChartReport.class;
	}

	/**
	 * Initialise and set management column index
	 */
	public PathPieChart() {
		super();
		// IMPORTANT: this tells the framework which column of this report you
		// want to pass as the report context id
		// when there is a UI action being launched in this report
		this.setMgmtColumnIndex(1);
	}

	// Returns report type for pie chart as shown below
	@Override
	public int getReportType() {
		return ReportDefinition.REPORT_TYPE_SNAPSHOT;
	}

	@Override
	public String getReportLabel() {
		return LABEL;
	}

	@Override
	public boolean isLeafReport() {
		return true;
	}

	@Override
	public String getReportName() {
		return NAME;
	}

	// Forcing this report into the Physical->Storage part of the GUI.
	@Override
	public int getMenuID() {
		return 51;
	}

	/**
	 * @return true if you want this chart to show up in a summary report
	 */
	@Override
	public boolean showInSummary() {
		return true;
	}

	// Returns report hint for pie chart as shown below
	@Override
	public int getReportHint() {
		return ReportDefinition.REPORT_HINT_PIECHART;
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
