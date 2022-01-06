<table cellspacing="1" cellpadding="1">
<tr>
    <td>Free:</td>
    <td>${context.includeChild("FreeMemory")}</td>
</tr>
<tr>
    <td>Available:</td>
    <td>${context.includeChild("TotalMemory")}</td>
</tr>
<tr>
    <td colspan="2" align="center">${context.includeChild("PercentageFree")}% FREE</td>
</tr>
</table>
