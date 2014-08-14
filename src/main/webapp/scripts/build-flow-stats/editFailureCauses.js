/**
 * Javascript for handling the different events when editing the failure 
 * causes used for data analysis. I'm no javascript expert (more like a 
 * total noob) so the script is probably not very well written and 
 * more camplex than it should. And it would be good to use jQuery
 * for this, but unfortunately it is not available in jenkins
 * per default.
 * @author  Kim Torberntsson
 */

/**
 * The following variables are used for giving the different 
 * elements unique id:s.
 */

/**
 * keeps track of the number of failure causes that have been created.
 * @type {Number}
 */
var failureCauseNumber = 1;

/**
 * keeps track of the number of failure cause names that have been created.
 * @type {Number}
 */
var failureCauseNameNumber = 1;

/**
 * keeps track of the number of failure cause descriptions that have been created.
 * @type {Number}
 */
var failureCauseDescriptionNumber = 1;

/**
 * keeps track of the number of patterns that have been created for each 
 * failure cause. Each position in the array corresponds to a failure cause.
 * @type {Array}
 */
var failureCausePatternNumber = [];

/**
 * Adds a failure cause to the element with the id defined by the 
 * parameter id. The method creates a new failure cause element 
 * by calling a set of subfunctions.
 * @param {String} id the id of the element
 */
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

/**
 * Adds a failure cause div to the element with the id defined by the 
 * parameter id.
 * @param {String} id the id of the element
 */
function addFailureCauseDiv(id) {
	var div = document.createElement('div');
	div.setAttribute('id', 'failure_' + failureCauseNumber);
	var button = document.getElementById('addFailureCauseButton');
	document.getElementById(id).insertBefore(div, button);
}

/**
 * Adds a input div to the element with the id defined by the 
 * parameter id.
 * @param {String} id the id of the element
 */
function addInputsDiv(id) {
	var div = document.createElement('div');
	div.setAttribute('id', 'inputs_' + failureCauseNumber);
	document.getElementById(id).appendChild(div);
}

/**
 * Adds a buttons div to the element with the id defined by the 
 * parameter id.
 * @param {String} id the id of the element
 */
function addButtonsDiv(id) {
	var div = document.createElement('div');
	div.setAttribute('id', 'buttons_' + failureCauseNumber);
	document.getElementById(id).appendChild(div);
}

/**
 * Adds a name to the element with the id defined by the 
 * parameter id.
 * @param {String} id the id of the element
 */
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

/**
 * Adds a description to the element with the id defined by the 
 * parameter id.
 * @param {String} id the id of the element
 */
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

/**
 * Adds a pattern to the element with the id defined by the 
 * parameter id.
 * @param {String} id the id of the element
 */
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

/**
 * Adds a button that can add additional patterns to the element with the id 
 * defined by the parameter id.
 * @param {String} id the id of the element
 * @param {String} toAppend id of the element to where the new patterns should be added when the button is pressed
 */
function addAddPatternButton(id, toAppend) {
	var button = document.createElement('button');
	button.setAttribute('type', 'button');
	button.setAttribute('onClick', 'addPattern(\'' + toAppend + '\', \'yes\')');
	button.innerHTML = "Add Pattern";
	document.getElementById(id).appendChild(button);
}

/**
 * Adds a button that can delete the failure cause with the id defined 
 * by the parameter id.
 * @param {String} id the id of the element
 * @param {String} toDelete id of the element that should be deleted when the button is pressed
 */
function addDeleteButton(id, toDelete) {
	var button = document.createElement('button');
	button.setAttribute('type', 'button');
	button.setAttribute('onClick', 'deleteInnerHTML(\'' + toDelete + '\')');
	button.innerHTML = "Delete Failure Cause";
	document.getElementById(id).appendChild(button);
}

/**
 * Adds a button that can delete the pattern with the id defined 
 * by the parameter id.
 * @param {String} id the id of the pattern
 */
function addPatternDeleteButton(id) {
	var button = document.createElement('button');
	button.setAttribute('type', 'button');
	button.setAttribute('onClick', 'deleteInnerHTML(\'' + id + '\')');
	button.innerHTML = "Delete Pattern";
	document.getElementById(id).appendChild(button);
}
/**
 * Sets the value of the element defined by the id parameter to the value
 * of the value parameter
 * @param {String} id the id of the element
 * @param {String} value the value that should be set to the element
 */
function setValue(id, value) {
	document.getElementById(id).setAttribute("value", value);
}

/**
 * Sets the value of the patterns for a failure cause according to the 
 * parameter patterns. This method gets called by the jelly-file when 
 * reading data from the XML-file.
 * @param {Array} patterns the patterns that should be added
 * @param {int} loopIndex the loop that the jelly-file is in. decides which id:s that should be used.
 */
function setPatternValues(patterns, loopIndex) {
	document.getElementById("pattern_" + loopIndex + "_" + 1).setAttribute("value", patterns[0]);
	if (patterns.length > 1) {
		for (i = 1; i < patterns.length; i++) {
			addPattern("inputs_" + loopIndex, "yes");
			document.getElementById("pattern_" + loopIndex + "_" + (i + 1)).setAttribute("value", patterns[i]);
		}
	}
}

/**
 * Deletes the innerHTML for the element defined by the parameter id.
 * @param  {String} id id of the element
 */
function deleteInnerHTML(id) { 
	document.getElementById(id).innerHTML = "";
}

/**
 * Walks through all failure causes that have not been deleted by the user and 
 * checks if there are any empty fields. If there are, a pop-up window is brought 
 * up and no data is sent.
 * @return {boolean} whether the form is valid or not
 */
function validateForm() {
	var valid = true;
	for (i = 1; i < failureCauseNumber; i++) {
		if (document.getElementById("name_" + i) != null) {
			if (document.getElementById("name_" + i).value === "") {
				return  alertUnvalidForm();
			}
			if (document.getElementById("description_" + i).value === "") {
				return  alertUnvalidForm();
			}
			for (k = 1; k < failureCausePatternNumber[i - 1]; k++) {
				if (document.getElementById("pattern_" + i + "_" + k).value === "") {
					return  alertUnvalidForm();
				}
			}
		}
	}
}

/**
 * Brings up a popup-window if any field are empty
 * @return {boolean} false because of invalid form
 */
function alertUnvalidForm() {
	alert("There are empty fields. Please fill them before submitting data.");
	return false;
}