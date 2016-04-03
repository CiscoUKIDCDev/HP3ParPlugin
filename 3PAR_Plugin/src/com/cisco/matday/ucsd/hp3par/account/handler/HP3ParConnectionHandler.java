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
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalConnectivityTestHandler;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;

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
					logger.info("Testing connectivity for account " + HP3ParConstants.INFRA_ACCOUNT_TYPE);
					PhysicalInfraAccount acc = AccountUtil.getAccountByName(accountName);
					HP3ParCredentials t = new HP3ParCredentials(acc);
					try {
						// Got a token, set as OK if it's not null
						HP3ParToken token = new HP3ParToken(t);

						String token_string = null;
						token_string = token.getToken();
						// String token = t.getToken();
						if (token_string == null) {
							logger.warn("Couldn't get token from system - probably invalid credentials");
							status.setConnectionOK(false);
							status.setErrorMsg("Could not get authentication token (check credentials)");
						} else {
							logger.debug("Token acquired - connection verified");
							status.setConnectionOK(true);
							StorageAccountStatusSummary.accountSummary(accountName);
						}
						if (token != null) {
							token.release();
						}
					} catch (Exception e) {
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
