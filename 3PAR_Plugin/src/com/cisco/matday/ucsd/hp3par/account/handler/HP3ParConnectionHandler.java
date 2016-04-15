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
package com.cisco.matday.ucsd.hp3par.account.handler;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.status.StorageAccountStatusSummary;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.HP3ParToken;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalConnectivityTestHandler;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;

/**
 * Used to test if the array is reachable, especially when creating a new
 * account
 * 
 * @author Matt Day
 *
 */
public class HP3ParConnectionHandler extends PhysicalConnectivityTestHandler {
	static Logger logger = Logger.getLogger(HP3ParConnectionHandler.class);

	@Override
	public PhysicalConnectivityStatus testConnection(String accountName) throws Exception {

		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(accountName);
		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(infraAccount);

		status.setConnectionOK(false);
		if (infraAccount != null) {
			if (infraAccount.getAccountType() != null) {
				logger.info(infraAccount.getAccountType());

				// Instead of Nimble Account , real account type needs to
				// specified.
				if (infraAccount.getAccountType().equals(HP3ParConstants.INFRA_ACCOUNT_TYPE)) {
					logger.debug("Testing connectivity for account " + HP3ParConstants.INFRA_ACCOUNT_TYPE);
					HP3ParCredentials t = new HP3ParCredentials(accountName);
					try {
						// Got a token, set as OK if it's not null
						HP3ParToken token = new HP3ParToken(t);
						// Don't care about output, only want to see if it
						// throws an exception
						token.getToken();
						logger.debug("Token acquired - connection verified");
						status.setConnectionOK(true);
						StorageAccountStatusSummary.accountSummary(accountName);
						token.release();
					}
					catch (@SuppressWarnings("unused") InvalidHP3ParTokenException e) {
						logger.warn("Couldn't get token from system - probably invalid credentials");
						status.setConnectionOK(false);
						status.setErrorMsg("Could not get authentication token (check credentials)");
					}
					catch (@SuppressWarnings("unused") Exception e) {
						logger.warn("Exception raised testing connection - probably wrong IP address or unreachable");
						// Didn't get a token
						status.setConnectionOK(false);
						status.setErrorMsg("Array not found (check IP address or hostname)");
					}

				}
			}

		}
		logger.debug("Returning 3PAR status " + status.isConnectionOK());
		return status;
	}

}
