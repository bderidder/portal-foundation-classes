<html>

<head>
	<title>${device.getTitle()}</title>

	<link rel="stylesheet" href="css" type="text/css" media="all">

	<script type="text/javascript" src="js"></script>

	<meta name="generator" content="Portal Foundation Classes" />

	${device.includeHeader()}

</head>

<body>

<div id="WaitPage" style="position: absolute; top: 120px;
                          width: 100%; height: 100%; visibility: hidden; display: none;">
<center>
	<table cellspacing="20" style="border: 1px solid black; padding: 5px; width: 300px; background-color: #E5E5E5">
	<tr>
		<td>
			<p>Please wait for your request to be processed by the application.</p>
		</td>
	</tr>
	<tr>
		<td align="center">
			<img src="progress.gif"/>
		</td>
	</tr>
	</table>
</center>

</div>

<div id="NormalPage" style="width: 100%; height: 100%; visibility: visible;">

	<form id="PortalForm" method="POST" action="PortalProcess" enctype="multipart/form-data">

		<input type="hidden" id="ieHasBugsHeader" name="ieHasBugsHeader" value="true"/>
		<input type="hidden" id="HiddenAction" name="NoAction" value="Dummy"/>

		${device.includeBody()}

		<input type="hidden" id="ieHasBugsFooter" name="ieHasBugsFooter" value="true"/>

	</form>

</div>

</body>

</html>