/*
 * Copyright 2000 - 2004, Bavo De Ridder
 * 
 * This file is part of Portal Foundation Classes.
 * 
 * Portal Foundation Classes is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * Portal Foundation Classes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Portal Foundation Classes; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston
 * 
 * http://www.gnu.org/licenses/gpl.html
 */

package portal.rt.services.monitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MonitorInvocationHandler implements InvocationHandler
{
	private static final Log LOGGER = LogFactory.getLog(MonitorInvocationHandler.class.getName());

	public MonitorInvocationHandler(Object pTarget,
			ObjectMonitorTarget monitorTarget, String pName)
	{
		this._mTarget = pTarget;
		this._monitorTarget = monitorTarget;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable
	{
		long beginTime = System.currentTimeMillis();

		try
		{
			return method.invoke(_mTarget, args);
		}
		catch (InvocationTargetException pExc)
		{
			throw pExc.getTargetException();
		}
		catch (IllegalAccessException pExc)
		{
			LOGGER.error("Could not access method on target", pExc);

			return null;
		}
		finally
		{
			long endTime = System.currentTimeMillis();

			_monitorTarget.registerCall(method, (endTime - beginTime));
		}

	}

	private Object _mTarget = null;
	private ObjectMonitorTarget _monitorTarget;
}