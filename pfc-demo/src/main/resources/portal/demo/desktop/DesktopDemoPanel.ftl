<table width="100%" height="100%">
<tr>
	<td valign="top">
		<table width="150" cellpadding="5">
		<tr>
			<td>${context.includeChild("AddButton")}</td>
		</tr>
		<tr>
			<td>
				<p>Open Windows:</p>
				${context.includeChild("WindowList")}
			</td>
		</tr>
		</table>
	</td>
	<td width="100%" valign="top">
		<div style="border: dashed 1px black;">
			${context.includeChild("Desktop")}
		</div>
	</td>
</tr>
</table>