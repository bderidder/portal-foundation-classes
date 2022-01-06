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

import java.util.Properties;

import portal.services.resource.IResource;
import portal.services.resource.ResourceServiceException;

public class ClasspathResourceLoader implements IResourceLoader
{
	private static final long serialVersionUID = 1L;

	public ClasspathResourceLoader()
	{
	}

	public void init(Properties properties) throws ResourceServiceException
	{
	}

	public boolean hasResource(String resourcePath)
			throws ResourceServiceException
	{
		return (getClass().getClassLoader().getResourceAsStream(resourcePath) != null);
	}

	public IResource getResource(String resourcePath)
			throws ResourceServiceException
	{
		return getResource(resourcePath, "");
	}

	public IResource getResource(String resourcePath, String contentType)
			throws ResourceServiceException
	{
		if (!hasResource(resourcePath))
		{
			throw new ResourceServiceException("Could not find resource '"
					+ resourcePath + "' in this loader (" + toString() + ")");
		}

		return new ClasspathResource(this, resourcePath, contentType);
	}
}