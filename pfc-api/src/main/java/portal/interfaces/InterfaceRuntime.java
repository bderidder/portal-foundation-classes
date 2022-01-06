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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * A support class for classes that want to extend their functionality with
 * optional interfaces but that do not want to spend much time in the
 * administration of these interfaces.
 * </p>
 *
 * <p>
 * This class can be used either by inheritance (extends) or by delegation.
 * </p>
 *
 * @author bderidder
 */
public class InterfaceRuntime implements IInterfaceImplementor
{
    private static final long serialVersionUID = 1L;
    private Map<Class<? extends IOptionalInterface>, Object> _interfaces;
    private boolean _interfaceAsked;

    public InterfaceRuntime()
    {
        _interfaces = new HashMap<>();

        _interfaceAsked = false;
    }

    @Override
    public final Set<Class<? extends IOptionalInterface>> listOptionalInterfaces()
    {
        _interfaceAsked = true;

        Set<Class<? extends IOptionalInterface>> interfaces;

        if (_interfaces.isEmpty())
        {
            interfaces = null;
        }
        else
        {
            interfaces = new HashSet<>(_interfaces.keySet());
        }

        return interfaces;
    }

    @Override
    public final boolean supportsInterface(Class<? extends IOptionalInterface> pInterface)
            throws NotAnOptionalInterfaceException
    {
        _interfaceAsked = true;

        if (pInterface == null)
        {
            throw new IllegalArgumentException(
                    "Class parameter can not be null.");
        }

        if (!InterfaceUtils.isOptionalInterface(pInterface))
        {
            throw new NotAnOptionalInterfaceException(
                    "Passed Class parameter was not a subtype of IOptionalInterface");
        }

        return _interfaces.keySet().contains(pInterface);
    }

    @Override
    public final IOptionalInterface getInterface(Class<? extends IOptionalInterface> pInterface)
            throws NotAnOptionalInterfaceException,
            InterfaceNotSupportedException
    {
        _interfaceAsked = true;

        if (pInterface == null)
        {
            throw new IllegalArgumentException(
                    "Class parameter can not be null.");
        }

        if (!supportsInterface(pInterface))
        {
            throw new InterfaceNotSupportedException(
                    "Optional interface is not supported by this object: "
                    + pInterface);
        }

        IInterfaceFactory factory = (IInterfaceFactory) _interfaces
                .get(pInterface);

        return factory.createInstance();
    }

    /**
     * <p>
     * Registers a new optional interface together with a factory class. The
     * registration of a new optional interface is only allowed as long as none
     * of the public methods have been called.
     * </p>
     *
     * <p>
     * The list of supported optional interfaces must be known and cannot change
     * after construction time. However, this implementation allows for a
     * slightly longer time in which optional interfaces are added: as long as
     * nobody knows what the current list is, we can add new optional
     * interfaces.
     * </p>
     *
     * @param pInterface the optional interface supported
     *
     * @param pFactory factory instance for returning the implementation
     *
     * @throws NotAnOptionalInterfaceException the passed interface class type
     * was not a subtype of IOptionalInterface
     * @throws InterfaceRuntimeException at least one of the public methods has
     * been called at least once
     */
    protected final void registerInterface(Class<? extends IOptionalInterface> pInterface,
            IInterfaceFactory pFactory) throws NotAnOptionalInterfaceException, InterfaceRuntimeException
    {
        if (_interfaceAsked)
        {
            throw new InterfaceRuntimeException(
                    "Can not add new optional interface, "
                    + "list of optional interfaces was already requested");
        }

        if (!InterfaceUtils.isOptionalInterface(pInterface))
        {
            throw new NotAnOptionalInterfaceException(
                    "Passed Class parameter was not a subtype of IOptionalInterface");
        }

        _interfaces.put(pInterface, pFactory);
    }
}
