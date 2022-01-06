package portal.demo.layout;

import portal.ui.IAction;
import portal.ui.comp.Button;
import portal.ui.comp.Label;
import portal.ui.comp.Panel;
import portal.ui.comp.TextInput;
import portal.ui.render.FreeMarkerLayout;

public class FreeMarkerDemoPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public FreeMarkerDemoPanel()
	{
		createPanel();
	}

	private void createPanel()
	{
		setLayout(new FreeMarkerLayout(
				"portal/demo/layout/FreeMarkerDemoPanel.ftl"));

		_label = new Label();
		_label.getModel().setValue("Hello");

		_textInput = new TextInput();

		_button = new Button();
		_button.setCaption("Greet");
		_button.setAction(new MyAction());

		add(_label, "Label");
		add(_textInput, "TextInput");
		add(_button, "Button");
	}
	private Label _label;
	private TextInput _textInput;
	private Button _button;

	private class MyAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void doAction()
		{
			_label.getModel().setValue(
					"Hello " + _textInput.getModel().getValue());
		}
	}
}
