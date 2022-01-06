/*
 * Copyright 2000,  Bavo De Ridder
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
package portal.ui;

import java.io.IOException;
import java.io.Serializable;

import portal.model.IModel;
import portal.ui.render.IRenderContext;
import portal.ui.render.LayoutManager;
import portal.ui.render.RenderException;
import portal.ui.render.Renderable;

/**
 * <p>This is the super class for all classes that take part in the user
 * interface. An instance of a component can be rendered and (optionally)
 * manipulated by the user.</p>
 *
 * <p>A component can optionally have a model to store it's internal state. This
 * is not an obligation, some simpler components can choose to store their
 * internal state within the component instance itself. Only components that
 * which to be more generic and extensible by means of implementing the required
 * Model interface, can delegate some of the logic to a model.</p>
 *
 * <p>Every component has a parent. The only exceptions are top level components
 * like a frame. See the documentation about internal desktop management. Not
 * every component will have children. The Component classes only models the
 * parent relationship. The Container class keeps track of the child
 * relationship. Developers wishing to implement their own container classes
 * will have to make sure the parent - child relationship is consistent.</p>
 *
 * <p>The component instance can choose to render itself by overriding the
 * {@link #render(IRenderContext) render()} method, or it can delegate the
 * rendering to a layout manager. Layout managers can be set using the
 * {@link #setLayout(LayoutManager) setLayout()} method.
 *
 * @author bavodr
 *
 * @since 0.9
 */
public class Component<ModelType extends IModel> implements Serializable, Renderable
{
    private static final long serialVersionUID = 1L;

    /**
     * <p>Creates a new
     * <code>Component</code> instance.</p>
     *
     * <p>Calling this method is equivalent to calling
     * <code>Component(null, null)</code></p>
     *
     * See also {@link #Component(String,IModel)}.
     *
     */
    public Component()
    {
        this(null, null);
    }

    /**
     * <p>Creates a new
     * <code>Component</code> instance with the given name.</p>
     *
     * <p>Calling this method is equivalent to calling
     * <code>Component(name, null)</code></p>
     *
     * See also {@link #Component(String,IModel)}.
     *
     * @param name the name of this component or null for no name
     */
    public Component(String name)
    {
        this(name, null);
    }

    /**
     * <p>Creates a new
     * <code>Component</code> instance with the given initial model.</p>
     *
     * <p>Calling this method is equivalent to calling
     * <code>Component(null, model)</code></p>
     *
     * See also {@link #Component(String,IModel)}.
     *
     * @param model the initial value of the model of this instance or null for
     * no initial model
     */
    public Component(ModelType model)
    {
        this(null, model);
    }

    /**
     * <p>Creates a new
     * <code>Component</code> instance with the given name and initial
     * model.</p>
     *
     * <p>The name is only used in debug output. Both the name and model
     * parameters can be null.</p>
     *
     * @param name the name of this new instance of Component
     * @param model the model of this component
     */
    public Component(String name, ModelType model)
    {
        _name = name;

        if (model == null)
        {
            _model = createDefaultModel();
        }
        else
        {
            _model = model;
        }

        _id = RandomIDGenerator.getRandomID();
    }

    /**
     * <p>Returns a unique id associated with this component.</p>
     *
     * <p>The unique id can be used when the component needs to be referenced
     * outside the java environment. For instance with the id attribute of DHTML
     * elements.</p>
     *
     * @return a unique id associated with this component
     */
    public final String getId()
    {
        return _id;
    }

    /**
     * <p>Sets a new parent. When the new parent is null, it will reset the
     * parent and make this component top level in the UI tree.</p>
     *
     * @param parent a <code>Component</code> value
     */
    public final void setParent(Component<?> parent)
    {
        this._parent = parent;
    }

    /**
     * <p>Returns the current parent or null if there is no parent.</p>
     *
     * @return the reference to the parent or null if no parent
     */
    public final Component<?> getParent()
    {
        return _parent;
    }

    /**
     * <p>Return if this component is visible or not. See
     * {@link #setVisible(boolean)} for more information.</p>
     *
     * @return true if visible, false otherwise
     */
    public final boolean isVisible()
    {
        return _visible;
    }

    /**
     * <p>Set the visibility for this component. Visible means this component
     * will be rendered. When the component is not visible, it will be ignored
     * in the rendering process.</p>
     *
     * @param visible true if this component should be visible, false for not
     * visible
     */
    public final void setVisible(boolean visible)
    {
        this._visible = visible;
    }

    /**
     * <p>Determines whether this component is enabled. An enabled component can
     * respond to user input and generate events. Components are enabled
     * initially by default.</p>
     *
     * <p>See also {@link #setEnabled(boolean)}.
     *
     * @return true when the component is enabled, false otherwise
     */
    public final boolean isEnabled()
    {
        return _enabled;
    }

    /**
     * <p>Enables or disables this component, depending on the value of the
     * parameter enabled. An enabled component can respond to user input.
     * Components are enabled initially by default.</p>
     *
     * @param enabled when true, the component is enabled, false if the
     * component should be disabled
     */
    public final void setEnabled(boolean enabled)
    {
        this._enabled = enabled;
    }

    /**
     * <p>This method is called before the actual rendering takes place. This is
     * true both when the
     * <code>draw()</method> or a layout manager is used for rendering.</p>
     *
     * <p>When this method throws an exception, the render process for the tree
     * rooted at this component is stopped.</p>
     */
    public void beforeRender() throws RenderException
    {
    }

    /**
     * Describe
     * <code>render</code> method here.
     *
     * @param renderContext the engine render context
     * @exception RenderException when an error occurs during the rendering
     */
    public final void render(IRenderContext renderContext)
            throws RenderException, IOException
    {
        beforeRender();

        try
        {
            LayoutManager myLayout = getLayout();

            if (myLayout != null)
            {
                myLayout.doLayout(this, renderContext);
            }
            else
            {
                draw(renderContext);
            }
        }
        finally
        {
            afterRender();
        }
    }

    /**
     * <p>This method is called after the actual rendering (either through a
     * layout manager or by the draw method implementation) took place. Even
     * when the
     * <code>render()</code> threw an exception.</p>
     *
     * <p>It is guaranteed that this method is called, even when the previous
     * render step threw an error. However when the
     * <code>beforeRender()</code> method threw an exception, the
     * <code>afterRender()</code> method is not called.</p>
     */
    public void afterRender() throws RenderException
    {
    }

    /**
     * <p>With this method the component can render itself using the passed
     * IRenderContext object. This method is not called when a layout manager is
     * set since the existence of a layout manager takes precedence above the
     * <code>draw()</code> method.</p>
     *
     * @param pRenderContext the render context instance to use for drawing this
     * component
     *
     * @throws RenderException when the rendering process could not proceed or
     * encountered an error
     */
    protected void draw(IRenderContext pRenderContext) throws RenderException, IOException
    {
    }

    /**
     * <p>Get the layout manager that will render this component, if set.</p>
     *
     * @return the current layout manager or null when none is set
     */
    public final LayoutManager getLayout()
    {
        return _layout;
    }

    /**
     * <p>Set the layout manager for this component. Passing null resets the
     * layout manager.</p>
     *
     * @param layout the new layout manager.
     */
    public final void setLayout(LayoutManager layout)
    {
        this._layout = layout;
    }

    /**
     * <p>Returns the current model instance or null when there is no model for
     * this component.</p>
     *
     * @return the current model or null when there is no model set
     */
    public final ModelType getModel()
    {
        return _model;
    }

    /**
     * <p>Set a new model for this component. Passing null will reset the
     * currently set model.</p>
     *
     * @param model the new model or null to reset the current model
     */
    protected final void setModel(ModelType model)
    {
        this._model = model;
    }

    /**
     * <p>This method is called by the constructor to create a default model for
     * this component. If this component type does not use a model or does not
     * have a suitable default model, this method can return null.</p>
     *
     * <p>The default implementation should return null.</p>
     *
     * @return an instance of a Model or null when there is no default model
     */
    protected ModelType createDefaultModel()
    {
        return null;
    }

    /*
     * This method should return a String useful for a developer to recognize this
     * component in a debug dump. The default implementation will return the actual
     * class name and the component name. Component implementors might choose to
     * override this method and return a more suitable value.
     *
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        if (_name != null)
        {
            return "Component{" + getClass().getName() + "[" + _name + "]" + "}";
        }
        else
        {
            return "Component{" + getClass().getName() + "}";
        }
    }
    private String _name;
    private String _id;
    private boolean _visible = true;
    private boolean _enabled = true;
    private ModelType _model;
    private LayoutManager _layout;
    private Component<?> _parent;
}
