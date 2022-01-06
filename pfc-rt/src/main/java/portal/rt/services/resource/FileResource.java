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
package portal.rt.services.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import portal.services.resource.IResource;
import portal.services.resource.ResourceServiceException;

public class FileResource implements IResource
{
    private static final long serialVersionUID = 1L;
    private FileResourceLoader _resourceLoader;
    private String _resourcePath;
    private String _contentType;
    private File _resourceFile;

    public FileResource(FileResourceLoader myResourceLoader,
            String resourcePath, String contentType)
            throws ResourceServiceException
    {
        _resourceLoader = myResourceLoader;
        _resourcePath = resourcePath;
        _contentType = contentType;

        _resourceFile = new File(_resourceLoader.getBasePath() + "/"
                + resourcePath);

        if (!_resourceFile.exists() || !_resourceFile.isFile()
                || !_resourceFile.canRead())
        {
            throw new ResourceServiceException(
                    "Resource does not exist, is not a file or is not readable: "
                    + resourcePath);
        }
    }

    @Override
    public long getLastModifiedTimeStamp() throws ResourceServiceException
    {
        return _resourceFile.lastModified();
    }

    @Override
    public String getContentType() throws ResourceServiceException
    {
        return _contentType;
    }

    @Override
    public InputStream getResourceAsStream() throws ResourceServiceException
    {
        try
        {
            return new FileInputStream(_resourceFile);
        }
        catch (Exception e)
        {
            throw new ResourceServiceException(
                    "Could not load resource from filesystem: " + _resourcePath,
                    e);
        }
    }
}
