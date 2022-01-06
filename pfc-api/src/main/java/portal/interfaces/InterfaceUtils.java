/*
 * Copyright 2000 - 2006,  Bavo De Ridder
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
 * A support class that bundled some convenience methods for dealing with
 * optional interfaces.
 * </p>
 *
 * @author bderidder
 */
public final class InterfaceUtils
{
    private InterfaceUtils()
    {
    }

    /**
     * <p>
     * This helper method will check if the instance of an implementor supports
     * a list of interfaces.
     * </p>
     *
     * @param pImplementor The implementor of the optionel interfaces, cannot be
     * null
     * @param pInterfaces An array of interface classes to check for, each one
     * must be a subtype of IOptionalInterface, cannot be null
     *
     * @return true if the implementor offers each of the requested interfaces,
     * false if at least one interface is not supported
     */
    public static boolean supportsInterfaces(
            IInterfaceImplementor pImplementor, Class<? extends IOptionalInterface>[] pInterfaces)
    {
        if ((pImplementor == null) || (pInterfaces == null))
        {
            throw new IllegalArgumentException(
                    "Either the implementor or the array of interfaces was null.");
        }

        for (int i = 0; i != pInterfaces.length; i++)
        {
            Class<? extends IOptionalInterface> interfaceClass = pInterfaces[i];

            if (interfaceClass == null)
            {
                throw new IllegalArgumentException(
                        "One of the classes was null.");
            }

            if (!InterfaceUtils.isOptionalInterface(interfaceClass))
            {
                throw new NotAnOptionalInterfaceException(
                        "One of the classes was not a subtype of IOptionalInterface");
            }

            if (!pImplementor.supportsInterface(interfaceClass))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>
     * A convenience method to check if a given class implements the interface
     * IOptionalInterface
     * </p>
     *
     * @param pInterface the interface class to verify
     *
     * @return true if the class inherits from IOptionalInterface, false
     * otherwise
     */
    public static boolean isOptionalInterface(Class<?> pInterface)
    {
        return IOptionalInterface.class.isAssignableFrom(pInterface);
    }
}
