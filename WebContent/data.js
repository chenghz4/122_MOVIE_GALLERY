/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


//function getParameterByName(target) {
//    // Get request URL
//    let url = window.location.href;
//    // Encode target parameter name to url encoding
//    target = target.replace(/[\[\]]/g, "\\$&");
//
//    // Ues regular expression to find matched parameter value
//    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
//        results = regex.exec(url);
//    if (!results) return null;
//    if (!results[2]) return '';
//    console.log(decodeURIComponent(results[2].replace(/\+/g, " ")));
//    // Return the decoded parameter value
//    return decodeURIComponent(results[2].replace(/\+/g, " "));
//}




function handleStarResult(resultData) {
    
    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#star_table_body");
    let tablename = jQuery("#tablename");
    
    let rowHTML = "";
//    for(let j=0; j<resultData.length;j++) {
//    	rowHTML = "";
//    	rowHTML += "Table name:" +resultData[j]["tablename"];
//    	starTableBodyElement.append(rowHTML);
//    
//    
    let a;
    let x = "";
    for (let i = 0; i <resultData.length; i++) {
    	
    	a = resultData[i]["tablename"];
    	
    	rowHTML = "";
    	rowHTML += "<tr>";
    	if(a != x) {
    		
    		
    		rowHTML = "";
    		
    		//rowHTML += "<tr>";
    		rowHTML += "<br>";
    		//rowHTML += "<br>";
    		rowHTML += "<p><b>Table Name: *****  "+resultData[i]["tablename"].split(" ")[1].toUpperCase()+"  *****<b></p>";
    		starTableBodyElement.append(rowHTML);
    		//rowHTML += "<tr>";
    		//rowHTML += "<table>";
//        	rowHTML += "<tr>";
//        	rowHTML += "<th>Field</th>";
//        	rowHTML += "<th>Type</th>";
//        	rowHTML += "<th>Null</th>";
//        	rowHTML += "<th>Key</th>";
//        	rowHTML += "<th>Default</th>";
//        	rowHTML += "</tr>";
        	
        	x = resultData[i]["tablename"];
    	}
    	rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["field"] + "</th>";
        rowHTML += "<th>" + resultData[i]["type"] + "</th>";
        rowHTML += "<th>" + resultData[i]["isnull"] + "</th>";
        rowHTML += "<th>" + resultData[i]["key"] + "</th>";
        rowHTML += "<th>" + resultData[i]["isdefault"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);


    }
    

    let space="";
    space +="<tr>";
    space +="<th>";
    space +="</th>";
    space +="</tr>";
    starTableBodyElement.append(space);
    let goback = "";


    goback +="<tr>";
    goback +="<th>";
    goback +="</th>";
    goback +="<th>";
    goback +="</th>";
    goback +="<th>";
    goback +="</th>";
    goback +="<th>";
    goback +="</th>";
    goback +="<th>";
    goback +="</th>";
    goback +="<th>";
    goback +="</th>";
    goback +=
        "<th style='color: crimson'>" +
        // Add a link to single-star.html with id passed with GET url parameter
        '<a href="dashboard.html" >'
        + "Go Back to Dashboard" +     
        '</a>' +
        "</th>";
    goback += "</tr>";
    starTableBodyElement.append(goback);



}












/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/Meta",
    // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});