/*
 * Copyright 2000,  Bavo De Ridder
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
package portal.services.resource;

import java.io.InputStream;

import portal.services.ServiceException;
import portal.services.ServiceLocator;

public class LazyResource implements IResource
{
    private static final long serialVersionUID = 1L;
    private String _resourcePath;
    private String _contentType;
    private IResource _resource;

    public LazyResource(String resourcePath)
    {
        this(resourcePath, null);
    }

    public LazyResource(String resourcePath, String contentType)
    {
        _resourcePath = resourcePath;
        _contentType = contentType;

        _resource = null;
    }

    @Override
    public long getLastModifiedTimeStamp() throws ResourceServiceException
    {
        return System.currentTimeMillis();
    }

    @Override
    public final String getContentType() throws ResourceServiceException
    {
        checkResource();

        return _resource.getContentType();
    }

    @Override
    public final InputStream getResourceAsStream()
            throws ResourceServiceException
    {
        checkResource();

        return _resource.getResourceAsStream();
    }

    private void checkResource() throws ResourceServiceException
    {
        if (_resource == null)
        {
            try
            {
                if (_contentType != null)
                {
                    _resource = loadResource(_resourcePath, _contentType);
                }
                else
                {
                    _resource = loadResource(_resourcePath);

                }
            }
            catch (ResourceServiceException e)
            {
                throw e;
            }
            catch (ServiceException e)
            {
                throw new ResourceServiceException(
                        "Could not load IResourceService", e);
            }

            if (_resource == null)
            {
                throw new ResourceServiceException(
                        "Could not retrieve resource '" + _resourcePath
                        + "'from ResourceService, does it exist?");
            }
        }
    }

    protected IResource loadResource(String resourcePath)
            throws ResourceServiceException, ServiceException
    {
        IResourceService resourceService = (IResourceService) ServiceLocator
                .getServiceLocator().locateService(IResourceService.class);

        return resourceService.getResource(resourcePath);
    }

    protected IResource loadResource(String resourcePath, String contentType)
            throws ResourceServiceException, ServiceException
    {
        IResourceService resourceService = (IResourceService) ServiceLocator
                .getServiceLocator().locateService(IResourceService.class);

        return resourceService.getResource(resourcePath, contentType);
    }
}
