package portal.demo.desktop;

import portal.ui.IAction;
import portal.ui.comp.Button;
import portal.ui.desktop.Frame;
import portal.ui.render.FreeMarkerLayout;

public class DemoFrame extends Frame
{
	private static final long serialVersionUID = 1L;

	public DemoFrame()
	{
		createCloseButton();
	}

	private void createCloseButton()
	{
		getContentPanel().setLayout(
				new FreeMarkerLayout("portal/demo/desktop/DemoFrame.ftl"));

		Button closeButton = new Button();

		closeButton.setCaption("Close Me");
		closeButton.setAction(new IAction()
		{
			private static final long serialVersionUID = 1L;

			public void doAction()
			{
				askForClose();
			}
		});

		getContentPanel().add(closeButton, "CloseButton");
	}

	private void askForClose()
	{
		CloseFrameDialog closeFrameDialog = new CloseFrameDialog(this);

		closeFrameDialog.setParent(this);
		closeFrameDialog.show();
	}
}
