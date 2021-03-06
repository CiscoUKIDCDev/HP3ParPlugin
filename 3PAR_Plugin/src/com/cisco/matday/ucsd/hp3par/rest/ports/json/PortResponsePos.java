/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
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
package com.cisco.matday.ucsd.hp3par.rest.ports.json;

import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParPortException;

/**
 * 3PAR Port list position JSON enumeration
 *
 * For more info see the HP 3PAR REST API documentation
 *
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class PortResponsePos {
	private int node;
	private int slot;
	private int cardPort;

	public PortResponsePos() {

	}

	public PortResponsePos(String portPos) throws Exception {
		// Format: 0@accountName@portPos
		// Node/Slot/Port
		try {
			final String[] split = portPos.split("/");
			this.node = Integer.parseInt(split[0]);
			this.slot = Integer.parseInt(split[1]);
			this.cardPort = Integer.parseInt(split[2]);
		}
		catch (Exception e) {
			throw new HP3ParPortException("Invalid 3PAR port format: " + portPos + " (" + e.getMessage() + ")");
		}
	}

	public int getNode() {
		return this.node;
	}

	public void setNode(int node) {
		this.node = node;
	}

	public int getSlot() {
		return this.slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getCardPort() {
		return this.cardPort;
	}

	public void setCardPort(int cardPort) {
		this.cardPort = cardPort;
	}
}
