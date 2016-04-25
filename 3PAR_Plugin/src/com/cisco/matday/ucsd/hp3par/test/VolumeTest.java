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
package com.cisco.matday.ucsd.hp3par.test;

import java.util.HashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;

// Don't document this test case, it changes too often
@SuppressWarnings("javadoc")
public class VolumeTest {

	final static String ipAddress = "10.51.8.210";
	final static String user = "3paradm";
	final static String password = "3pardata";

	@SuppressWarnings({
			"deprecation", "unused", "boxing"
	})
	public static void main(String[] args) {

		try {

			Logger.getRootLogger().setLevel(Level.INFO);

			// Don't warn that I'm using test methods
			HP3ParCredentials login = new HP3ParCredentials(ipAddress, user, password);

			HashMap<String, Boolean> keepMap = new HashMap<>();

			final String newMembers = "0@3PAR@Member1, 0@3PAR@Member2, 0@3PAR@Member3";
			final String oldMembers = "0@3PAR@Member9, 0@3PAR@Member2, 0@3PAR@Member8";

			// Should end up with delete list =
			// Member9, Member8
			// Create List =
			// Member 1, Member3

			// Assume we're not keeping anything
			for (String host : oldMembers.split(",")) {
				keepMap.put(host.split("@")[2], false);
			}
			// Compute which ones to keep
			for (String host : newMembers.split(",")) {
				keepMap.put(host.split("@")[2], true);
			}
			// Remove anything not matching:
			for (String host : keepMap.keySet()) {
				if (!keepMap.get(host)) {
					System.out.println("Remove: " + host);
				}
			}
			// Assume everything new should be added as new
			for (String host : newMembers.split(",")) {
				keepMap.put(host.split("@")[2], true);
			}
			// Anything currently in the list should not
			for (String host : oldMembers.split(",")) {
				keepMap.put(host.split("@")[2], false);
			}
			for (String host : keepMap.keySet()) {
				if (keepMap.get(host)) {
					System.out.println("Add: " + host);
				}
			}

		}
		catch (Exception e) {
			// Ignore errors in test case
		}
	}
}
