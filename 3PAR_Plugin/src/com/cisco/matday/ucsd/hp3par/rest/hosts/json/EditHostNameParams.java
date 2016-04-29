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
package com.cisco.matday.ucsd.hp3par.rest.hosts.json;

/**
 * Implements HP 3PAR iSCSI/FC adding parameters
 *
 * See the HP3PAR API guide for more info
 *
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class EditHostNameParams {
	final HostResponseDescriptors descriptors;
	final String newName;

	public EditHostNameParams(HostResponseDescriptors descriptors, String oldName, String newName) {
		this.descriptors = descriptors;
		if (!oldName.equals(newName)) {
			this.newName = newName;
		}
		else {
			this.newName = null;
		}

	}

	/**
	 * @return the descriptors
	 */
	public HostResponseDescriptors getDescriptors() {
		return this.descriptors;
	}

	/**
	 * @return the newName
	 */
	public String getNewName() {
		return this.newName;
	}

}
