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

import java.util.Map;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.api.HP3ParAccountJSONBinder;
import com.cisco.matday.ucsd.hp3par.account.api.HP3ParJSONBinder;
import com.cloupia.lib.connector.AbstractInventoryItemHandler;
import com.cloupia.lib.connector.InventoryContext;
import com.cloupia.service.cIM.inframgr.collector.controller.PersistenceListener;
import com.cloupia.service.cIM.inframgr.collector.model.ItemResponse;

public class HP3ParInventoryItemHandler extends AbstractInventoryItemHandler {

	/**
	 * This class is used to provide the implementation for Nimble Account
	 * Inventory Item handlers. While writing any new Item handler for any
	 * Nimble Account inventory task, that should extend this
	 * AbstractInventoryItemHandler Class. This provides the flexible way to
	 * override the url, binder and listener objects.
	 * 
	 * This class provides the common logic to collect the inventory data and
	 * persist into the respective persistable POJO objects and cleanup the data
	 * while deleting the Nimble account.
	 */

	private static Logger logger = Logger.getLogger(HP3ParInventoryItemHandler.class);

	/**
	 * This method used for cleanup Item Inventory. method of
	 * InventoryItemHandlerIf interface
	 * 
	 * @param accountName
	 * @return void
	 * @exception Exception
	 */
	@Override
	public void cleanup(String accountName) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * This method used for do Inventory of Account
	 * 
	 * @Override method of IInventoryItemHandlerIf interface
	 * @param accountName
	 *            ,InventoryContext
	 * @return void
	 * @exception Exception
	 */
	@Override
	public void doInventory(String accountName, InventoryContext inventoryCtxt) throws Exception {
		doInventory(accountName);

	}

	/**
	 * This method used for do Inventory of Account
	 * 
	 * @Override method of IInventoryItemHandlerIf interface
	 * @param accountName
	 *            ,Object
	 * @return void
	 * @exception Exception
	 */
	@Override
	public void doInventory(String accountName, Object obj) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * private Method used for doing Inventory of Account
	 * 
	 * @param accountName
	 * @return void
	 * @exception Exception
	 */
	private void doInventory(String accountName) throws Exception {

		// NimbleAccountAPI api = NimbleAccountAPI.getNimbleAccountAPI(acc);

		/**
		 * To provide the real implemntation , depends on the respond data
		 * binder for the requested item. To ensure the data collecting for the
		 * inventory via HTTP / SSH connection. Response is converted as JSON
		 * Data, Json Data is binded with the
		 */

		// String jsonData = api.getInventoryData(getUrl());
		String jsonData = null;
		ItemResponse bindableResponse = new ItemResponse();
		bindableResponse.setContext(getContext(accountName));
		bindableResponse.setCollectedData(jsonData);
		ItemResponse bindedResponse = null;
		logger.info("Before Callng bind");

		HP3ParJSONBinder binder = getBinder();
		if (binder != null) {
			bindedResponse = binder.bind(bindableResponse);

		}

		logger.info("after Calling bind");

		PersistenceListener listener = getListener();
		if (listener != null) {
			logger.info("Calling for Persistence");
			listener.persistItem(bindedResponse);
		} else {
			logger.info("Persistence is null");
		}

	}

	/**
	 * Method used for get Url
	 * 
	 * @param No
	 * @return String
	 * @exception No
	 */
	public String getUrl() {
		// TODO Auto-generated method stub
		return "platform/1/protocols/smb/shares";
	}

	/**
	 * Method used to get object of NimbleAccountJSONBinder Binder will bind the
	 * respective object as JSON.
	 * 
	 * @param No
	 * @return NimbleAccountJSONBinder
	 * @exception No
	 */
	public HP3ParAccountJSONBinder getBinder() {
		// TODO Auto-generated method stub
		return new HP3ParAccountJSONBinder();
	}

	/**
	 * Private Method used to get Map of Context
	 * 
	 * @param accountName
	 * @return Map<String, Object>
	 * @exception No
	 */
	private Map<String, Object> getContext(String accountName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Private Method used to get Object of PersistenceListener
	 * 
	 * @param No
	 * @return PersistenceListener
	 * @exception No
	 */
	private PersistenceListener getListener() {
		// TODO Auto-generated method stub
		return new HP3ParCollectorInventoryPersistenceListener();
	}

}
