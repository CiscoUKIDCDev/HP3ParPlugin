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

import java.util.List;

/**
 * REST representation from 3PAR array when querying volumes
 * 
 * See the HP3PAR documentation for more detail
 * 
 * @author Matt Day
 *
 */
public class VolumeResponse {
	private int total;
	private List<VolumeResponseMember> members;

	@SuppressWarnings("javadoc")
	public void setMembers(List<VolumeResponseMember> members) {
		this.members = members;
	}

	@SuppressWarnings("javadoc")
	public void setTotal(int total) {
		this.total = total;
	}

	@SuppressWarnings("javadoc")
	public long getTotal() {
		return total;
	}

	@SuppressWarnings("javadoc")
	public List<VolumeResponseMember> getMembers() {
		return members;
	}
}
