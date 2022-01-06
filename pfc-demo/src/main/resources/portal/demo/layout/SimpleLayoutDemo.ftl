<p>Each box implements the same functionality but uses a different way of rendering the contents.
The FreeMarker Demo Panel uses a FreeMarker template to layout the components in html. The Layout Demo Panel uses
layout managers to layout the components.</p>

<table cellspacing="6">
<tr>
	<td valign="top"><p class="DemoPanelName">FreeMarker Demo Panel</></td>
	<td valign="top"><p class="DemoPanelName">Layout Demo Panel</></td>
</tr>
<tr>
	<td valign="top" class="DemoPanelContent">${context.includeChild("FreeMarkerDemoPanel")}</td>
	<td valign="top" class="DemoPanelContent">${context.includeChild("LayoutDemoPanel")}</td>
</tr>
</table>