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

import java.io.IOException;

import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.comp.Button;
import portal.ui.comp.Label;
import portal.ui.comp.Panel;
import portal.ui.comp.ProxyComponent;
import portal.ui.render.FreeMarkerLayout;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class Wizard extends Component<WizardModel>
{
	private static final long serialVersionUID = 1L;

	public Wizard()
	{
		createUI();
	}

	public WizardModel getWizardModel()
	{
		return getModel();
	}

	@Override
	public WizardModel createDefaultModel()
	{
		return new WizardModel();
	}

	@Override
	public void beforeRender() throws RenderException
	{
		WizardModel model = getWizardModel();
		WizardState wizardState = model.getWizardState();
		WizardPage currentPage = wizardState.getCurrentPage();

		_pageLabel.getModel().setValue(currentPage.getPageTitle());

		if (wizardState.hasPrevious())
		{
			_previousButton.setEnabled(true);
		}
		else
		{
			_previousButton.setEnabled(false);
		}

		if (wizardState.hasNext())
		{
			_nextButton.setEnabled(true);
		}
		else
		{
			_nextButton.setEnabled(false);
		}

		if (wizardState.hasNext())
		{
			_finishButton.setEnabled(false);
		}
		else
		{
			_finishButton.setEnabled(true);
		}

		_proxy.setComponent(currentPage.getComponent());
	}

	@Override
	public void draw(IRenderContext pRenderContext) throws RenderException, IOException
	{
		pRenderContext.includeComponent(_panel);
	}

	private IAction getPreviousAction()
	{
		return new PreviousAction();
	}

	private IAction getNextAction()
	{
		return new NextAction();
	}

	private IAction getFinishAction()
	{
		return new FinishAction();
	}

	private IAction getCancelAction()
	{
		return new CancelAction();
	}

	private void createUI()
	{
		_panel = new Panel();
		_panel.setParent(this);

		_pageLabel = new Label();

		_previousButton = new Button();
		_previousButton.setCaption("Previous");
		_previousButton.setAction(getPreviousAction());

		_nextButton = new Button();
		_nextButton.setCaption("Next");
		_nextButton.setAction(getNextAction());

		_cancelButton = new Button();
		_cancelButton.setCaption("Cancel");
		_cancelButton.setAction(getCancelAction());

		_finishButton = new Button();
		_finishButton.setCaption("Finish");
		_finishButton.setAction(getFinishAction());

		_proxy = new ProxyComponent();

		_panel.add(_pageLabel, "WizardPageLabel");
		_panel.add(_previousButton, "PreviousButton");
		_panel.add(_nextButton, "NextButton");
		_panel.add(_cancelButton, "CancelButton");
		_panel.add(_finishButton, "FinishButton");
		_panel.add(_proxy, "WizardPage");

		_panel.setLayout(new FreeMarkerLayout("portal/comp/wizard/Wizard.ftl"));
	}
	private Label _pageLabel;
	private Button _previousButton;
	private Button _nextButton;
	private Button _cancelButton;
	private Button _finishButton;
	private ProxyComponent _proxy;
	private Panel _panel;

	private class PreviousAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void doAction()
		{
			getWizardModel().validate();
			getWizardModel().previousPage();
		}
	}

	private class NextAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void doAction()
		{
			getWizardModel().validate();
			getWizardModel().nextPage();
		}
	}

	private class FinishAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void doAction()
		{
			getWizardModel().validate();
			getWizardModel().finish();
		}
	}

	private class CancelAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void doAction()
		{
			getWizardModel().validate();
			getWizardModel().cancel();
		}
	}
}
