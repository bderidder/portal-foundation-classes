<table class="WizardTable">

<tr>
    <td class="WizardTitleCell">
    	<p class="WizardTitleLabel">${context.includeChild("WizardPageLabel")}</p></td>
</tr>

<tr>
    <td>
    	${context.includeChild("WizardPage")}
    </td>
</tr>

<tr>
    <td class="WizardPageCell" align="right">
		<hr class="WizardButtonSeparator"/>
		<table class="WizardButtonTable">
	    <tr>
    	    <td>${context.includeChild("CancelButton")}</td>
			<td>${context.includeChild("PreviousButton")}</td>
			<td>${context.includeChild("NextButton")}</td>
			<td>${context.includeChild("FinishButton")}</td>
	    </tr>
		</table>
    </td>
</tr>

</table>
