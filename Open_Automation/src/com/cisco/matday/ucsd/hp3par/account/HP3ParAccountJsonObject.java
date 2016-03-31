package com.cisco.matday.ucsd.hp3par.account;

public class HP3ParAccountJsonObject {
	private String account;
	private String array_address;
	private String dnDetachedState;
	private String isCredentialPolicy;
	private String username;
	private String password;
	private int tcp_port;
	private boolean https;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getArray_address() {
		return array_address;
	}
	public void setArray_address(String array_address) {
		this.array_address = array_address;
	}
	public String getDnDetachedState() {
		return dnDetachedState;
	}
	public void setDnDetachedState(String dnDetachedState) {
		this.dnDetachedState = dnDetachedState;
	}
	public String getIsCredentialPolicy() {
		return isCredentialPolicy;
	}
	public void setIsCredentialPolicy(String isCredentialPolicy) {
		this.isCredentialPolicy = isCredentialPolicy;
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

}
