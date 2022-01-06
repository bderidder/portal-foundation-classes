/*
 * Copyright 2000 - 2006, Bavo De Ridder
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

package portal.services.scheduler;

import portal.services.IService;

/**
 * @author Bavo De Ridder <bavo@coderspotting.org>
 * 
 * Use this class to schedule the execution of tasks at various times
 */
public interface ISchedulerService extends IService
{
	/**
	 * Create an object implementing pInterface that times and logs calls to the
	 * real implementation given by pTarget. Timings will be logged with pName.
	 * 
	 * @param pTask
	 *            The task to execute
	 * @param pWhen
	 *            An object telling when the task must be executed
	 */
	void schedule(IScheduledTask pTask, Object pWhen);
}