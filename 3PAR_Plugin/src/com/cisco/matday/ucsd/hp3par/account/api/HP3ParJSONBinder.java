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
 * Boilerplate from SDK, not sure what it does 
 * @author Matt Day
 *
 */
public abstract class HP3ParJSONBinder implements ItemDataObjectBinderIf {
	private static Logger logger = Logger.getLogger(HP3ParJSONBinder.class);
	
	@SuppressWarnings({
			"rawtypes", "javadoc"
	})
	public abstract List<Class> getPersistantClassList();

	protected void bindContext(Object obj, Map<String, Object> context) {
		for (Map.Entry<String, Object> entry : context.entrySet()) {
			String varName = entry.getKey();
			Object value = entry.getValue();
			try {
				Field field = obj.getClass().getDeclaredField(varName);
				field.setAccessible(true);
				if (value != null)
					field.set(obj, value);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block

			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				logger.debug("No field by name " + varName + " for Class " + obj.getClass().getSimpleName());
				continue;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				logger.info("Illegal argument value while setting value for " + varName + obj.getClass());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				logger.info("Illegal access while setting value for " + varName + obj.getClass());
			}
		}
	}
}
