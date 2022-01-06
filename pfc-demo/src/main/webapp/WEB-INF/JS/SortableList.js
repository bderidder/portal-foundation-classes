function initSortableList(listId)
{
	var dropTargetName = "DropTarget" + listId;
	var dl = document.getElementById(listId);

	new dojo.dnd.HtmlDropTarget(dl, [dropTargetName]);
	var lis = getChildrenByTagName(dl, "div");
		
	for(var x=0; x<lis.length; x++)
	{
		new dojo.dnd.HtmlDragSource(lis[x], dropTargetName);
	}
}

function copySortableList(listId)
{
	var indexList = "";

	var dl = document.getElementById(listId);
	var lis = getChildrenByTagName(dl, "div");

	for(var x=0; x<lis.length; x++)
	{
		indexList = indexList + lis[x].getAttribute("pfc:SortableListIndex") + ",";
	}

	var hiddenField = document.getElementById("Hidden" + listId);
	hiddenField.setAttribute("value", indexList);
}

function getChildrenByTagName(parentElement, tagName)
{
	var nodeList = new Array();

	var searchChildren = parentElement.childNodes;
	
	for (var i = 0; i < searchChildren.length; i++)
	{
		var childNode = searchChildren[i];

		if (childNode.tagName == tagName.toUpperCase())
		{
			nodeList.push(childNode);
		}
	}

	return nodeList;
}