<table width="100%" border="0" cellspacing="10" cellpadding="0" valign="top">
<tr>
	<td>
		<table cellspacing="10">
		<tr>
			<td valign="middle"><h1>${context.includeChild("Title")}</h1></td>
		</tr>
		</table>
	</td>
</tr>
<tr>
	<td valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
<#if $context.getComponent().getFrame().hasDialog() >
	<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" class="tablebordergray">
<#else>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" class="tablebordergray">
</#if>
				<tr>
					<td valign="top">
						${context.includeChild("Frame")}
					</td>
				</tr>
				</table>
			</td>
		</tr>
		</table>
	</td>
</tr>
</table>

