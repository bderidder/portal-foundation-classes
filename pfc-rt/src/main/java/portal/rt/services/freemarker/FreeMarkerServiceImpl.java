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
package portal.rt.services.freemarker;

import freemarker.cache.ClassTemplateLoader;
import java.util.Iterator;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.services.IServiceConfig;
import portal.services.freemarker.IFreeMarkerService;

public class FreeMarkerServiceImpl implements IFreeMarkerService
{
	private static final Log LOGGER = LogFactory.getLog(FreeMarkerServiceImpl.class);
	private IServiceConfig _serviceConfig;
	private Configuration _configuration;

	public FreeMarkerServiceImpl(IServiceConfig serviceConfig)
	{
		_serviceConfig = serviceConfig;
	}

	@Override
	public Configuration getConfiguration()
	{
		return _configuration;
	}

	public void start()
	{
		try
		{
			_configuration = new Configuration();
			_configuration.setObjectWrapper(new DefaultObjectWrapper());
			_configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			_configuration.setIncompatibleImprovements(new Version(2, 3, 20));
			_configuration.setDefaultEncoding("UTF-8");

			ClassTemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/");

			_configuration.setTemplateLoader(templateLoader);

			if (_serviceConfig.getAttributeNames()
					!= null)
			{
				Iterator<String> it = _serviceConfig.getAttributeNames().iterator();
				while (it.hasNext())
				{
					String attrName = it.next();
					String attrValue = _serviceConfig.getAttributeValue(attrName);

					LOGGER.debug("adding FreeMarker setting " + attrName
							+ " with value " + attrValue);

					_configuration.setSetting(attrName, attrValue);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.fatal("Could not initialize Freemarker framework", e);
		}
	}

	public void stop()
	{
		_configuration = null;
	}
}
