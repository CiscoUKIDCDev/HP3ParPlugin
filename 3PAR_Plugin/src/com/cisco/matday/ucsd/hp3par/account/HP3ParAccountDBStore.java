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
package com.cisco.matday.ucsd.hp3par.account;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.connector.account.AbstractInfraAccount;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.model.cIM.InfraAccount;
import com.cloupia.service.cIM.inframgr.collector.view2.ConnectorCredential;
import com.cloupia.service.cIM.inframgr.forms.wizard.FieldValidation;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;
import com.cloupia.service.cIM.inframgr.forms.wizard.HideFieldOnCondition;

/**
 * This class extends the in-build UCS Director account storage. Don't use it
 * directly, instead use HP3ParCredentials to obtain and manage this
 * information.
 *
 * @author matt
 *
 */
@PersistenceCapable(detachable = "true", table = "HP3Par_account")
public class HP3ParAccountDBStore extends AbstractInfraAccount implements ConnectorCredential {

	static Logger logger = Logger.getLogger(HP3ParAccountDBStore.class);

	@Persistent
	@FormField(label = "Use Credential Policy", validate = true, help = "Select if you want to use policy to give the credentials.", type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	private boolean isCredentialPolicy = false;

	@Persistent
	@FormField(label = "Device Address", help = "Device Address (hostname or IP address)", mandatory = true)
	@HideFieldOnCondition(field = "isCredentialPolicy", op = FieldValidation.OP_EQUALS, value = "false")
	private String array_address;

	@Persistent
	@FormField(label = "TCP Port", help = "TCP Port (default is 8080 for https, 8008 for http)", mandatory = true)
	@HideFieldOnCondition(field = "isCredentialPolicy", op = FieldValidation.OP_EQUALS, value = "false")
	private int tcp_port;

	@Persistent
	@FormField(label = "Username", help = "Username", mandatory = true)
	@HideFieldOnCondition(field = "isCredentialPolicy", op = FieldValidation.OP_EQUALS, value = "false")
	private String username;

	@Persistent
	@FormField(label = "Password", help = "Password", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_PASSWORD)
	@HideFieldOnCondition(field = "isCredentialPolicy", op = FieldValidation.OP_EQUALS, value = "false")
	private String password;

	@Persistent
	@FormField(label = "Use secure connection (https)", help = "Use secure connection (https)", type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	@HideFieldOnCondition(field = "isCredentialPolicy", op = FieldValidation.OP_EQUALS, value = "false")
	private boolean https;

	@Persistent
	@FormField(label = "Validate server certificate", help = "Validate https certificate (disable for labs)", type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	@HideFieldOnCondition(field = "isCredentialPolicy", op = FieldValidation.OP_EQUALS, value = "false")
	private boolean validateCert;

	@Persistent
	@FormField(label = "Poll Interval (minutes)", help = "Between 1 and 60 minutes. -1 to disable (not recommended). Inventory will always be polled after an action.", type = FormFieldDefinition.FIELD_TYPE_NUMBER)
	@HideFieldOnCondition(field = "isCredentialPolicy", op = FieldValidation.OP_EQUALS, value = "false")
	private int polling;

	/**
	 * Do not instantiate this directly, let UCSD do that...
	 */
	public HP3ParAccountDBStore() {
		super();
		this.validateCert = true;
		this.https = true;
		this.tcp_port = HP3ParConstants.DEFAULT_PORT;
		this.polling = (int) (HP3ParConstants.INVENTORY_LIFE / HP3ParConstants.MILLISECOND_TO_MINUTES);
	}

	/**
	 * @return if the certificate should be validated
	 */
	public boolean isValidateCert() {
		return this.validateCert;
	}

	/**
	 * @param validateCert
	 *            if the server certificate should be validated
	 */
	public void setValidateCert(boolean validateCert) {
		this.validateCert = validateCert;
	}

	@Override
	public String getProtocol() {
		return (this.https) ? "https" : "http";
	}

	@Override
	public boolean isCredentialPolicy() {
		return true;
	}

	@Override
	public void setCredentialPolicy(boolean isCredentialPolicy) {
		this.isCredentialPolicy = isCredentialPolicy;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public void setServer(String ip) {
		this.array_address = ip;
	}

	@Override
	public String getServerAddress() {
		return this.array_address;
	}

	@Override
	public String getServer() {
		return this.array_address;
	}

	@Override
	public String getServerIp() {
		return this.array_address;
	}

	// public String

	@Override
	public void setServerAddress(String ip) {
		this.array_address = ip;
	}

	@Override
	public void setServerIp(String ip) {
		this.array_address = ip;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public InfraAccount toInfraAccount() {

		try {

			ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);

			String cquery = "server == '" + this.array_address + "' && userID == '" + this.username
					+ " && validateCert == '" + this.validateCert + "' && transport == " + this.https + "' && port == "
					+ this.tcp_port;

			List<InfraAccount> accList = store.query(cquery);

			if ((accList != null) && (accList.size() > 0)) {
				return accList.get(0);
			}
			return null;

		}
		catch (Exception e) {
			logger.error("Exception while mapping DeviceCredential to InfraAccount for server: " + this.array_address
					+ ": " + e.getMessage());
		}

		return null;

	}

	@SuppressWarnings("javadoc")
	public String getArray_address() {
		return this.array_address;
	}

	@SuppressWarnings("javadoc")
	public void setArray_address(String array_address) {
		this.array_address = array_address;
	}

	@SuppressWarnings("javadoc")
	public int getTcp_port() {
		return this.tcp_port;
	}

	@SuppressWarnings("javadoc")
	public void setTcp_port(int tcp_port) {
		this.tcp_port = tcp_port;
	}

	@SuppressWarnings("javadoc")
	public boolean isHttps() {
		return this.https;
	}

	@SuppressWarnings("javadoc")
	public void setHttps(boolean https) {
		this.https = https;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getPolicy() {
		return null;
	}

	@SuppressWarnings("javadoc")
	public String getDeviceIp() {
		return this.array_address;
	}

	@SuppressWarnings("javadoc")
	public void setDeviceIp(String deviceIp) {
		this.array_address = deviceIp;
	}

	@SuppressWarnings("javadoc")
	public String getLogin() {
		return this.username;
	}

	@SuppressWarnings("javadoc")
	public void setLogin(String login) {
		this.username = login;
	}

	@Override
	public void setPolicy(String policy) {
		return;
	}

	@Override
	public void setPort(int port) {
		this.tcp_port = port;

	}

	@Override
	public void setUsername(String userName) {
		this.username = userName;
	}

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            the logger to set
	 */
	public static void setLogger(Logger logger) {
		HP3ParAccountDBStore.logger = logger;
	}

	/**
	 * @return the polling interval in minutes
	 */
	public int getPolling() {
		if (this.polling == 0) {
			return (int) (HP3ParConstants.INVENTORY_LIFE / HP3ParConstants.MILLISECOND_TO_MINUTES);
		}
		return this.polling;
	}

	/**
	 * @param polling
	 *            the polling to set in minutes
	 */
	public void setPolling(int polling) {
		this.polling = polling;
	}

}
