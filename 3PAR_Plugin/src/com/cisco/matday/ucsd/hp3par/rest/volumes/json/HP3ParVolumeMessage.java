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
 * Object representation of a REST response from the 3PAR array.
 * 
 * See the 3PAR documentation for details
 * 
 * @author Matt Day
 *
 */
public class HP3ParVolumeMessage {
	private int code;
	private String desc;

	@SuppressWarnings("javadoc")
	public int getCode() {
		return code;
	}

	@SuppressWarnings("javadoc")
	public void setCode(int code) {
		this.code = code;
	}

	@SuppressWarnings("javadoc")
	public String getDesc() {
		return desc;
	}

	@SuppressWarnings("javadoc")
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
