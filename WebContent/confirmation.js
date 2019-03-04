function handleSessionData(resultDataString) {

    const resultArray = resultDataString.split(",");
    let n=resultArray.length;
    let res = "<ul>";
    for (let i = 0; i < n-1; i+=3) {
        // each item will be in a bullet point
        res += "<li>" + resultArray[i]+"&nbsp"+resultArray[i+1]+"&nbsp"+resultArray[i+2]+"</li>";//even number is id odd is title

    }
    res += "</ul>";

    // show the session information
    $("#item_list").html("");
    $("#item_list").append(res);
}

$.ajax({
    type: "POST",
    url: "api/confirmation",
    success: (resultDataString) => handleSessionData(resultDataString)
});



