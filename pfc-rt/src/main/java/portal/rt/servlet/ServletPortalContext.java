/*
 * Copyright 2000 - 2004, Bavo De Ridder
 *
 * This file is part of Portal Foundation Classes.
 *
 * Portal Foundation Classes is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * Portal Foundation Classes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Portal Foundation Classes; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston
 *
 * http://www.gnu.org/licenses/gpl.html
 */
package portal.rt.servlet;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.rt.ui.render.BrowserDevice;
import portal.ui.PortalContext;

public class ServletPortalContext extends PortalContext
{
	private static final Log LOGGER = LogFactory.getLog(ServletPortalContext.class);

	public static void setPortalContext(PortalContext portalContext)
	{
		LOGGER.debug("Setting portal context " + portalContext);

		getThreadLocalPortalContext().set(portalContext);
	}

	public ServletPortalContext(BrowserDevice pBrowserDevice)
	{
		_browserDevice = pBrowserDevice;
		_initParams = new HashMap<>();
	}

	@Override
	public String getInitParameter(String name)
	{
		return _initParams.get(name);
	}

	public void addInitParameter(String name, String value)
	{
		_initParams.put(name, value);
	}

	@Override
	public Principal getUserPrincipal()
	{
		return _userPrincipal;
	}

	@Override
	public void setUserPrincipal(Principal userPrincipal)
	{
		LOGGER.debug("Setting principal " + userPrincipal.getName());

		this._userPrincipal = userPrincipal;
	}
	private BrowserDevice _browserDevice;
	private Principal _userPrincipal;
	private Map<String, String> _initParams;
}
