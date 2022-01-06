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

package portal.rt.ui.resource;

public final class ResourceUtils
{
	public static String createResourceURL(boolean isStatic, String resourceKey)
	{
		if (isStatic)
		{
			return "resource/" + resourceKey;
		}
		else
		{
			return "getResource?resource=" + resourceKey;
		}
	}

	public static String createDownloadURL(boolean isStatic,
			String resourceKey,
			String filename)
	{
		if (isStatic)
		{
			return "resource/" + resourceKey + "/" + filename;
		}
		else
		{
			return "getResource?resource=" + resourceKey + "&name=" + filename;
		}
	}

	private ResourceUtils()
	{
	}
}
