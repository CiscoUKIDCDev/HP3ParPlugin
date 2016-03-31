package com.cisco.matday.ucsd.hp3par.account;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.log4j.Logger;

import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.connector.account.AbstractInfraAccount;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.model.cIM.InfraAccount;
import com.cloupia.service.cIM.inframgr.collector.view2.ConnectorCredential;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;


@PersistenceCapable(detachable = "true", table = "hp3par_account")
public class HP3ParAccount extends AbstractInfraAccount implements
		ConnectorCredential {

	static Logger logger = Logger.getLogger(HP3ParAccount.class);
	private final static int DEFAULT_PORT = 8080;
	
	@Persistent
	private boolean isCredentialPolicy = false;
	
	@Persistent
	@FormField(label = "Device Address", help = "Device Address", mandatory = true)
	private String array_address;
	
	@Persistent
	@FormField(label = "Username", help = "Username", mandatory = true)
	private String username;
	
	@Persistent
	@FormField(label = "Password", help = "Password", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_PASSWORD)
	private String password;
	
	@Persistent
	@FormField(label = "TCP Port (Optional)", help = "TCP Port")
	private int tcp_port = DEFAULT_PORT;
	
	@FormField(label = "Use https", help = "Use secure http (https)",type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	private boolean https = true;
	

	public HP3ParAccount() {
		
	}
	
	@Override
	public boolean isCredentialPolicy() {
		return false;
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
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public InfraAccount toInfraAccount() {
		
		try {
			
			ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);
			
			String cquery = "server == '" + array_address + "' && userID == '" 
					+ username + "' && transport == " + https + "' && port == "
					+ tcp_port;
			
			logger.info("query = " + cquery);
			
			List<InfraAccount> accList = store.query(cquery);
			
			if (accList != null && accList.size() > 0) {
				return accList.get(0);
			}
			else {
				return null;
			}
			
			
		} catch(Exception e) {
			logger.error("Exception while mapping DeviceCredential to InfraAccount for server: "
					+ array_address + ": " + e.getMessage());
		}
		
		return null;
		
	}

	public String getArray_address() {
		return array_address;
	}

	public void setArray_address(String array_address) {
		this.array_address = array_address;
	}

	public int getTcp_port() {
		return tcp_port;
	}

	public void setTcp_port(int tcp_port) {
		this.tcp_port = tcp_port;
	}

	public boolean isHttps() {
		return https;
	}

	public void setHttps(boolean https) {
		this.https = https;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public String getPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPolicy(String policy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPort(int port) {
		tcp_port = port;
		
	}

	@Override
	public void setUsername(String userName) {
		username = userName;
	}


	
}
