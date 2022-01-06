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
package portal.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import portal.model.IModel;
import portal.ui.render.LayoutManager;

/**
 * <p>This is the base class for all components that can contain a list of
 * children. This class manages the link between these child components and
 * their parent (this Container instance).</p>
 *
 * <p>The rendering of this container is delegated to layout managers. Setting a
 * layout manager for a container is done by calling the method
 * {@link portal.ui.Component#setLayout(LayoutManager)}.</p>
 *
 * <p>Some layout managers require more information to render the children of
 * the container. For these specific layout managers you can pass a value using
 * the constraint parameter of the add(...) methods. The actual value is only
 * interpreted by the layout manager. See the documentation of the
 * {@link portal.ui.render.LayoutManager layout manager} used for possible
 * values and their meaning.</p>
 *
 * @author bavodr
 *
 * @since 0.9
 */
public class Container extends Component<IModel>
{
    private static final long serialVersionUID = 1L;

    /**
     * <p>Create a new Container instance.</p>
     *
     * <p>Calling this constructor is is identical to calling
     * {@link #Container(String,LayoutManager)} with a name equal to null and a
     * layout equal to null.</p>
     *
     * <p>For this container to be rendered, you have to call the method
     * {@link portal.ui.Component#setLayout(LayoutManager)} and set a layout
     * manager.</p>
     */
    public Container()
    {
        this(null, null);
    }

    /**
     * <p>Create a new Container instance with an initial layout manager.</p>
     *
     * <p>Calling this constructor is is identical to calling
     * {@link #Container(String,LayoutManager)} with a name equal to null.</p>
     *
     * @param layout the initial layout manager for this container
     */
    public Container(LayoutManager layout)
    {
        this(null, layout);
    }

    /**
     * <p>Create a new Container instance with the give name and layout manager.
     * The layout manager can be replaced by a later call to
     * {@link portal.ui.Component#setLayout(LayoutManager)}</p>
     *
     * <p>See also {@link portal.ui.Component#Component(String)}.</p>
     *
     * @param name the name of this container
     * @param layout the initial layout manager for this container
     */
    public Container(String name, LayoutManager layout)
    {
        super(name);

        _components = new LinkedList<Component<?>>();
        _constraints = new LinkedList<Object>();

        setLayout(layout);
    }

    /**
     * <p>Add a new child component to this container with no constraint.
     * Calling this method is identical to calling
     * {@link #add(Component,Object)} with a constraint value of null.</p>
     *
     * @param component The child component to add to this container.
     */
    public final void add(Component<?> component)
    {
        add(size(), component, null);
    }

    /**
     * <p>Add a new child component to this container with the given constraint.
     * The possible values of the constraint parameter depend on the layout
     * manager used to render this container. Some layout managers even ignore
     * this value.</p>
     *
     * <p>The parent of the child component is updated to be this container. If
     * the child belonged to another container, it will be removed from that
     * container first.</p>
     *
     * @param component The child component to add to this container.
     * @param constraint The constraint used to add this child component to this
     * container.
     */
    public final void add(Component<?> component, Object constraint)
    {
        add(size(), component, constraint);
    }

    /**
     * <p>Add a new child component to this container with the given constraint
     * and at the given index.</p>
     *
     * <p>The actual ordering of the items within the container is only used by
     * the layout manager and may be ignored.</p>
     *
     * <p>See also {@link #add(Component,Object)} for more information.</p>
     *
     * @param index The index at which to add this child component.
     * @param component The child component to add to this container.
     * @param constraint The constraint used to add this child component to this
     * container.
     */
    public final void add(int index, Component<?> component, Object constraint)
    {
        if (component.getParent() == this)
        {
            throw new IllegalArgumentException(
                    "The component "
                    + component
                    + " is already a child of this parent, "
                    + "you can not add a component twice to the same container.");
        }

        if (!(index >= 0) && (index <= _components.size()))
        {
            throw new IndexOutOfBoundsException(
                    "The given index is out of bounds: 0 <= " + index + " <= "
                    + _components.size());
        }

        removeFromOldContainer(component);
        component.setParent(this);

        _components.add(index, component);
        _constraints.add(index, constraint);
    }

    /**
     * <p>Remove the given child component from this container if present. If
     * the child component is not present in this container, nothing will
     * happen.</p>
     *
     * <p>The parent of the child component will be set to null after the child
     * is removed.</p>
     *
     * @param component the child component that has to be removed.
     */
    public final void remove(Component<?> component)
    {
        remove(_components.indexOf(component));

        component.setParent(null);
    }

    /**
     * <p>Find the first child component that has a constraint equal to the
     * given constraint and remove the child component. If there is no such
     * constraint used in this container, nothing happens.</p>
     *
     * <p>The parent of the child component will be set to null before the child
     * is removed.</p>
     *
     * @param constraint the constraint for which the child has to be removed.
     */
    public final void remove(Object constraint)
    {
        for (int i = 0; i != size(); i++)
        {
            Object currentConstraint = _constraints.get(i);

            if ((currentConstraint != null)
                    && currentConstraint.equals(constraint))
            {
                remove(i);

                return;
            }
        }
    }

    /**
     * <p>Removes the child at the given index. The index has to be within the
     * bounds set by the container (&lt; size() and &gt;= 0).</p>
     *
     * <p>The parent of the child component will be set to null before the child
     * is removed.</p>
     *
     * @param index the index of the child component that has to be removed.
     */
    public final void remove(int index)
    {
        if (!isIndexValid(index))
        {
            throw new IndexOutOfBoundsException(
                    "The given index is out of bounds, only "
                    + _components.size()
                    + " are available in this container.");
        }

        Component<?> component = _components.remove(index);

        _constraints.remove(index);

        if (component != null)
        {
            component.setParent(null);
        }
    }

    /**
     * <p>Remove all children from this container instance. The result is an
     * empty container.</p>
     *
     * <p>The parent of all child components will be set to null before the
     * child is removed.</p>
     */
    public final void removeAll()
    {
        Iterator<Component<?>> it = _components.iterator();
        while (it.hasNext())
        {
            Component<?> component = it.next();
            component.setParent(null);
        }

        _components.clear();
        _constraints.clear();
    }

    /**
     * <p>Returns the size of this container. The size is set to be equal to the
     * number of components in this container.</p>
     *
     * @return The number of components held within this container.
     */
    public final int size()
    {
        return _components.size();
    }

    /**
     * <p>Return the constraint used by the child component at the given index.
     * The index has to be within the bounds set by the container (&lt; size()
     * and &gt;= 0).</p>
     *
     * <p>It is possible that the child component at the given index does not
     * use a constraint. In that case null will be returned.</p>
     *
     * @param index the index within the container to search at.
     *
     * @return the constrained used by the child component at the given index or
     * null if no constraint was given.
     */
    public final Object getConstraint(int index)
    {
        if (!isIndexValid(index))
        {
            throw new IndexOutOfBoundsException(
                    "The given index is out of bounds, only "
                    + _components.size()
                    + " are available in this container.");
        }

        return _constraints.get(index);
    }

    /**
     * <p>Return the child component at the given index. The index has to be
     * within the bounds set by the container (&lt; size() and &gt;= 0).</p>
     *
     * @param index the index within the container to search at.
     *
     * @return the child component at the given index.
     */
    public final Component<?> getComponent(int index)
    {
        if (!isIndexValid(index))
        {
            throw new IndexOutOfBoundsException(
                    "The given index is out of bounds, only "
                    + _components.size()
                    + " are available in this container.");
        }

        return _components.get(index);
    }

    /**
     * <p>Creates a copy of the current list of child components in this
     * container. The resulting collection can be manipulated without affecting
     * the container.</p>
     *
     * @return a copy of the list of child components in this container.
     */
    public final Collection<Component<?>> getComponents()
    {
        return new LinkedList<Component<?>>(_components);
    }

    /**
     * <p>Search for the first component in this container that uses the given
     * constraint. If no such component is found, null is returned.</p>
     *
     * @param constraint the constraint to search for. Cannot be null.
     *
     * @return the first component found that uses this constraint.
     */
    public final Component<?> getComponent(Object constraint)
    {
        if (constraint == null)
        {
            throw new IllegalArgumentException("The constraint was null.");
        }

        for (int i = 0; i != size(); i++)
        {
            Object currentConstraint = _constraints.get(i);

            if ((currentConstraint != null)
                    && (currentConstraint.equals(constraint)))
            {
                return _components.get(i);
            }
        }

        return null;
    }

    /**
     * This internal utility method will check of the given child has a
     * Container instance set as its current parent. If so, it will first remove
     * the given child from the old container.
     *
     * @param child the child component
     */
    private void removeFromOldContainer(Component<?> child)
    {
        if (child.getParent() != null
                && (child.getParent() instanceof Container))
        {
            ((Container) child.getParent()).remove(child);
        }
    }

    /**
     * <p>An internal utility method to check if a given index is valid within
     * the bounds set by this container.</p>
     *
     * @param index the value to check.
     *
     * @return returns true if the index is valid for this container, false
     * otherwise.
     */
    private boolean isIndexValid(int index)
    {
        return (index >= 0) && (index < _components.size());
    }
    private LinkedList<Component<?>> _components;
    private LinkedList<Object> _constraints;
}
