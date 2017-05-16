/**
 * Created by Thomas on 10/05/2017.
 */
//Function to set HTML element with certain id to the given index in the URL (split on "/") and return this value
function getURL(index, id) {
    var currentLocation = new String(window.location);
    var paths = currentLocation.split("/");
    document.getElementById(id).innerHTML = paths[index];
    return paths[index];
}

//Set correct URL on bot creation
function setAction() {
    var action = document.getElementById("type").value;
    if (action !== "") {
        action = action.toLowerCase();
        var url = "/workers/0/bots/create/" + action;
        document.getElementById("newBotForm").action = url;
        document.getElementById("newBotForm").submit();
    } else {
        alert("Please set form action");
    }
}

//Set correct URL on bot edit
function setEditAction(botId) {
    var property = document.getElementById("botProperty" + botId).value;
    var value = document.getElementById("propertyValue" + botId).value;
    if (value !== "") {
        property = property.toLowerCase();
        var url = '/workers/0/bots/set/' + botId + '/' + property + '/' + value;
        document.getElementById("editBotForm" + botId).action = url;
        document.getElementById("editBotForm" + botId).submit();
    } else {
        alert("Please fill in the property value");
    }
}