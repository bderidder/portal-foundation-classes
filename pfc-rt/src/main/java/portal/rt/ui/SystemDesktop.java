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
package portal.rt.ui;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.rt.ui.render.BrowserDevice;
import portal.services.registry.RegistryException;
import portal.services.registry.StringKey;
import portal.ui.Component;
import portal.ui.IApplication;
import portal.ui.comp.Panel;
import portal.ui.comp.ProxyComponent;
import portal.ui.render.FreeMarkerLayout;

public class SystemDesktop implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final String LAYOUT_CONTENT_KEY = "Content";
	private static final String REGISTRY_KEY_DESKTOP_TEMPLATE = "portal.application.DesktopTemplate";
	private static final Log LOGGER = LogFactory.getLog(SystemDesktop.class);
	private BrowserDevice _browserDevice;
	private Panel _rootPanel;
	private ProxyComponent _contentProxy;
	private IApplication _application;

	public SystemDesktop(BrowserDevice pBrowserDevice)
	{
		LOGGER.debug("Creating system desktop");

		_browserDevice = pBrowserDevice;

		startDesktop();
	}

	public BrowserDevice getBrowserDevice()
	{
		return _browserDevice;
	}

	public String getTitle()
	{
		return _application.getName();
	}

	public Component<?> getRootComponent()
	{
		return _rootPanel;
	}

	private void startDesktop()
	{
		try
		{
			createRootDesktopPanel();

			startApplication();
		}
		catch (RegistryException e)
		{
			LOGGER.error(e, e);
		}
	}

	private void startApplication()
	{
		LOGGER.info("Starting application");

		initApplication();
	}

	private void createRootDesktopPanel() throws RegistryException
	{
		String desktopTemplate = StringKey.getValue(REGISTRY_KEY_DESKTOP_TEMPLATE);

		_rootPanel = new Panel();
		_rootPanel.setLayout(new FreeMarkerLayout(desktopTemplate));

		_contentProxy = new ProxyComponent();

		_rootPanel.add(_contentProxy, LAYOUT_CONTENT_KEY);
	}

	private void initApplication()
	{
		LOGGER.debug("Creating application instance");

		try
		{
			String className = StringKey.getValue("portal.application");

			LOGGER.debug("Instantiating application class: " + className);

			Class<?> classInstance = Class.forName(className);

			_application = (IApplication) classInstance.newInstance();

			_application.init();

			_contentProxy.setComponent(_application.getComponent());
		}
		catch (RegistryException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
		{
			LOGGER.error("Can't start application instance", e);
		}
	}
}
