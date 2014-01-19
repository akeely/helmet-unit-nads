/**
 * Manipulate Literally Canvas images
 */

/**
 * Upload the image to the server
 */
function uploadImage(gameId) {
	var canvas = $('#newImage')[0]
	var imageData = canvas.toDataURL();
	
	uploadData(gameId,"imgdata",imageData)
}

function uploadSentence(gameId) {
	var sentence = $('#sentence')[0].value
	
	uploadData(gameId,"txt",sentence)
}

function uploadData(gameId,entryType,data) {
	
	$.post("/api/game/" + gameId + "/entry?entryType="+entryType, data, function() {window.location.reload()})
	.fail(function(jqXHR, textStatus, errorThrown) {
	  if (textStatus == 'timeout')
		console.log('The server is not responding');
	    alert("The server is down. Please try again later.");
	  if (textStatus == 'error')
		console.log(jqXHR.responseText);
        alert("Upload failed, please try again.");
	});
}