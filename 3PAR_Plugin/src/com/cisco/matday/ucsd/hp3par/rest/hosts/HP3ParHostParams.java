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
package com.cisco.matday.ucsd.hp3par.rest.hosts;

import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseDescriptors;

/**
 * @author Matt Day
 *
 */
public class HP3ParHostParams {
	private String name;
	private String domain;
	private HostResponseDescriptors descriptors;

	/**
	 * @param name
	 * @param desc
	 */
	public HP3ParHostParams(String name, HostResponseDescriptors desc) {
		this.name = name;
		this.descriptors = desc;
	}

	/**
	 * @param name
	 * @param domain
	 * @param desc
	 */
	public HP3ParHostParams(String name, String domain, HostResponseDescriptors desc) {
		this.name = name;
		if ((domain != null) && (!"".equals(domain))) {
			this.domain = domain;
		}
		this.descriptors = desc;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return this.domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the descriptors
	 */
	public HostResponseDescriptors getDescriptors() {
		return this.descriptors;
	}

	/**
	 * @param descriptors
	 *            the descriptors to set
	 */
	public void setDescriptors(HostResponseDescriptors descriptors) {
		this.descriptors = descriptors;
	}

}
