/*
 * Copyright 2000, Bavo De Ridder
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
 * <p>Locating a service can be an expensive task, especially when it is done in
 * a loop. This class caches a reference to a service.</p>
 *
 * @author Bavo De Ridder < bavo AT coderspotting DOT org >
 */
public final class CachedServiceRef<ServiceType extends IService>
{
    private ServiceType _service;

    /**
     * <p>Create a cached reference to a service.</p>
     *
     * @param interfaceRequired The Class instance of the interface the service
     * should implement
     *
     * @throws ServiceException thrown when the framework was unable to search
     * for a service or when the service did not exist
     */
    @SuppressWarnings("unchecked")
    public CachedServiceRef(Class<ServiceType> interfaceRequired) throws ServiceException
    {
        _service = (ServiceType) ServiceLocator.getServiceLocator().locateService(interfaceRequired);

        if (_service == null)
        {
            throw new ServiceException(
                    "There is no registered service implementing the interface "
                    + interfaceRequired);
        }
    }

    /**
     * @return the cached reference to the service
     */
    public ServiceType getService()
    {
        return _service;
    }
}
