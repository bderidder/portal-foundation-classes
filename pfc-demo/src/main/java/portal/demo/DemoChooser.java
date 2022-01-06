package portal.demo;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import portal.eventbus.EventBus;
import portal.model.IModel;
import portal.ui.Component;
import portal.ui.IAction;
import portal.ui.render.IRenderContext;
import portal.ui.render.RenderException;

public class DemoChooser extends Component<IModel>
{
    private static final long serialVersionUID = 1L;

    public DemoChooser(EventBus eventBus, Collection<DemoEntryInfo> demoInfoEntries)
    {
        this._eventBus = eventBus;

        initChooser(demoInfoEntries);
    }

    private void initChooser(Collection<DemoEntryInfo> demoInfoEntries)
    {
        _demoChooserEntries = new ArrayList<DemoChooserEntry>();

        Iterator<DemoEntryInfo> it = demoInfoEntries.iterator();

        while (it.hasNext())
        {
            DemoEntryInfo demoEntryInfo = it.next();

            DemoChooserEntry chooseEntry = new DemoChooserEntry(demoEntryInfo);

            _demoChooserEntries.add(chooseEntry);
        }
    }

    // this methods looks like an ideal candidate to turn into a reusable
    // component !
    public void draw(IRenderContext pRenderContext) throws RenderException
    {
        try
        {
            Writer writer = pRenderContext.getWriter();

            Iterator<DemoChooserEntry> it = _demoChooserEntries.iterator();

            int currentRow = 0;

            writer.write("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");

            while (it.hasNext())
            {
                DemoChooserEntry child = it.next();

                currentRow++;

                String normalCSSClass;

                if (currentRow % 2 == 0)
                {
                    normalCSSClass = "DemoChooserEvenRow";
                }
                else
                {
                    normalCSSClass = "DemoChooserOddRow";
                }

                writer.write("<tr>");
                writer.write("<td class=\"" + normalCSSClass + "\" ");
                writer.write("onmouseover=\"this.className = 'DemoChooserEntryHoover';\" ");
                writer.write("onmouseout=\"this.className = '" + normalCSSClass
                        + "';\">");

                //writer.write("<td>");

                pRenderContext.includeComponent(child);
                pRenderContext.getWriter().write("</td>");
                pRenderContext.getWriter().write("</tr>");
            }

            pRenderContext.getWriter().write("</table>");
        }
        catch (IOException e)
        {
            throw new RenderException("Could not render DemoChooser", e);
        }
    }
    private EventBus _eventBus;
    private List<DemoChooserEntry> _demoChooserEntries;

    private class DemoChooserEntry extends Component<IModel> implements IAction
    {
        private static final long serialVersionUID = 1L;

        public DemoChooserEntry(DemoEntryInfo demoEntryInfo)
        {
            this._demoEntryInfo = demoEntryInfo;
        }

        public void doAction()
        {
            StartDemoEvent event = new StartDemoEvent(this, _demoEntryInfo);

            _eventBus.postEvent(event);
        }

        public void draw(IRenderContext pRenderContext) throws RenderException
        {
            try
            {
                Writer writer = pRenderContext.getWriter();

                String myActionUrl = pRenderContext.createActionUrl(this);

                writer.write("<div onclick=\"" + myActionUrl + "\">");

                writer.write("<p class='DemoChooserShortName'>");
                writer.write(_demoEntryInfo.getShortName());
                writer.write("</p>");

                writer.write("<p class='DemoChooserDescription'>");
                writer.write(_demoEntryInfo.getDescription());
                writer.write("</p>");

                writer.write("</div>");
            }
            catch (IOException e)
            {
                throw new RenderException("Could not render DemoChooserEntry "
                        + _demoEntryInfo.getShortName(), e);
            }
        }
        private DemoEntryInfo _demoEntryInfo;
    }
}
