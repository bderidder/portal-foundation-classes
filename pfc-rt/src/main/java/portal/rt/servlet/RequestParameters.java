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

package portal.rt.servlet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import portal.rt.ui.upload.FileItem;
import portal.rt.ui.upload.FileUpload;
import portal.services.registry.StringKey;

public class RequestParameters
{
	private static final String ENGINE_DEFAULT_TEMPDIR = "/tmp";
	private static final String ENGINE_TEMPDIR_KEY = "portal.core.engine.tempdir";
	private static final int UPLOAD_SIZE_TRESHOLD = 4 * 1024;	// 4KB
	private static final int UPLOAD_MAX_SIZE = 10 * 1024 * 1024;	// 10MB

	public RequestParameters(HttpServletRequest request)
			throws ServletException
	{
		try
		{
			_fileUpload = new FileUpload();
			_fileUpload.setSizeMax(UPLOAD_MAX_SIZE);
			_fileUpload.setSizeThreshold(UPLOAD_SIZE_TRESHOLD);
			_fileUpload.setRepositoryPath(getRepositoryPath());

			_items = _fileUpload.parseRequest(request);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new ServletException(
					"Error while wrapping request with file upload");
		}
	}

	public boolean isFormField(String paramName)
	{
		Iterator<FileItem> it = _items.iterator();

		while (it.hasNext())
		{
			FileItem fileItem = it.next();

			if (fileItem.getFieldName().equals(paramName)
					&& fileItem.isFormField())
			{
				return true;
			}
		}

		return false;
	}

	public FileItem getFile(String paramName)
	{
		Iterator<FileItem> it = _items.iterator();

		while (it.hasNext())
		{
			FileItem fileItem = it.next();

			if (fileItem.getFieldName().equals(paramName)
					&& !fileItem.isFormField())
			{
				return fileItem;
			}
		}

		return null;
	}

	public Object getParameterValuesAsObject(String paramName)
	{
		if (isFormField(paramName))
		{
			return getParameterValues(paramName);
		}
		else
		{
			return getFile(paramName);
		}
	}

	public String[] getParameterValues(String paramName)
	{
		List<String> result = new LinkedList<String>();

		Iterator<FileItem> it = _items.iterator();

		while (it.hasNext())
		{
			FileItem fileItem = it.next();

			if (fileItem.getFieldName().equals(paramName)
					&& fileItem.isFormField())
			{
				result.add(fileItem.getString());
			}
		}

		if (result.size() != 0)
		{
			return castArray(result.toArray());
		}
		else
		{
			return null;
		}
	}

	public String getParameter(String paramName)
	{
		Iterator<FileItem> it = _items.iterator();

		while (it.hasNext())
		{
			FileItem fileItem = it.next();

			if (fileItem.getFieldName().equals(paramName)
					&& fileItem.isFormField())
			{
				return fileItem.getString();
			}
		}

		return null;
	}

	private String[] castArray(Object[] objs)
	{
		String[] result = new String[objs.length];

		for (int i = 0; i != objs.length; i++)
		{
			result[i] = (String) objs[i];
		}

		return result;
	}

	private String getRepositoryPath()
	{
		return getStringValue(ENGINE_TEMPDIR_KEY, ENGINE_DEFAULT_TEMPDIR);
	}

	private String getStringValue(String key, String defaultValue)
	{
		try
		{
			String value = StringKey.getValue(key);

			return value;
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	private FileUpload _fileUpload;
	private List<FileItem> _items;
}