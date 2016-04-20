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
package com.cisco.matday.ucsd.hp3par.rest.volumes.json;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;

/**
 * REST representation of a 3PAR request
 *
 * See the HP 3PAR documentation for more detail
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "hp3par_volume_response_member_policy_v1")
public class VolumeResponseMembersPolicies implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8170216187766198511L;

	@SuppressWarnings("javadoc")
	public boolean isStaleSS() {
		return this.staleSS;
	}

	@SuppressWarnings("javadoc")
	public void setStaleSS(boolean staleSS) {
		this.staleSS = staleSS;
	}

	@SuppressWarnings("javadoc")
	public boolean isOneHost() {
		return this.oneHost;
	}

	@SuppressWarnings("javadoc")
	public void setOneHost(boolean oneHost) {
		this.oneHost = oneHost;
	}

	@SuppressWarnings("javadoc")
	public boolean isZeroDetect() {
		return this.zeroDetect;
	}

	@SuppressWarnings("javadoc")
	public void setZeroDetect(boolean zeroDetect) {
		this.zeroDetect = zeroDetect;
	}

	@SuppressWarnings("javadoc")
	public boolean isSystem() {
		return this.system;
	}

	@SuppressWarnings("javadoc")
	public void setSystem(boolean system) {
		this.system = system;
	}

	@SuppressWarnings("javadoc")
	public boolean isCaching() {
		return this.caching;
	}

	@SuppressWarnings("javadoc")
	public void setCaching(boolean caching) {
		this.caching = caching;
	}

	private boolean staleSS;
	private boolean oneHost;
	private boolean zeroDetect;
	private boolean system;
	private boolean caching;
}
