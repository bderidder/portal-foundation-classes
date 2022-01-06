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

package portal.services.monitor;

import portal.services.IService;

/**
 * @author Bavo De Ridder <bavo@coderspotting.org>
 * 
 * Use this class to time executions of method calls on interfaces.
 */
public interface IMonitorService extends IService
{
	/**
	 * Create an object implementing pInterface that times and logs calls to the
	 * real implementation given by pTarget. Timings will be logged with pName.
	 * 
	 * @param pTarget
	 *            The implementation class to monitor
	 * @param pInterface
	 *            The interface implemented by pTarget
	 * @param pName
	 *            The name to use in logging
	 * @return a reference to an object that will monitor all method calls to the
	 *         object given in pTarget, it implements the interface given in the 
	 *         pInterface parameter
	 */
	Object monitor(Object pTarget, Class<?> pInterface, String pName);
}