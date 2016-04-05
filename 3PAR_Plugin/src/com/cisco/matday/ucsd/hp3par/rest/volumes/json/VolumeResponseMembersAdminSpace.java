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

/**
 * Basic REST representation of a 3PAR request
 * 
 * See the HP3PAR documentation for more details
 * @author Matt Day
 *
 */
public class VolumeResponseMembersAdminSpace {
	// "adminSpace": {
	// "reservedMiB": 0,
	// "rawReservedMiB": 0,
	// "usedMiB": 0,
	// "freeMiB": 0
	// },
	private long reservedMiB;
	private long rawReservedMiB;
	private long usedMiB;
	private long freeMiB;

	@SuppressWarnings("javadoc")
	public long getReservedMiB() {
		return reservedMiB;
	}

	@SuppressWarnings("javadoc")
	public void setReservedMiB(long reservedMiB) {
		this.reservedMiB = reservedMiB;
	}

	@SuppressWarnings("javadoc")
	public long getRawReservedMiB() {
		return rawReservedMiB;
	}

	@SuppressWarnings("javadoc")
	public void setRawReservedMiB(long rawReservedMiB) {
		this.rawReservedMiB = rawReservedMiB;
	}

	@SuppressWarnings("javadoc")
	public long getUsedMiB() {
		return usedMiB;
	}

	@SuppressWarnings("javadoc")
	public void setUsedMiB(long usedMiB) {
		this.usedMiB = usedMiB;
	}

	@SuppressWarnings("javadoc")
	public long getFreeMiB() {
		return freeMiB;
	}

	@SuppressWarnings("javadoc")
	public void setFreeMiB(long freeMiB) {
		this.freeMiB = freeMiB;
	}
}
