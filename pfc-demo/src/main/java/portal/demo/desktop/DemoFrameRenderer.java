package portal.demo.desktop;

import java.io.IOException;

import portal.ui.comp.Label;
import portal.ui.comp.Panel;
import portal.ui.desktop.Frame;
import portal.ui.desktop.FrameRenderer;
import portal.ui.render.FreeMarkerLayout;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class DemoFrameRenderer implements FrameRenderer
{
	private static final long serialVersionUID = 1L;

	public void renderFrame(IRenderContext pRenderContext, Frame frame)
			throws RenderException, IOException
	{
		Panel panel = new Panel();

		Label frameTitle = new Label();
		frameTitle.getModel().setValue(frame.getTitle());

		panel.add(frameTitle, "Title");
		panel.add(frame, "Frame");

		if (frame.hasDialog())
		{
			panel.setLayout(new FreeMarkerLayout(
					"portal/demo/desktop/DialogDecorator.ftl"));
		}
		else
		{
			panel.setLayout(new FreeMarkerLayout(
					"portal/demo/desktop/FrameDecorator.ftl"));
		}

		pRenderContext.includeComponent(panel);

	}
}
