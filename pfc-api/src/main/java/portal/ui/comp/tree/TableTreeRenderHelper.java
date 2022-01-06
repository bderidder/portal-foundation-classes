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

package portal.ui.comp.tree;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import portal.ui.IAction;
import portal.ui.comp.IconResource;
import portal.ui.comp.Image;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class TableTreeRenderHelper
{
	public static final IconResource FOLDER_PLUS = new IconResource(
			"portal/comp/tree/plus.gif", "image/gif");
	public static final IconResource FOLDER_MINUS = new IconResource(
			"portal/comp/tree/minus.gif", "image/gif");
	public static final IconResource FOLDER_LEAF = new IconResource(
			"portal/comp/tree/leaf.gif", "image/gif");

	public static final IconResource FOLDER_CLOSED = new IconResource(
			"portal/comp/tree/closed_unselected.gif", "image/gif");
	public static final IconResource FOLDER_OPEN = new IconResource(
			"portal/comp/tree/open_unselected.gif", "image/gif");

	public static final Image LEAF_ICON = new Image(FOLDER_LEAF);

	public void renderTree(IRenderContext pRenderContext,
			TreeModel treeModel,
			boolean renderRoot) throws RenderException, IOException
	{
		if (!renderRoot)
		{
			renderTreeNodeChildren(pRenderContext, treeModel);
		}
		else
		{
			TreeNodeModel treeNode = (TreeNodeModel) treeModel;

			PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

			writer.println("<table " + "class='TreeNodeTable' "
					+ "cellspacing='0' " + "cellpadding='3'>");

			writer.println("<tr>");
			writer.println("<td class='TreeNodeToggle' "
					+ "width='15' align='left' " + "valign='middle'>");

			if (treeNode.isOpened() || treeNode.hasChildNodes())
			{
				renderToggleLink(pRenderContext, treeNode);
			}
			else
			{
				pRenderContext.includeComponent(LEAF_ICON);
			}

			writer.println("</td>");
			writer.println("<td class='TreeNodeLabel' "
					+ "valign='middle' align='left'>");
			writer.print("<nobr>");

			renderSelectLink(pRenderContext, treeNode);

			writer.println("</nobr>");
			writer.println("</td>");
			writer.println("</tr>");

			if ((!treeNode.getChildren().isEmpty()) && treeNode.isOpened())
			{
				writer.println("<tr>");
				writer.println("<td class='TreeNodeToggle' "
						+ "width='15' align='left' "
						+ "valign='middle'>&nbsp;</td>");
				writer.println("<td class='TreeNodeLabel' "
						+ "valign='middle' align='left'>");

				renderTreeNodeChildren(pRenderContext, treeNode);

				writer.println("</td>");
				writer.println("</tr>");
			}

			writer.println("</table>");
		}
	}

	private void renderTreeNodeChildren(IRenderContext pRenderContext,
			TreeNodeModel parentNodeModel) throws RenderException, IOException
	{
		PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

		writer.println("<table " + "class='TreeNodeTable' "
				+ "cellspacing='0' " + "cellpadding='3'>");

		Collection<TreeNodeModel> children = parentNodeModel.getChildren();

		if (children != null)
		{
			Iterator<TreeNodeModel> it = children.iterator();

			while (it.hasNext())
			{
				TreeNodeModel treeNodeChild = it.next();

				writer.println("<tr>");
				writer.println("<td class='TreeNodeToggle' "
						+ "width='15' align='left' " + "valign='middle'>");

				if (treeNodeChild.isOpened() || treeNodeChild.hasChildNodes())
				{
					renderToggleLink(pRenderContext, treeNodeChild);
				}
				else
				{
					pRenderContext.includeComponent(LEAF_ICON);
				}

				writer.println("</td>");
				writer.println("<td class='TreeNodeLabel' "
						+ "valign='middle' align='left'>");
				writer.print("<nobr>");

				renderSelectLink(pRenderContext, treeNodeChild);

				writer.println("</nobr>");
				writer.println("</td>");
				writer.println("</tr>");

				if ((!treeNodeChild.getChildren().isEmpty())
						&& treeNodeChild.isOpened())
				{
					writer.println("<tr>");
					writer.println("<td class='TreeNodeToggle' "
							+ "width='15' align='left' "
							+ "valign='middle'>&nbsp;</td>");
					writer.println("<td class='TreeNodeLabel' "
							+ "valign='middle' align='left'>");

					renderTreeNodeChildren(pRenderContext, treeNodeChild);

					writer.println("</td>");
					writer.println("</tr>");
				}
			}
		}
		else
		{
			// no children or closed, do nothing
		}

		writer.println("</table>");
	}

	private void renderSelectLink(IRenderContext pRenderContext,
			TreeNodeModel treeNodeModel) throws RenderException, IOException
	{
		IAction action = new TreeNodeSelectAction(treeNodeModel);

		String actionUrl = pRenderContext.createActionUrl(action);

		StringBuffer buffer = new StringBuffer();

		buffer.append("<a");

		addAttribute("href", actionUrl, buffer);
		addAttribute("class", "TreeNodeSelect", buffer);

		buffer.append(">");

		buffer.append(treeNodeModel.getName());

		buffer.append("</a>");

		PrintWriter writer = new PrintWriter(pRenderContext.getWriter());

		writer.println(buffer.toString());
	}

	private void renderToggleLink(IRenderContext pRenderContext,
			TreeNodeModel treeNodeModel) throws RenderException, IOException
	{
		IAction action = new TreeNodeToggleAction(treeNodeModel);

		Image image;

		if (treeNodeModel.isOpened() && treeNodeModel.hasChildNodes())
		{
			image = new Image(FOLDER_OPEN);
		}
		else
		{
			image = new Image(FOLDER_CLOSED);
		}

		image.setAction(action);

		pRenderContext.includeComponent(image);
	}

	private void addAttribute(String name, String value, StringBuffer buffer)
	{
		if (value != null)
		{
			buffer.append(" " + name + "=\"" + value + "\"");
		}
	}
}