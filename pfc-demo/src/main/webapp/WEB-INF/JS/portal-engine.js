var pfc = {};

pfc.submitListeners = [];

pfc.addOnSubmit = function(obj, fcnName)
{
	if (arguments.length == 1)
	{
		pfc.submitListeners.push(obj);
	}
	else if (arguments.length > 1)
	{
		pfc.submitListeners.push(
			function()
			{
				obj[fcnName]();
			}
		);
	}
}

pfc.refreshPage = function()
{
	pfc.submitForm();
}

pfc.pfcOnload = function()
{
	pfc.autoSubmitPage(500);
}

pfc.autoSubmitPage = function(timeout)
{
	milliseconds = timeout * 1000;
	setTimeout('submitForm()', milliseconds);
}

pfc.submitFormWithAction = function(actionId)
{
	actionElem = document.getElementById('HiddenAction');
	actionElem.setAttribute('name', actionId, 0);

	return pfc.submitForm();
}

pfc.callSubmitListeners = function()
{
	for (var x = 0; x < pfc.submitListeners.length; x++)
	{
		try
		{	
			pfc.submitListeners[x]();
		}
		catch (e)
		{
		}
	}
}

pfc.submitForm = function()
{
	pfc.callSubmitListeners();

	portalForm = document.getElementById('PortalForm');
	portalForm.submit();
	
	setTimeout('pfc.initiateSubmitWait()', 1500);

	return false;
}

pfc.initiateSubmitWait = function()
{
	waitDiv = document.getElementById('WaitPage');
	normalDiv = document.getElementById('NormalPage');

	waitDiv.style.visibility = "visible";
	waitDiv.style.display = "block";

	normalDiv.style.visibility = "hidden";
	normalDiv.style.display = "none";
}

pfc.switchImage = function(imgId, imageUrl)
{
	document.getElementById(imgId).setAttribute('src',imageUrl);
}