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

package portal.ui.comp.tree;

import java.io.IOException;

import portal.ui.Component;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class Tree extends Component<TreeModel>
{
	private static final long serialVersionUID = 1L;

	public TreeModel getTreeModel()
	{
		return getModel();
	}

	public TreeModel createDefaultModel()
	{
		return new TreeModel();
	}

	public void setShowRoot(boolean showRoot)
	{
		this._showRoot = showRoot;
	}

	public boolean getShowRoot()
	{
		return _showRoot;
	}

	public void draw(IRenderContext pRenderContext) throws RenderException, IOException
	{
		TreeModel treeModel = getTreeModel();

		TableTreeRenderHelper renderHelper = new TableTreeRenderHelper();

		renderHelper.renderTree(pRenderContext, treeModel, _showRoot);
	}

	private boolean _showRoot = true;
}