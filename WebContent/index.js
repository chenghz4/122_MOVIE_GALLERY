/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

let title = getParameterByName('id');
let year= getParameterByName('year');
let director= getParameterByName('director');
let star= getParameterByName('star');
let page=getParameterByName('page');
let number=getParameterByName('number');
let sort=getParameterByName('sort');
let genres=getParameterByName('genres');
let letters =getParameterByName('letters');
let number1=parseInt(number,10);
let page1=parseInt(page,10);
let offset=(page1-1)*number1.toString();

function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    console.log(decodeURIComponent(results[2].replace(/\+/g, " ")));
    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}



/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleStarResult(resultData) {
    console.log(offset);
    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#star_table_body");
    let url = window.location.href;

    for (let i = 0; i <resultData.length; i++) {
        console.log(i+1+offset);
        let a=(i+1+offset);
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + a + "</th>";
        rowHTML +=
            "<th>" +
        // Add a link to single-star.html with id passed with GET url parameter
        '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
        + resultData[i]["movie_title"] +     // display star_name for the link text
        '</a>' +
        "</th>";

        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";

       // for(let j=0; j < resultData[i]["list_g"].length;j++)
        //rowHTML += "<th>" + resultData[i]["list_g"] + "</th>";

        let y=resultData[i]["list_g"].split(",").length;
        rowHTML +="<th>";
        for (let u = 0; u < y-1; u++) {

                rowHTML += // Add a link to single-star.html with id passed with GET url parameter
                    '<a href="index.html?id='+title+'&year='+year+'&director='+director+'&star='+star+'&page='
                    +page+'&number='+number+'&sort='+sort+'&genres='+resultData[i]["list_g"].split(",")[u]+'&letters='
                    +letters+'">'
                    + resultData[i]["list_g"].split(",")[u] + ", "     // display star_name for the link text
            '</a>';

        }

            rowHTML += // Add a link to single-star.html with id passed with GET url parameter
                '<a href="index.html?id='+title+'&year='+year+'&director='+director+'&star='+star+'&page='
                +page+'&number='+number+'&sort='+sort+'&genres='+resultData[i]["list_g"].split(",")[y-1]+'&letters='
                +letters+'">'
                + resultData[i]["list_g"].split(",")[y-1] +""     // display star_name for the link text
        '</a>';
        rowHTML +="</th>";







        let n=resultData[i]["s.id"].split(",").length;
        rowHTML +="<th>";
        for (let j = 0; j < n-1; j++) {

            rowHTML += // Add a link to single-star.html with id passed with GET url parameter
                    '<a href="single-star.html?id=' + resultData[i]["s.id"].split(",")[j] + '">'
                    + resultData[i]["list_s"].split(",")[j] + ", "     // display star_name for the link text
                '</a>';

        }

        rowHTML += // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single-star.html?id=' + resultData[i]["s.id"].split(",")[n-1] + '">'
            + resultData[i]["list_s"].split(",")[n-1] + ""     // display star_name for the link text
        '</a>';

        rowHTML +="</th>";


        rowHTML += "<th>" + resultData[i]["rating"] + "</th>";


        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="cart.html?id=' + resultData[i]['movie_id'] + '">'
            +"Add to cart" +     // display star_name for the link text
            '</a>' +
            "</th>";


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
        '<a href="Main.html" >'
        + "Go Back to Search, Browsing Page" +     // display star_name for the link text
        '</a>' +
        "</th>";
    goback += "</tr>";
    starTableBodyElement.append(goback);



}




function handlenumberResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    console.log("jump to index");
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "index.html?id="+title+
        "&year="+year +
        "&director="+director+
        "&star="+star+
        "&page="+page+
        "&number="+resultDataJson["number"]+
        "&sort="+sort+
        "&genres="+genres+
        "&letters="+letters


    );

}
function submitnumber(formSubmitEvent) {
    console.log("ss");
    formSubmitEvent.preventDefault();

    $.post(
        "api/stars",
        // Serialize the login form to the data sent by POST request
        $("#number").serialize(),
        (resultDataString) => handlenumberResult(resultDataString)
);
}
$("#number").submit((event) => submitnumber(event));



function handlenextResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    console.log(resultDataJson["page_n"]);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "index.html?id="+title+
        "&year="+year +
        "&director="+director+
        "&star="+star+
        "&page="+resultDataJson["page_n"]+
        "&number="+number+
        "&sort="+sort+
        "&genres="+genres+
        "&letters="+letters

    );

}
function submitnextForm(formSubmitEvent) {
    console.log("ss");
    formSubmitEvent.preventDefault();

    $.post(
        "api/stars",
        // Serialize the login form to the data sent by POST request
        $("#page_next").serialize(),
        (resultDataString) => handlenextResult(resultDataString)
);
}

// Bind the submit action of the form to a handler function
$("#page_next").submit((event) => submitnextForm(event));




function handlepreResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    console.log(resultDataJson["page_p"]);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "index.html?id="+title+
        "&year="+year +
        "&director="+director+
        "&star="+star+
        "&page="+resultDataJson["page_p"]+
        "&number="+number+
        "&sort="+sort+
        "&genres="+genres+
        "&letters="+letters
    );

}
function submitpreForm(formSubmitEvent) {
    console.log("ss");
    formSubmitEvent.preventDefault();

    $.post(
        "api/stars",
        // Serialize the login form to the data sent by POST request
        $("#page_prev").serialize(),
        (resultDataString) => handlepreResult(resultDataString)
);
}
$("#page_prev").submit((event) => submitpreForm(event));




function handlesort_rResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "index.html?id="+title+
        "&year="+year +
        "&director="+director+
        "&star="+star+
        "&page="+page+
        "&number="+number+
        "&sort="+resultDataJson["sort_r"]+
        "&genres="+genres+
        "&letters="+letters
    );

}
function submitsort_r(formSubmitEvent) {
    formSubmitEvent.preventDefault();

    $.post(
        "api/stars",
        // Serialize the login form to the data sent by POST request
        $("#sort_rate").serialize(),
        (resultDataString) => handlesort_rResult(resultDataString)
);
}
$("#sort_rate").submit((event) => submitsort_r(event));



function handlesort_tResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "index.html?id="+title+
        "&year="+year +
        "&director="+director+
        "&star="+star+
        "&page="+page+
        "&number="+number+
        "&sort="+resultDataJson["sort_t"]+
        "&genres="+genres+
        "&letters="+letters
    );

}
function submitsort_t(formSubmitEvent) {
    console.log("ss");
    formSubmitEvent.preventDefault();

    $.post(
        "api/stars",
        // Serialize the login form to the data sent by POST request
        $("#sort_title").serialize(),
        (resultDataString) => handlesort_tResult(resultDataString)
);
}
$("#sort_title").submit((event) => submitsort_t(event));



function handlesort_raResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "index.html?id="+title+
        "&year="+year +
        "&director="+director+
        "&star="+star+
        "&page="+page+
        "&number="+number+
        "&sort="+resultDataJson["sort_ra"]+
        "&genres="+genres+
        "&letters="+letters
    );

}
function submitsort_ra(formSubmitEvent) {
    formSubmitEvent.preventDefault();

    $.post(
        "api/stars",
        // Serialize the login form to the data sent by POST request
        $("#sort_rate_a").serialize(),
        (resultDataString) => handlesort_raResult(resultDataString)
);
}
$("#sort_rate_a").submit((event) => submitsort_ra(event));



function handlesort_taResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    // If login succeeds, it will redirect the user to index.html
    window.location.replace(
        "index.html?id="+title+
        "&year="+year +
        "&director="+director+
        "&star="+star+
        "&page="+page+
        "&number="+number+
        "&sort="+resultDataJson["sort_ta"]+
        "&genres="+genres+
        "&letters="+letters
    );

}
function submitsort_ta(formSubmitEvent) {
    console.log("ss");
    formSubmitEvent.preventDefault();

    $.post(
        "api/stars",
        // Serialize the login form to the data sent by POST request
        $("#sort_title_a").serialize(),
        (resultDataString) => handlesort_taResult(resultDataString)
);
}
$("#sort_title_a").submit((event) => submitsort_ta(event));






/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/stars?id="+title+"&year="+year+"&director="+director+"&star="+star+"&page="+page+"&number="+number
    +"&sort="+sort+"&genres="+genres+"&letters="+letters,
    // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});