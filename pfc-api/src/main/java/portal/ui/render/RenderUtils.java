package portal.ui.render;

import java.io.IOException;
import java.io.Writer;

public final class RenderUtils
{
    public static void addAttribute(String name, String value,
            StringBuffer buffer)
    {
        if (value != null)
        {
            buffer.append(" " + name + "=\"" + value + "\"");
        }
    }

    public static void addAttribute(String name, String value,
            Writer pWriter) throws IOException
    {
        if (value != null)
        {
            pWriter.write(" " + name + "=\"" + value + "\"");
        }
    }

    private RenderUtils()
    {
    }
}
