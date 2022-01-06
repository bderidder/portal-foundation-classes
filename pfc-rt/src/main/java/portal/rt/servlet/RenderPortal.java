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

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.rt.debug.DebugConfig;
import portal.rt.ui.render.GraphicsDevice;

public class RenderPortal extends BasePortalServlet
{
    private static final long serialVersionUID = 1L;
    private static final int BYTE_PER_KB = 1024;
    private static final Log LOGGER = LogFactory.getLog(RenderPortal.class);

    @Override
    public void process(IHttpProcessContext processContext)
            throws ServletException
    {
        long beginTime = System.currentTimeMillis();

        try
        {
            LOGGER.debug("Fetching graphics device for this session");

            GraphicsDevice graphicsDevice = processContext.getBrowserDevice()
                    .getGraphicsDevice();

            LOGGER.debug("Rendering the graphics device");

            graphicsDevice.render(processContext);

            calculateSessionSize(graphicsDevice);
        }
        catch (Exception e)
        {
            LOGGER.error("Could not process post request", e);
        }
        finally
        {
            long endTime = System.currentTimeMillis();

            LOGGER.debug("render time " + (endTime - beginTime));
        }
    }

    private void calculateSessionSize(Object o)
    {
        try
        {
            if (DebugConfig.getDefaultConfig().sessionSize())
            {
                ByteArrayOutputStream oStream = new ByteArrayOutputStream();
                ObjectOutputStream objOStream = new ObjectOutputStream(oStream);

                objOStream.writeObject(o);

                objOStream.flush();

                int size = oStream.size();

                LOGGER.info("Session is: " + (size / BYTE_PER_KB) + "kB");
            }
        }
        catch (Exception e)
        {
            LOGGER.error("Could not calculate session size", e);
        }
    }
}
