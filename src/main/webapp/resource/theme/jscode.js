function login() {
    var username = document.getElementById("username").value;
    var pass = document.getElementById("password").value;

    $.ajax({
        type: "post",
        url: "http://localhost:8080/loginAPI",
        datatype: 'json',
        contentType : 'application/json; charset=utf-8',
        data: JSON.stringify({'username': username, 'password': pass}),
        success: function(){
            location.reload();
        },
        error:function(jqxhr){
            $('#password').val('');
            if (jqxhr.responseText == "invalid-username") {
                $('#l-username-error').html("<span>Username does not exist</span>");
            } else {
                $('#l-username-error').html("");
            }
            if (jqxhr.responseText == "invalid-password") {
                $('#l-pass-error').html("<span>Password is incorrect</span>");
            } else {
                $('#l-pass-error').html("");
            }
        }
    });

    return false;
}

function register() {
    var username = document.getElementById("r-username").value;
    var pass = document.getElementById("r-pass").value;
    var cpass = document.getElementById("r-cpass").value;

    if (pass != cpass) {
        $('#pass-error').html("<span>Please enter password again</span>");
        $('#username-error').html("");
        $('#r-pass').val('');
        $('#r-cpass').val('');
    } else {
        $('#pass-error').html("");

        $.ajax({
            type: "post",
            url: "http://localhost:8080/register",
            datatype: 'json',
            contentType : 'application/json; charset=utf-8',
            data: JSON.stringify({'username': username, 'password': pass}),
            success: function(status){
                location.reload();
            },
            error:function(jqxhr){
                if (jqxhr.responseText == "duplicate-username") {
                    $('#username-error').html("<span>Username already exists</span>");
                    $('#r-pass').val('');
                    $('#r-cpass').val('');
                } else {
                    $('#username-error').html("");
                }
            }
        });
    }

    return false;
}

function removeCart(id) {
    $('#cart' + id).remove();
    var qty = $('#cartSize').text();
    qty--;
    $('#cartSize').html(qty);
    $.ajax({
        type: 'get',
        url: 'http://localhost:8080/removeCart/' + id
    });
}

function addToCart(id) {
    toastr.options.timeOut = 1500;
    toastr.success('Add food to cart successfully');
    $.ajax({
        type: 'get',
        url: 'http://localhost:8080/addToCart/' + id,
        success: function () {
            
        }
    })
}
