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

import java.lang.reflect.Proxy;
import java.util.HashMap;

import portal.services.monitor.IMonitorService;

public class MonitorServiceImpl implements IMonitorService
{
    public MonitorServiceImpl(String namespace)
    {
        this._namespace = namespace;

        _targetInterfaces = new HashMap<>();
    }

    @Override
    public Object monitor(Object pTarget, Class<?> pInterface, String pName)
    {
        if (!isStopped())
        {
            ObjectMonitorTarget objectMonitorTarget = _targetInterfaces.get(pInterface);

            if (objectMonitorTarget == null)
            {
                objectMonitorTarget = new ObjectMonitorTarget(pInterface);

                _targetInterfaces.put(pInterface, objectMonitorTarget);
            }

            Class<?>[] interfaces = new Class<?>[]
            {
                pInterface
            };

            return Proxy.newProxyInstance(pInterface.getClassLoader(),
                    interfaces, new MonitorInvocationHandler(
                    pTarget, objectMonitorTarget, _namespace + "."
                    + pName));
        }
        else
        {
            return pTarget;
        }
    }

    public void stop()
    {
        synchronized (this)
        {
            _stopped = true;
        }
    }

    public void start()
    {
        synchronized (this)
        {
            _stopped = false;
        }
    }

    private boolean isStopped()
    {
        synchronized (this)
        {
            return _stopped;
        }
    }
    private HashMap<Class<?>, ObjectMonitorTarget> _targetInterfaces;
    private boolean _stopped = true;
    private String _namespace;
}
