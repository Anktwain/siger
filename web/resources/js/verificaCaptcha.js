$(function() {
    $("body").click(function(e) {
        if (e.target.id == "myDiv" || $(e.target).parents("#myDiv").size()) { 
            alert("Inside div");
        } else { 
           alert("Outside div");
        }
    });
})