package com.cisco.matday.ucsd.hp3par.account.inventory;

import com.cloupia.model.cIM.ConvergedStackComponentDetail;
import com.cloupia.service.cIM.inframgr.reports.contextresolve.ConvergedStackComponentBuilderIf;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cloupia.model.cIM.ReportContextRegistry;

public class HP3ParConvergedStackBuilder implements ConvergedStackComponentBuilderIf {

	static Logger logger = Logger.getLogger(HP3ParConvergedStackBuilder.class);

	/**
	 * Need to override this method providing account details to be shown in
	 * converged view.
	 * 
	 * @param :
	 *            account context Id
	 * 
	 * @return: returns ConvergedStackComponentDetail instance
	 */
	@Override
	public ConvergedStackComponentDetail buildConvergedStackComponent(String contextId) throws Exception {

		HP3ParCredentials c = new HP3ParCredentials(contextId);
		HP3ParSystem systemInfo = new HP3ParSystem(c);

		ConvergedStackComponentDetail detail = new ConvergedStackComponentDetail();

		detail.setModel(systemInfo.getSystem().getModel());

		detail.setOsVersion(systemInfo.getSystem().getSystemVersion());
		detail.setVendorLogoUrl("/app/uploads/openauto/3Par_Icon.png");
		detail.setMgmtIPAddr(systemInfo.getSystem().getIPv4Addr());
		detail.setStatus("OK");
		detail.setVendorName("HP3PAR");

		detail.setLabel("System Name:" + systemInfo.getSystem().getName());

		detail.setIconUrl("/app/uploads/openauto/3Par_Icon.png");
		// setting account context type
		detail.setContextType(
				ReportContextRegistry.getInstance().getContextByName(HP3ParConstants.INFRA_ACCOUNT_TYPE).getType());
		// setting context value that should be passed to report implementation
		detail.setContextValue(contextId);
		detail.setLayerType(3);
		detail.setComponentSummaryList(getSummaryReports());
		return detail;
	}

	private List<String> getSummaryReports() throws Exception {

		List<String> rpSummaryList = new ArrayList<String>();
		rpSummaryList.add("test");
		rpSummaryList.add("test2");
		return rpSummaryList;

	}
}
