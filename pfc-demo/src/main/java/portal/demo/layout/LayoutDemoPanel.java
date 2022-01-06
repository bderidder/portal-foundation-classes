package portal.demo.layout;

import portal.ui.IAction;
import portal.ui.comp.Block;
import portal.ui.comp.Button;
import portal.ui.comp.Label;
import portal.ui.comp.Panel;
import portal.ui.comp.TextInput;
import portal.ui.render.FlowLayout;
import portal.ui.render.GridConstraint;
import portal.ui.render.Insets;

public class LayoutDemoPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public LayoutDemoPanel()
	{
		createPanel();
	}

	private void createPanel()
	{
		setLayout(new FlowLayout(FlowLayout.VERTICAL));

		Label label;

		Panel formPanel = new Panel();
		formPanel.setLayout(new FlowLayout(FlowLayout.HORIZONTAL));

		_greetingLabel = new Label();
		_greetingLabel.getModel().setValue("Hello");

		label = new Label();
		label.getModel().setValue("Your name:");

		_textInput = new TextInput();

		GridConstraint formConstraint = new GridConstraint(new Insets(1, 1, 1,
				1), new Insets(0, 0, 0, 0));

		formPanel.add(new Block(Block.P, "", label), formConstraint);
		formPanel.add(_textInput, formConstraint);

		_button = new Button();
		_button.setCaption("Greet");
		_button.setAction(new MyAction());

		GridConstraint flowConstraint = new GridConstraint(new Insets(5, 5, 5,
				5), new Insets(0, 0, 0, 0));

		add(new Block(Block.P, "", _greetingLabel), flowConstraint);
		add(formPanel, flowConstraint);
		add(_button, flowConstraint);
	}
	private Label _greetingLabel;
	private TextInput _textInput;
	private Button _button;

	private class MyAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void doAction()
		{
			_greetingLabel.getModel().setValue(
					"Hello " + _textInput.getModel().getValue());
		}
	}
}
