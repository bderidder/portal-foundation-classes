<table width="100%" cellspacing="0" cellpadding="0">
<tr>
	<td style="border-top: 1px solid black;">

<table width="100%" cellspacing="0" cellpadding="0">
<tr>
	<td valign="top" width="175">
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
			<td><img src="${context.getResourceURL("portal/demo/choose.gif")}"/></td>
		</tr>
		<tr>
			<td>
				${context.includeChild("DemoChooser")}
			</td>
		</tr>
		</table>
	</td>
	<td valign="top">
		<div style="padding: 15px;">
			${context.includeChild("DemoEntries")}
		</div>
	</td>
</tr>
</table>

	</td>
</tr>
</table>