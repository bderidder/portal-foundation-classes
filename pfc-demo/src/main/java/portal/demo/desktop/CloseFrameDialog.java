package portal.demo.desktop;

import portal.ui.IAction;
import portal.ui.comp.Button;
import portal.ui.comp.Label;
import portal.ui.desktop.Dialog;
import portal.ui.desktop.Frame;
import portal.ui.render.FreeMarkerLayout;

public class CloseFrameDialog extends Dialog
{
	//private static final Log LOGGER = LogFactory.getLog(CloseFrameDialog.class);
	private static final long serialVersionUID = 1L;

	public CloseFrameDialog(Frame frame)
	{
		this._frame = frame;

		setTitle("Close Frame");

		createUI();
	}

	public void beforeApply()
	{
	}

	public void destroy()
	{
	}

	private void createUI()
	{
		try
		{
			getContentPanel().setLayout(
					new FreeMarkerLayout(
					"portal/demo/desktop/CloseFrameDialog.ftl"));

			createButtons();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void createButtons()
	{
		_frameName = new Label();
		_frameName.getModel().setValue(_frame.getTitle());

		_okButton = new Button();
		_okButton.setCaption("Yes");
		_okButton.setAction(new OkAction());

		_cancelButton = new Button();
		_cancelButton.setCaption("No");
		_cancelButton.setAction(new CancelAction());

		getContentPanel().add(_frameName, "FrameName");
		getContentPanel().add(_okButton, "OkButton");
		getContentPanel().add(_cancelButton, "CancelButton");
	}

	private class OkAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		public void doAction()
		{
			_frame.close();

			close();
		}
	}

	private class CancelAction implements IAction
	{
		private static final long serialVersionUID = 1L;

		public void doAction()
		{
			close();
		}
	}
	private Frame _frame;
	private Label _frameName;
	private Button _okButton;
	private Button _cancelButton;
}
