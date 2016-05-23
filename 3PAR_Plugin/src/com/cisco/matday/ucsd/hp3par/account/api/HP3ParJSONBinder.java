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
package com.cisco.matday.ucsd.hp3par.account.api;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cloupia.service.cIM.inframgr.collector.controller.ItemDataObjectBinderIf;

/**
 * Boilerplate from SDK, not sure what it does!
 *
 * @author Matt Day
 *
 */
public abstract class HP3ParJSONBinder implements ItemDataObjectBinderIf {
	private static Logger logger = Logger.getLogger(HP3ParJSONBinder.class);

	@SuppressWarnings({
			"rawtypes", "javadoc"
	})
	public abstract List<Class> getPersistantClassList();

	@SuppressWarnings("static-method")
	protected void bindContext(Object obj, Map<String, Object> context) {
		for (final Map.Entry<String, Object> entry : context.entrySet()) {
			final String varName = entry.getKey();
			final Object value = entry.getValue();
			try {
				final Field field = obj.getClass().getDeclaredField(varName);
				field.setAccessible(true);
				if (value != null) {
					field.set(obj, value);
				}
			}
			catch (final SecurityException e) {
				logger.warn("Security Exception: " + e.getMessage());

			}
			catch (@SuppressWarnings("unused") final NoSuchFieldException e) {
				logger.warn("No field by name " + varName + " for Class " + obj.getClass().getSimpleName());
				continue;
			}
			catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
				logger.warn("Illegal argument value while setting value for " + varName + obj.getClass());
			}
			catch (@SuppressWarnings("unused") final IllegalAccessException e) {
				logger.warn("Illegal access while setting value for " + varName + obj.getClass());
			}
		}
	}
}
