<p><b>Text Input</b></p>

<div>
${context.includeChild("TextInput")}
</div>

<p><b>Label</b></p>

<div>
${context.includeChild("Label")}
</div>

<p><b>Test Button</b></p>

<div>
${context.includeChild("Button")}
</div>

<p><b>Text Area</b></p>

<div>
${context.includeChild("TextArea")}
</div>

<p><b>Check Box</b></p>

<div>
${context.includeChild("CheckBox")}
</div>

<p><b>Image Sample (IconResource)</b></p>

<div>
${context.includeChild("Image")}
</div>

<p><b>Image Sample (Direct resource)</b></p>

<div>
<img src="${context.getResourceURL("test.png")}"/>
</div>

<p><b>Image Sample (Download resource)</b></p>

<div>
<a href="${context.getDownloadURL("test.png", "test.png")}">Download image</a>
</div>