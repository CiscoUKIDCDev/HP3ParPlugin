package com.cisco.matday.ucsd.hp3par.account;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.cisco.cuic.api.client.JSON;
import com.cisco.matday.ucsd.hp3par.rest.HP3ParToken;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;

/**
 * 
 * @author matt
 *
 */
public class HP3ParCredentials {
	private final static int DEFAULT_PORT = 8080;
	private int tcp_port;
	private boolean https;
	private String array_address;
	private String username;
	private String password;
	private HP3ParToken token;
	static Logger logger = Logger.getLogger(HP3ParCredentials.class);

	public HP3ParCredentials(String array_address, String username, String password, boolean https, int http_port) {
		init(array_address, username, password, https, http_port);
	}

	public HP3ParCredentials(String array_address, String username, String password, boolean https) {
		init(array_address, username, password, https, DEFAULT_PORT);
	}

	public HP3ParCredentials(PhysicalInfraAccount acc) throws Exception {
		String json = acc.getCredential();
		HP3ParAccountJsonObject account = (HP3ParAccountJsonObject) JSON.jsonToJavaObject(json,
				HP3ParAccountJsonObject.class);
		String username = account.getUsername();
		String password = account.getPassword();
		String array_address = account.getArray_address();
		boolean https = account.isHttps();
		int tcp_port = account.getTcp_port();
		init(array_address, username, password, https, tcp_port);
	}

	public String getToken() throws HttpException, IOException {
		if (this.token == null) {
			this.token = new HP3ParToken(this);
			logger.info("Got token from 3PAR: " + token.getToken());
		}
		logger.info("Returning token: " + token.getToken());
		return token.getToken();
	}

	public void setToken(HP3ParToken token) {
		this.token = token;
	}

	public String getProtocol() {
		return (https) ? "https" : "http";
	}

	private void init(String array_address, String username, String password, boolean https, int http_port) {
		this.array_address = array_address;
		this.username = username;
		this.password = password;
		this.https = https;
		this.tcp_port = http_port;
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

	public String getArray_address() {
		return array_address;
	}

	public void setArray_address(String array_address) {
		this.array_address = array_address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
