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

import java.io.File;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.services.resource.IResource;
import portal.services.resource.ResourceServiceException;

public class FileResourceLoader implements IResourceLoader
{
	private static final long serialVersionUID = 1L;

	private static final Log LOGGER = LogFactory.getLog(FileResourceLoader.class);

	public FileResourceLoader() throws ResourceServiceException
	{
	}

	public void init(Properties properties) throws ResourceServiceException
	{
		String basePath = (String) properties.get("basepath");

		_baseDirectory = (new File(basePath)).getAbsoluteFile();
		_basePath = _baseDirectory.getAbsolutePath();

		if (!_baseDirectory.exists() || !_baseDirectory.isDirectory())
		{
			throw new ResourceServiceException(
					"Location does not exist or is not a directory: "
							+ _basePath);
		}
	}

	public boolean hasResource(String resourcePath)
			throws ResourceServiceException
	{
		if (resourcePath.startsWith("/"))
		{
			LOGGER.warn("Resource location is absolute, stripping leading /: "
					+ resourcePath);

			resourcePath = resourcePath.substring(1);
		}

		File resourceFile = new File(_basePath + "/" + resourcePath);

		return (resourceFile.exists() && resourceFile.isFile());
	}

	public IResource getResource(String resourcePath)
			throws ResourceServiceException
	{
		return getResource(resourcePath, "");
	}

	public IResource getResource(String resourcePath, String contentType)
			throws ResourceServiceException
	{
		if (resourcePath.startsWith("/"))
		{
			LOGGER.warn("Resource location is absolute, stripping leading /: "
					+ resourcePath);

			resourcePath = resourcePath.substring(1);
		}

		if (!hasResource(resourcePath))
		{
			throw new ResourceServiceException("Could not find resource '"
					+ resourcePath + "' in this loader (" + toString() + ")");
		}

		return new FileResource(this, resourcePath, contentType);
	}

	public String getBasePath()
	{
		return _basePath;
	}

	private String _basePath;
	private File _baseDirectory;
}