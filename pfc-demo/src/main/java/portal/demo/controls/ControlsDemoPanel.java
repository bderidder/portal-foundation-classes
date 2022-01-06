package portal.demo.controls;

import portal.demo.DemoEntry;
import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.comp.Button;
import portal.ui.comp.CheckBox;
import portal.ui.comp.IconResource;
import portal.ui.comp.Image;
import portal.ui.comp.Label;
import portal.ui.comp.Panel;
import portal.ui.comp.TextArea;
import portal.ui.comp.TextInput;
import portal.ui.render.FreeMarkerLayout;

public class ControlsDemoPanel extends Panel implements DemoEntry
{
	//private static final Log LOGGER = LogFactory.getLog(ControlsDemoPanel.class);
	private static final long serialVersionUID = 1L;
	public static final IconResource TEST_ICON = new IconResource("test.png",
			"image/png");
	public static final IconResource HOVER_TEST_ICON = new IconResource(
			"hover_test.png", "image/png");

	public ControlsDemoPanel()
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
				"portal/demo/controls/ControlsDemoPanel.ftl"));

		_textInput = new TextInput();
		_textInput.getModel().setValue("Edit me");
		add(_textInput, "TextInput");

		_label = new Label();
		_label.getModel().setValue("This is a label.");
		add(_label, "Label");

		TextArea textArea = new TextArea();
		textArea.getModel().setValue("Edit me");
		add(textArea, "TextArea");

		CheckBox checkBox = new CheckBox();
		checkBox.setCaption("Select me!");
		add(checkBox, "CheckBox");

		Button button = new Button();
		button.setCaption("Click me.");
		button.setAction(new IAction()
		{
			private static final long serialVersionUID = 1L;

			public void doAction()
			{
				_label.getModel().setValue(
						_textInput.getModel().getValue());
			}
		});
		add(button, "Button");

		Image image = new Image(TEST_ICON, HOVER_TEST_ICON);
		add(image, "Image");
	}
	private TextInput _textInput;
	private Label _label;
}
