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

package portal.interfaces;

/**
 * <p>
 * This utility implementation of the IInterfaceFactory interface always returns
 * the same instance. This factory can be used if the IInterfaceImplementor
 * implements the optional interface itself or when there is a known instance at
 * construction time:
 * </p>
 * 
 * <tt>
 * registerInterface(IMyInterface.class, new StaticFactory(this));
 * </tt>
 * 
 * @author bderidder
 */
public final class StaticFactory implements IInterfaceFactory
{
	/**
	 * <p>
	 * Creates a new instance with the given implementation.
	 * </p>
	 * 
	 * @param pImplementation
	 *            the implementation to return in the method createInstance().
	 *            Can not be null.
	 */
	public StaticFactory(IOptionalInterface pImplementation)
	{
		if (pImplementation == null)
		{
			throw new IllegalArgumentException(
					"The passed implementation cannot be null");
		}

		_implementation = pImplementation;
	}

	/**
	 * <p>
	 * Returns the implementation instance set at construction time.
	 * </p>
	 * 
	 * @return the implementation of the optional interface
	 */
	public IOptionalInterface createInstance()
	{
		return _implementation;
	}

	private IOptionalInterface _implementation;
}
