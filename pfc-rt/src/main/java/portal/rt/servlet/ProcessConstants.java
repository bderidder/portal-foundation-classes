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

public class ProcessConstants
{
	public static final String SESSION_TIMED_OUT_URL = "/TimedOut.jsp";
	public static final String RENDER_URL = "/RenderPortal";

	public static final String BROWSER_DEVICE_INSTANCE_SESSION_KEY = "portal.core.servlet.BROWSER_DEVICE_INSTANCE";

	public static final String PORTAL_CONTEXT_SESSION_KEY = "portal.core.servlet_PORTAL_CONTEXT_";

	public static final String ACTION_REQUEST_KEY = "action";
	public static final String RESOURCE_REQUEST_KEY = "resource";

	public static final int HEARTBEAT_REFRESH = 1000; // seconds
	public static final long MAX_IDLETIME = 1800; // seconds
}