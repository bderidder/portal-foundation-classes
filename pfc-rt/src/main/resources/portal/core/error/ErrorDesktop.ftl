<script>

function toggleErrorReportDetails()
{
	var errorReportDetail = document.getElementById('ErrorReportDetails');
	var errorReportDetailToggle = document.getElementById('ErrorReportDetailsToggle')

	var currentDisplay = errorReportDetail.style.display;

	if (currentDisplay != 'none')
	{
		errorReportDetail.style.display = 'none';
		errorReportDetailToggle.innerText = 'Show report detail';
	}
	else
	{
		errorReportDetail.style.display = 'inline';
		errorReportDetailToggle.innerText = 'Hide report detail';
	}
}

</script>

<table cellspacing="15" align="center" width="60%">
<tr>
	<td rowspan="3" valign="top">
		${context.includeChild("CrashIcon")}
	</td>

	<td valign="middle" style="border-bottom: 1px solid black;">
		<p><b>The application has encountered a problem and needs to close. We are sorry for the inconvenience.</b></p>
	</td>
</tr>
<tr>
	<td valign="middle">
		<p><b>Help us solve the problem!</b></p>
		<p>An error report has been created and will be send to us.
		You can help us by providing some extra information that you might find useful in helping
		us track down the problem and improving our application.</p>
		<p><a id="ErrorReportDetailsToggle" href="javascript:toggleErrorReportDetails()">Error report details</a></p>
	</td>
</tr>
<tr>
	<td align="center">${context.includeChild("OkButton")}</td>
</tr>
<tr>
	<td align="left" colspan="2">
		<div id="ErrorReportDetails" style="display: none;">
		<pre>${context.includeChild("ThrowableTrace")}</pre>
		</div>
	</td>
</tr>
</table>

