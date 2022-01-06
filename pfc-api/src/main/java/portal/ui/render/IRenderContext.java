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

package portal.ui.render;

import java.io.IOException;
import java.io.Writer;

import portal.interfaces.IInterfaceImplementor;
import portal.services.resource.IResource;
import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.IUpdater;

public interface IRenderContext extends IInterfaceImplementor
{
	Writer getWriter();

	String addUpdater(IUpdater updater);

	void addUpdater(String key, IUpdater updater);

	boolean containsUpdaterKey(String key);

	String createActionUrl(IAction action) throws RenderException;

	String createResourceUrl(IResource resource) throws RenderException;

	String createDownloadUrl(IResource resource, String filename)
			throws RenderException;

	void includeComponent(Component<?> component) throws RenderException, IOException;
}