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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;

import portal.rt.ui.render.BrowserDevice;

public final class ProcessUtils
{
    public static BrowserDevice getBrowserDevice(HttpSession session)
    {
        return (BrowserDevice) session.getAttribute(ProcessConstants.BROWSER_DEVICE_INSTANCE_SESSION_KEY);
    }

    public static void setBrowserDevice(HttpSession session,
            BrowserDevice pBrowserDevice)
    {
        session.setAttribute(
                ProcessConstants.BROWSER_DEVICE_INSTANCE_SESSION_KEY,
                pBrowserDevice);
    }

    public static ServletPortalContext getPortalContext(HttpSession session)
    {
        return (ServletPortalContext) session.getAttribute(ProcessConstants.PORTAL_CONTEXT_SESSION_KEY);
    }

    public static void setPortalContext(HttpSession session,
            ServletPortalContext portalContext)
    {
        session.setAttribute(ProcessConstants.PORTAL_CONTEXT_SESSION_KEY,
                portalContext);
    }

    public static void dumpRequest(HttpServletRequest request, Log pLog)
    {
        pLog.debug("start request dump");

        Enumeration<?> paramEnum = request.getParameterNames();

        while (paramEnum.hasMoreElements())
        {
            String name = (String) paramEnum.nextElement();
            String value = request.getParameter(name);

            pLog.debug("parameter name " + name);
            pLog.debug("parameter value " + value);
        }

        pLog.debug("end request dump");
    }

    private ProcessUtils()
    {
        // utility class
    }
}
