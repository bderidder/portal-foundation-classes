package portal.demo.validation;

import portal.demo.DemoEntry;
import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.comp.Button;
import portal.ui.comp.Panel;
import portal.ui.render.FreeMarkerLayout;

public class ValidationDemoPanel extends Panel implements DemoEntry
{
	private static final long serialVersionUID = 1L;

	public ValidationDemoPanel()
	{
	}

	public void init()
	{
		createPanel();
	}

	public void destroy()
	{
	}

	public Component<?> getComponent()
	{
		return this;
	}

	private void createPanel()
	{
		setLayout(new FreeMarkerLayout(
				"portal/demo/validation/ValidationDemoPanel.ftl"));

		_model = new DemoFormModel();

		_demoForm = new DemoForm(_model);

		add(_demoForm, "DemoForm");

		Button validateButton = new Button();
		validateButton.setCaption("Validate");
		validateButton.setAction(new IAction()
		{
			private static final long serialVersionUID = 1L;

			public void doAction()
			{
				validateForm();
			}
		});

		add(validateButton, "ValidateButton");
	}

	private void validateForm()
	{
		_demoForm.validate(null);
	}
	private DemoFormModel _model;
	private DemoForm _demoForm;
}
