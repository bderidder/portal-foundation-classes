/*
 * Copyright 2000 - 2004,  Bavo De Ridder
 * 
 * This file is part of Portal Foundation Classes.
 *
 * Portal Foundation Classes is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * Portal Foundation Classes is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Portal Foundation Classes; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston
 * 
 * http://www.gnu.org/licenses/gpl.html
 */

package portal.services;

/**
 * This class represents an enumeration type used to specify a service state.
 * Three states are possible:
 * 
 * <table cellspacing="4">
 * <tr>
 * 	<td valign="top"><p><b>RUNNING</b></p></td>
 *  <td valign="top"><p>The service is started and can be used.</p></td>
 * </tr>
 * <tr>
 *  <td valign="top"><p><b>STOPPED</b></p></td>
 *  <td valign="top">
 *      <p>
 *       The service is currently not started and cannot be used until the start() method is called.
 *      </p>
 *  </td>
 * </tr>
 * <tr>
 *  <td valign="top"><p><b>PENDING</b></p></td>
 *  <td valign="top">
 *      <p>
 *       A start has been requested but the service has not yet fully started. This
 *       could be because of some internal processing still to be performed or because
 *       depending services are not yet started.
 *      </p>
 *  </td>
 * </tr>
 * </table>
 * 
 * @author Bavo De Ridder < bavo AT coderspotting DOT org >
 */
public final class ServiceState
{
	public static final ServiceState RUNNING = new ServiceState(0, "RUNNING");
	public static final ServiceState STOPPED = new ServiceState(1, "STOPPED");
	public static final ServiceState PENDING = new ServiceState(2, "PENDING");

	public boolean equals(Object obj)
	{
		if (obj instanceof ServiceState)
		{
			return _serviceState == ((ServiceState) obj)._serviceState;
		}
		else
		{
			return false;
		}
	}

	public int hashCode()
	{
		return _stateName.hashCode();
	}

	public String toString()
	{
		return _stateName;
	}

	private ServiceState(int serviceState, String stateName)
	{
		this._serviceState = serviceState;
		this._stateName = stateName;
	}

	private int _serviceState;
	private String _stateName;
}