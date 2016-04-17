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

/**
 * Implements the inventory collector. This doesn't do much today
 *
 * @author Matt Day
 *
 */

public class HP3ParInventoryItemHandler extends AbstractInventoryItemHandler {

	private static Logger logger = Logger.getLogger(HP3ParInventoryItemHandler.class);

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
	 * @exception Exception
	 */
	@Override
	public void doInventory(String accountName, InventoryContext inventoryCtxt) throws Exception {
		this.doInventory(accountName);

	}

	/**
	 * This method used for do Inventory of Account
	 *
	 * @Override method of IInventoryItemHandlerIf interface
	 * @param accountName
	 *            ,Object
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
	 * @exception Exception
	 */
	private void doInventory(String accountName) throws Exception {

		// HP3ParAccountAPI api = HP3ParAccountAPI.getHP3ParAccountAPI(acc);

		/**
		 * To provide the real implemntation , depends on the respond data
		 * binder for the requested item. To ensure the data collecting for the
		 * inventory via HTTP / SSH connection. Response is converted as JSON
		 * Data, Json Data is binded with the
		 */

		// String jsonData = api.getInventoryData(getUrl());

		final String jsonData = null;
		final ItemResponse bindableResponse = new ItemResponse();
		bindableResponse.setContext(this.getContext(accountName));
		bindableResponse.setCollectedData(jsonData);
		ItemResponse bindedResponse = null;
		logger.info("Before Callng bind");

		final HP3ParJSONBinder binder = this.getBinder();
		if (binder != null) {
			bindedResponse = binder.bind(bindableResponse);
			logger.info(bindedResponse.getItem().getLabel());

		}

		logger.info("after Calling bind");

		final PersistenceListener listener = this.getListener();
		if (listener != null) {
			logger.info("Calling for Persistence");
			if (bindedResponse == null) {
				logger.info("bindedResponse is null");
			}
			listener.persistItem(bindedResponse);
		}
		else {
			logger.info("Persistence is null");
		}

	}

	/**
	 * Method used for get Url
	 *
	 * @return String
	 */
	@SuppressWarnings("static-method")
	public String getUrl() {
		// TODO Auto-generated method stub
		return "platform/1/protocols/smb/shares";
	}

	/**
	 * Method used to get object of HP3ParAccountJSONBinder Binder will bind the
	 * respective object as JSON.
	 *
	 * @return HP3ParAccountJSONBinder
	 */
	@SuppressWarnings("static-method")
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
	@SuppressWarnings("static-method")
	private Map<String, Object> getContext(String accountName) {
		return null;
	}

	/**
	 * Private Method used to get Object of PersistenceListener
	 *
	 * @param No
	 * @return PersistenceListener
	 * @exception No
	 */
	@SuppressWarnings("static-method")
	private PersistenceListener getListener() {
		// TODO Auto-generated method stub
		return new HP3ParCollectorInventoryPersistenceListener();
	}

}
