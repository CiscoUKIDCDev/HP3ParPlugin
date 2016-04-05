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
package com.cisco.matday.ucsd.hp3par.account.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloupia.service.cIM.inframgr.collector.model.ItemResponse;

// This is boilerplate required by the API, don't document
@SuppressWarnings("javadoc")
public class HP3ParAccountJSONBinder extends HP3ParJSONBinder {
	private static Logger logger = Logger.getLogger(HP3ParAccountJSONBinder.class);

	@Override
	public ItemResponse bind(ItemResponse bindable) {
		String jsonData = bindable.getCollectedData();
		logger.debug("RAW JSON Data" + jsonData);

		if (jsonData != null && !jsonData.isEmpty()) {
			// Json data to be converted as target object

		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Class> getPersistantClassList() {
		List<Class> cList = new ArrayList<Class>();
		// add the Persistant class in the CList , for reference.
		return cList;
	}
}