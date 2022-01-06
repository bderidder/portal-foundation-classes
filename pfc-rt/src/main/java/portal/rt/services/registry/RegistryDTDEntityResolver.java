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

package portal.rt.services.registry;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RegistryDTDEntityResolver implements EntityResolver
{
	private static final Log LOGGER = LogFactory.getLog(RegistryDTDEntityResolver.class);

	private static final String DTD_URL = "http://pfc.sourceforge.net/";
	private static final String CLASSPATH_BASE = "portal/services/registry/";

	public RegistryDTDEntityResolver()
	{
	}

	public InputSource resolveEntity(String pPublicId, String pSystemId)
			throws SAXException, IOException
	{
		if (pSystemId != null && pSystemId.startsWith(DTD_URL))
		{
			LOGGER.debug("trying to locate " + pSystemId
					+ " in classpath under " + CLASSPATH_BASE);

			String path = CLASSPATH_BASE
					+ pSystemId.substring(DTD_URL.length());

			ClassLoader classLoader = getClass().getClassLoader();

			InputStream dtdStream = null;

			if (classLoader == null)
			{
				dtdStream = getClass().getResourceAsStream(path);
			}
			else
			{
				dtdStream = classLoader.getResourceAsStream(path);
			}

			if (dtdStream == null)
			{
				LOGGER.debug(pSystemId + "not found in classpath");

				return null;
			}
			else
			{
				LOGGER.debug("found " + pSystemId + " in classpath");

				InputSource source = new InputSource(dtdStream);

				source.setPublicId(pPublicId);
				source.setSystemId(pSystemId);

				return source;
			}
		}
		else
		{
			return null;
		}
	}
}