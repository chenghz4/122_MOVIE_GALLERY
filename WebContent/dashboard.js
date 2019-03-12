/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        $("#login_error_message").text(resultDataJson["message"]);

    }
    else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#login_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.get(
        "api/write/dash",
        // Serialize the login form to the data sent by POST request
        $("#star_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString)
);
}


function handleMovieResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        $("#login_error_message_movie").text(resultDataJson["message"]);

    }
    else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#login_error_message_movie").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitMovieForm(formSubmitEvent) {
    console.log("22");
    formSubmitEvent.preventDefault();

    $.post(
        "api/write/dash",
        // Serialize the login form to the data sent by POST request
        $("#movie_form").serialize(),
        (resultDataString) => handleMovieResult(resultDataString)
);
}


// Bind the submit action of the form to a handler function
$("#star_form").submit((event) => submitLoginForm(event));
$("#movie_form").submit((event) => submitMovieForm(event));

