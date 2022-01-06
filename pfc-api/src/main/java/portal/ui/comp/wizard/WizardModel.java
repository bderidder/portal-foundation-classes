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
package portal.ui.comp.wizard;

import java.util.ArrayList;
import java.util.Iterator;

import portal.model.IModel;

public class WizardModel implements IModel
{
    //private static final Log LOGGER = LogFactory.getLog(WizardModel.class);
    private static final long serialVersionUID = 1L;

    public WizardModel()
    {
        _pages = new ArrayList<>();
        _listeners = new ArrayList<>();

        _currentPage = 0;

        _wizardState = new MyWizardState();
    }

    public WizardState getWizardState()
    {
        return _wizardState;
    }

    public void validate()
    {
        for (int i = 0; i != _pages.size(); i++)
        {
            InternalWizardPage page = _pages.get(i);

            page.validate();
        }
    }

    public void nextPage()
    {
        if (_wizardState.getCurrentInternalPage().isValid())
        {
            getWizardState().nextPage();
        }
    }

    public void previousPage()
    {
        if (_wizardState.getCurrentInternalPage().isValid())
        {
            getWizardState().previousPage();
        }
    }

    public int getWizardPageCount()
    {
        return _pages.size();
    }

    public void addWizardPage(WizardPage wizardPage)
    {
        _pages.add(new InternalWizardPage(wizardPage));

        if (_pages.size() == 1)
        {
            _wizardState.beforeShowCurrentPage();
        }
    }

    public void addWizardPage(int index, WizardPage wizardPage)
    {
        _pages.add(index, new InternalWizardPage(wizardPage));
    }

    public WizardPage remove(int index)
    {
        return _pages.remove(index).getWizardPage();
    }

    public void finish()
    {
        if (_wizardState.getCurrentInternalPage().isValid())
        {
            notifyFinished();
        }
    }

    public void addWizardListener(WizardListener listener)
    {
        _listeners.add(listener);
    }

    public void removeWizardListener(WizardListener listener)
    {
        _listeners.remove(listener);
    }

    public void cancel()
    {
        notifyCancelled();
    }

    private void notifyFinished()
    {
        ArrayList<WizardListener> temp;

        synchronized (_listeners)
        {
            temp = new ArrayList<>(_listeners);
        }

        Iterator<WizardListener> it = temp.iterator();
        while (it.hasNext())
        {
            WizardListener listener = it.next();

            listener.finished();
        }
    }

    private void notifyCancelled()
    {
        ArrayList<WizardListener> temp;

        synchronized (_listeners)
        {
            temp = new ArrayList<>(_listeners);
        }

        Iterator<WizardListener> it = temp.iterator();
        while (it.hasNext())
        {
            WizardListener listener = it.next();

            listener.cancelled();
        }
    }
    private ArrayList<WizardListener> _listeners;
    private ArrayList<InternalWizardPage> _pages;
    private int _currentPage;
    private MyWizardState _wizardState;

    protected class MyWizardState implements WizardState
    {
        private static final long serialVersionUID = 1L;

        @Override
        public WizardPage getCurrentPage()
        {
            return _pages.get(_currentPage).getWizardPage();
        }

        public InternalWizardPage getCurrentInternalPage()
        {
            return _pages.get(_currentPage);
        }

        @Override
        public void nextPage()
        {
            if (hasNext())
            {
                afterHideCurrentPage();
                _currentPage++;
                beforeShowCurrentPage();
            }
        }

        @Override
        public void previousPage()
        {
            if (hasPrevious())
            {
                afterHideCurrentPage();
                _currentPage--;
                beforeShowCurrentPage();
            }
        }

        @Override
        public boolean hasNext()
        {
            return (_currentPage + 1 < getWizardPageCount());
        }

        @Override
        public boolean hasPrevious()
        {
            return (_currentPage > 0);
        }

        @Override
        public void reset()
        {
            afterHideCurrentPage();
            _currentPage = 0;
            beforeShowCurrentPage();
        }

        public void afterHideCurrentPage()
        {
            if (getCurrentPage() != null)
            {
                getCurrentPage().afterHidePage();
            }
        }

        public void beforeShowCurrentPage()
        {
            if (getCurrentPage() != null)
            {
                getCurrentPage().beforeShowPage();
            }
        }
    }
}
