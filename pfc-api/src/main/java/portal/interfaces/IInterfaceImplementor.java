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

import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * Java allows a developer to express the supported interfaces at development
 * time. This only works for interfaces that are known to be supported for every
 * single instance of a class. There is no language construct to indicate
 * optional interfaces. These are interfaces for which support can only be
 * decided at runtime, when the state of the object is known.
 * </p>
 *
 * <p>
 * This interface allows you to find out which optional interfaces are supported
 * by this instance.
 * </p>
 *
 * @author bderidder
 */
public interface IInterfaceImplementor
{
    /**
     * <p>
     * Query this instance for the list of supported optional interfaces.
     * </p>
     *
     * <p>
     * Any implementation must guarantee that throughout the lifetime of the
     * object, this list remains unchanged. In other words, the list of
     * supported optional interfaces is determined at construction time and
     * doesn't change after that.
     * </p>
     *
     * @return the list of supported interfaces or null when no optional
     * interfaces are supported, all returned references are subtypes of
     * IOptionalInterface
     */
    Set<Class<? extends IOptionalInterface>> listOptionalInterfaces();

    /**
     * <p>
     * Checks if a particular optional interface is supported by this instance.
     *
     * @param pInterface the Class instance of the interface class
     *
     * @return true if the interface is supported, false otherwise
     *
     * @throws NotAnOptionalInterfaceException when the passed parameter is not
     * a subtype of IOptionalInterface
     */
    boolean supportsInterface(Class<? extends IOptionalInterface> pInterface)
            throws NotAnOptionalInterfaceException;

    /**
     * <p>
     * The object instance does not always implement the optional interfaces
     * directly. This method should always be used to retrieve an object
     * instance implementing the interface.
     * </p>
     *
     * <p>
     * Directly casting the object instance to the requested interface instead
     * of using this method might work on some occasions but is not guaranteed
     * to work and therefore not supported.
     * </p>
     *
     * @param pInterface the Class instance of the interface class
     *
     * @return an object instance implementing the requested interface
     *
     * @throws InterfaceNotSupportedException when the object does not support
     * this optional interface
     * @throws NotAnOptionalInterfaceException when the passed parameter is not
     * a subtype of IOptionalInterface
     */
    IOptionalInterface getInterface(Class<? extends IOptionalInterface> pInterface)
            throws NotAnOptionalInterfaceException, InterfaceNotSupportedException;
}
