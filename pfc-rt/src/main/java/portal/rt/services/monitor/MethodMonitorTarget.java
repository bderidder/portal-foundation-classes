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

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MethodMonitorTarget
{
	private static final Log LOGGER = LogFactory.getLog(MethodMonitorTarget.class);

	public MethodMonitorTarget(Method method)
	{
		_method = method;
	}

	public Method getMethod()
	{
		return _method;
	}

	public long getCallCount()
	{
		return _callCount;
	}

	public long getAverageTime()
	{
		return _averageTime;
	}

	public long getMinimumTime()
	{
		return _minimumTime;
	}

	public long getMaximumTime()
	{
		return _maximumTime;
	}

	public void registerCall(long timePerformed)
	{
		if (timePerformed < _minimumTime)
		{
			_minimumTime = timePerformed;
		}

		if (timePerformed > _maximumTime)
		{
			_maximumTime = timePerformed;
		}

		long oldCount = _callCount;
		long oldAverageTime = _averageTime;

		_callCount++;

		_averageTime = ((oldAverageTime * oldCount) + timePerformed) / _callCount;

		LOGGER.info(getMethod().getName() + "[" + _callCount + "," + _averageTime
				+ "," + _minimumTime + "," + _maximumTime + "," + timePerformed
				+ "]");
	}

	private Method _method;

	private long _callCount = 0;
	private long _averageTime = 0;
	private long _minimumTime = Long.MAX_VALUE;
	private long _maximumTime = Long.MIN_VALUE;
}