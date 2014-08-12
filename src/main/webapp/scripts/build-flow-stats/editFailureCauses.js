var failureCauseNumber = 1;
var failureCauseNameNumber = 1;
var failureCauseDescriptionNumber = 1;
var failureCausePatternNumber = [];

function addFailureCause(id) {
	var divId = "failure_" + failureCauseNumber;
	var inputsId = "inputs_" + failureCauseNumber;
	var buttonsId = "buttons_" + failureCauseNumber;
	addFailureCauseDiv(id);
	addInputsDiv(divId);
	addButtonsDiv(divId);
	
	addName(inputsId);
	addDescription(inputsId);
	failureCausePatternNumber[failureCauseNumber - 1] = 1;
	addPattern(inputsId);

	addAddPatternButton(buttonsId, inputsId);
	addDeleteButton(buttonsId, divId);

	document.getElementById(divId).innerHTML += "<br/>";
	failureCauseNumber += 1;
}

function addFailureCauseDiv(id) {
	var div = document.createElement('div');
	div.setAttribute('id', 'failure_' + failureCauseNumber);
	var button = document.getElementById('addFailureCauseButton');
	document.getElementById(id).insertBefore(div, button);
}

function addInputsDiv(id) {
	var div = document.createElement('div');
	div.setAttribute('id', 'inputs_' + failureCauseNumber);
	document.getElementById(id).appendChild(div);
}

function addButtonsDiv(id) {
	var div = document.createElement('div');
	div.setAttribute('id', 'buttons_' + failureCauseNumber);
	document.getElementById(id).appendChild(div);
}

function addName(id) {
	var label = document.createElement('label');
	label.innerHTML = "Name: ";
	var textField = document.createElement('input');
	textField.setAttribute('id', 'name_' + failureCauseNameNumber);
	textField.setAttribute('type', 'text');
	textField.setAttribute('size', '70');
	textField.setAttribute('name', 'name_' + failureCauseNameNumber);
	failureCauseNameNumber += 1;
	document.getElementById(id).appendChild(label);
	document.getElementById(id).appendChild(textField);
}

function addDescription(id) {
	var label = document.createElement('label');
	label.innerHTML = "Description: ";
	var textField = document.createElement('input');
	textField.setAttribute('id', 'description_' + failureCauseDescriptionNumber);
	textField.setAttribute('type', 'text');
	textField.setAttribute('size', '66');
	textField.setAttribute('name', 'description_' + failureCauseDescriptionNumber);
	document.getElementById(id).innerHTML += "<br/>";
	document.getElementById(id).appendChild(label);
	document.getElementById(id).appendChild(textField);
	failureCauseDescriptionNumber += 1;
}

function addPattern(id, addButton) {
	var failureNumber = parseInt(id.replace(/.*_/, ""));
	var patternId = 'pattern_' + failureNumber + '_' + failureCausePatternNumber[failureNumber - 1];
	var patternDivId = 'patternDiv_' + failureNumber + '_' + failureCausePatternNumber[failureNumber - 1];
	var div = document.createElement('div');
	div.setAttribute('id', patternDivId);
	var label = document.createElement('label');
	label.innerHTML = "Pattern: ";
	var textField = document.createElement('input');
	textField.setAttribute('id', patternId);
	textField.setAttribute('type', 'text');
	textField.setAttribute('size', '69');
	textField.setAttribute('name', 'patterns_' + failureNumber);
	div.appendChild(label);
	div.appendChild(textField);
	document.getElementById(id).appendChild(div);
	if (addButton) {
		addPatternDeleteButton(patternDivId);
	}
	failureCausePatternNumber[failureNumber - 1] += 1;
}

function addAddPatternButton(id, toAppend) {
	var button = document.createElement('button');
	button.setAttribute('type', 'button');
	button.setAttribute('onClick', 'addPattern(\'' + toAppend + '\', \'yes\')');
	button.innerHTML = "Add Pattern";
	document.getElementById(id).appendChild(button);
}

function addDeleteButton(id, toDelete) {
	var button = document.createElement('button');
	button.setAttribute('type', 'button');
	button.setAttribute('onClick', 'deleteInnerHTML(\'' + toDelete + '\')');
	button.innerHTML = "Delete Failure Cause";
	document.getElementById(id).appendChild(button);
}

function addPatternDeleteButton(id) {
	var button = document.createElement('button');
	button.setAttribute('type', 'button');
	button.setAttribute('onClick', 'deleteInnerHTML(\'' + id + '\')');
	button.innerHTML = "Delete Pattern";
	document.getElementById(id).appendChild(button);
}

function setValue(id, value) {
	document.getElementById(id).setAttribute("value", value);
}

function setPatternValues(patterns, loopIndex) {
	document.getElementById("pattern_" + loopIndex + "_" + 1).setAttribute("value", patterns[0]);
	if (patterns.length > 1) {
		for (i = 1; i < patterns.length; i++) {
			addPattern("inputs_" + loopIndex, "yes");
			document.getElementById("pattern_" + loopIndex + "_" + (i + 1)).setAttribute("value", patterns[i]);
		}
	}
}

function deleteInnerHTML(id) { 
	document.getElementById(id).innerHTML = "";
}

