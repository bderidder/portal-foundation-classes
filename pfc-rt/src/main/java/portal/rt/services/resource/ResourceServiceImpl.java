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

package portal.rt.services.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.services.resource.IResource;
import portal.services.resource.IResourceService;
import portal.services.resource.ResourceServiceException;

public class ResourceServiceImpl implements IResourceService
{
	private static final Log LOGGER = LogFactory.getLog(ResourceServiceImpl.class);

	public ResourceServiceImpl() throws ResourceServiceException
	{
		_resourceLoaders = new ArrayList<IResourceLoader>();
	}

	public void addResourceLoader(IResourceLoader resourceLoader)
	{
		_resourceLoaders.add(resourceLoader);
	}

	public IResource getResource(String resourcePath)
			throws ResourceServiceException
	{
		for (int i = 0; i != _resourceLoaders.size(); i++)
		{
			IResourceLoader resourceLoader = _resourceLoaders.get(i);

			LOGGER.debug("Checking loader " + resourceLoader);

			if (resourceLoader.hasResource(resourcePath))
			{
				return resourceLoader.getResource(resourcePath);
			}
		}

		return null;
	}

	public IResource getResource(String resourcePath, String contentType)
			throws ResourceServiceException
	{
		for (int i = 0; i != _resourceLoaders.size(); i++)
		{
			IResourceLoader resourceLoader = _resourceLoaders.get(i);

			if (resourceLoader.hasResource(resourcePath))
			{
				return resourceLoader.getResource(resourcePath, contentType);
			}
		}

		return null;
	}

	private List<IResourceLoader> _resourceLoaders;
}