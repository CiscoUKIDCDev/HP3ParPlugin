package com.cisco.matday.ucsd.hp3par.account.api;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloupia.service.cIM.inframgr.collector.model.ItemResponse;

public class HP3ParAccountJSONBinder  extends HP3ParJSONBinder {
	private static Logger logger = Logger.getLogger(HP3ParAccountJSONBinder.class);
	
	@Override
	public ItemResponse bind(ItemResponse bindable) {
		String jsonData = bindable.getCollectedData();
		logger.debug("RAW JSON Data" + jsonData);
		
		if( jsonData != null && !jsonData.isEmpty())
		{
			//Json data to be converted as target object 
		
		}
		
		return null;
	}

	@Override
	public List<Class> getPersistantClassList() {
		List<Class> cList = new ArrayList<Class>();
		//add the Persistant class in the CList , for reference.
		return cList;
	}
}