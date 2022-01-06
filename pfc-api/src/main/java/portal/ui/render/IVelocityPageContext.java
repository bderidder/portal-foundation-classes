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

import portal.ui.Component;

/**
 * An object instance implementing this interface is available to a velocity
 * template layout as <tt>$context</tt>.
 *
 * Note that none of the methods throws a checked exception. This is to avoid
 * the often very long and meaningless stacktraces thrown by the velocity
 * engine. Each method will however log any errors, warnings and exceptions.
 *
 * @author bavodr
 *
 * @since 0.9
 */
public interface IVelocityPageContext
{
    /**
     * <p>Retrieve the component being rendered with tis velocity template. The
     * reference can then be used from within the template.</p>
     *
     * @return the component that is being rendered with this velocity template
     */
    Component<?> getComponent();

    /**
     * <p>Check if a child with the given name is available. This method can be
     * used from within a velocity template layout for conditional inclusion of
     * a child. It checks if the component rendered by this layout is a
     * container and, when yes, if it contains the named child.</p>
     *
     * @param componentName the name of the child component
     *
     * @return true when there is such a child, false otherwise
     */
    boolean hasChild(String componentName) throws RenderException;

    /**
     * <p>Search for a component in this container with the given name. When
     * found include the rendering output of this component at the current
     * location.</p>
     *
     * <p>When the component being rendered is not a container or does not
     * contain the named child, a warning will be logged and the rendering
     * continues.</p>
     *
     * @param componentName the name of the child component
     */
    void includeChild(String componentName) throws RenderException;

    /**
     * <p>Query the resource service for the named resource. When found, it will
     * register the resource with the render engine and return an url to
     * retrieve the resource.</p>
     *
     * <p>This can be used to include images or other resources in the html
     * output without the need of instantiating an Image class for example.</p>
     *
     * <p>Example:</p>
     *
     * <tt>&lt;img src='$context.getResourceURL("image.png")'></tt>
     *
     * @param resourceName the name of the resource
     *
     * @return the relative url that can be used to retrieve the contents of the
     * resource
     */
    String getResourceURL(String resourceName) throws RenderException;

    /**
     * <p>Query the resource service for the named resource. When found, it will
     * register the resource with the render engine and return an url to
     * retrieve the resource.</p>
     *
     * <p>Some HTTP headers will be added so that the browser will present the
     * user with a "Save As ..." dialog instead of showing the resource inline.
     * Note that the browser can choose to implement this behavior
     * differently.</p>
     *
     * <p>Example:</p>
     *
     * <tt>&lt;img src='$context.getDownloadURL("resources/graphics/image.png",
     * "image.png)'></tt>
     *
     * @param resourceName the name of the resource
     * @param filename the suggested filename in the browsers Save As dialog
     *
     * @return the relative url that can be used to retrieve the contents of the
     * resource
     */
    String getDownloadURL(String resourceName, String filename) throws RenderException;
}
