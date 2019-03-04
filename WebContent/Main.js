function handleLoginResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    console.log("jump to index");
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "index.html?id="+resultDataJson["title"]+
        "&year="+resultDataJson["year"] +
        "&director="+resultDataJson["director"]+
        "&star="+resultDataJson["star"]+
        "&page="+"1"+
        "&number="+"20"+
        "&sort="+"a.rating desc"+
        "&genres="+""+
        "&letters="+""
    );

}


function submitLoginForm(formSubmitEvent) {
    console.log("search_form");
    formSubmitEvent.preventDefault();

    $.post(
        "api/Main",
        // Serialize the login form to the data sent by POST request
        $("#search_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString)
);
}

// Bind the submit action of the form to a handler function
$("#search_form").submit((event) => submitLoginForm(event));


/////////////////////////---------------------------------------------------///////////////////////////////////////////

function handleLookup(moviename, doneCallback) {
    if(moviename.length>=3) {
        console.log("autocomplete initiated")
        console.log("sending AJAX request to backend Java Servlet")
        console.log(temp_data)


        // TODO: if you want to check past query results first, you can do it here
        if (temp_query.indexOf(moviename) == -1||temp_data=="") {
            // sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
            // with the query data

            console.log("no cache matched, getting data from database")
            jQuery.ajax({
                "method": "GET",
                "url": "api/Main?moviename=" + escape(moviename),
                "success": function (data) {
                    handleLookupAjaxSuccess(data, moviename, doneCallback)
                },
                "error": function (errorData) {
                    console.log("lookup ajax error")
                    console.log(errorData)
                }
            })


        }
        else {

            console.log("found matched past result, using the cache data: ")
            console.log(temp_data)

            doneCallback({suggestions: temp_data});
        }
    }
}


var temp_data="";
var temp_query="";

function handleLookupAjaxSuccess(data, moviename, doneCallback) {
    console.log("lookup ajax successful")
    // parse the string into JSON
    var jsonData = JSON.parse(data);
    console.log(jsonData)
    if(jsonData.length!=0) temp_data=jsonData;
    else temp_data="";

    temp_query=moviename;
    doneCallback( { suggestions: jsonData } );

}



function handleSelectSuggestion(suggestion) {
    console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["movie_id"])
    window.location.replace("single-movie.html?id="+suggestion["data"]["movie_id"]);

}



$('#movie_name_auto').autocomplete({
    // documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (moviename, doneCallback) {
        handleLookup(moviename, doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,

});

/*$('#autocomplete').keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {

    }
})*/

//////////////////////////////////////////---------------------//////////////////////////////////

function handlebrowsing_gen(resultDataString) {

    resultDataJson = JSON.parse(resultDataString);
    console.log(resultDataJson["genres"]);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "index.html?id="+""+
        "&year="+"" +
        "&director="+""+
        "&star="+""+
        "&page="+"1"+
        "&number="+"20"+
        "&sort="+"a.rating desc"+
        "&genres="+resultDataJson["genres"]+
        "&letters="+""
    );

}



function handlegenre(formSubmitEvent) {
    console.log("submit cart form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.post(
        "api/Main",
        // Serialize the cart form to the data sent by POST request
        $("#browsing_genres").serialize(),
        (resultDataString) => handlebrowsing_gen(resultDataString)
);
}

$("#browsing_genres").submit((event) => handlegenre(event));




function handlebrowsing_let(resultDataString) {

    resultDataJson = JSON.parse(resultDataString);
    console.log(resultDataJson["letters"]);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "index.html?id="+""+
        "&year="+"" +
        "&director="+""+
        "&star="+""+
        "&page="+"1"+
        "&number="+"20"+
        "&sort="+"a.rating desc"+
        "&genres="+""+
        "&letters="+resultDataJson["letters"]
    );

}



function handleletter (formSubmitEvent) {
    console.log("submit cart form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.post(
        "api/Main",
        // Serialize the cart form to the data sent by POST request
        $("#browsing_letter").serialize(),
        (resultDataString) => handlebrowsing_let(resultDataString)
);
}

$("#browsing_letter").submit((event) => handleletter(event));




function handlegResult(resultData) {
    let movieTableBodyElement = jQuery("#gnamelist");
    //resultDataJson = JSON.parse(resultData);
    let y=resultData["gname"].split(",").length;
    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < y; i++) {
        let rowHTML = "";
        rowHTML += "<option value="+resultData["gname"].split(",")[i]+">"
            +resultData["gname"].split(",")[i]+"</option>";
        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}



// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "post",// Setting request method
    url: "api/Main",
    success: (resultData) => handlegResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});