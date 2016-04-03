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
package com.cisco.matday.ucsd.hp3par.rest.cpg.json;

public class CPGResponseSpace {
    private long totalMiB;
    private long rawTotalMiB;
    private long usedMiB;
    public long getTotalMiB() {
		return totalMiB;
	}
	public void setTotalMiB(long totalMiB) {
		this.totalMiB = totalMiB;
	}
	public long getRawTotalMiB() {
		return rawTotalMiB;
	}
	public void setRawTotalMiB(long rawTotalMiB) {
		this.rawTotalMiB = rawTotalMiB;
	}
	public long getUsedMiB() {
		return usedMiB;
	}
	public void setUsedMiB(long usedMiB) {
		this.usedMiB = usedMiB;
	}
	public long getRawUsedMiB() {
		return rawUsedMiB;
	}
	public void setRawUsedMiB(long rawUsedMiB) {
		this.rawUsedMiB = rawUsedMiB;
	}
	private long rawUsedMiB;
}
