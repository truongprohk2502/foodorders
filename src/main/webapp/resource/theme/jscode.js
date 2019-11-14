var emailRegex = /^[a-z][a-z0-9_\.]{5,32}@[a-z0-9]{2,}(\.[a-z0-9]{2,4}){1,2}$/;
var phoneRegex = /(09|01[2|6|8|9])+([0-9]{8})\b/;
toastr.options.timeOut = 1500;

function login() {
    var username = $('#username').val();
    var pass = $('#password').val();

    $.ajax({
        type: "post",
        url: "http://localhost:8080/loginAPI",
        datatype: 'json',
        contentType : 'application/json; charset=utf-8',
        data: JSON.stringify({'usernameOrEmail': username, 'password': pass}),
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

function login2() {
    var username = $('#username2').val();
    var pass = $('#password2').val();

    if (username.trim() == '') {
        $('#login-error').html("<span>Please enter username or email</span>");
    } else if (pass.trim() == '') {
        $('#login-error').html("<span>Please enter password</span>");
    } else {
        $.ajax({
            type: "post",
            url: "http://localhost:8080/loginAPI",
            datatype: 'json',
            contentType : 'application/json; charset=utf-8',
            data: JSON.stringify({'usernameOrEmail': username, 'password': pass}),
            success: function(){
                location.reload();
            },
            error:function(jqxhr){
                $('#password2').val('');
                if (jqxhr.responseText == "invalid-username") {
                    $('#login-error').html("<span>Username does not exist</span>");
                }
                if (jqxhr.responseText == "invalid-password") {
                    $('#login-error').html("<span>Password is incorrect</span>");
                }
            }
        });
    }

    return false;
}

function register() {
    var username = $('#r-username').val();
    var email = $('#r-email').val();
    var pass = $('#r-pass').val();
    var cpass = $('#r-cpass').val();

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
            data: JSON.stringify({'username': username, 'email': email, 'password': pass}),
            success: function(status){
                location.reload();
            },
            error:function(jqxhr){
                if (jqxhr.responseText == "duplicate-username") {
                    $('#username-error').html("<span>Username already exists</span>");
                    $('#r-pass').val('');
                    $('#r-cpass').val('');
                } else if (jqxhr.responseText == "duplicate-email") {
                    $('#email-error').html("<span>Email already exists</span>");
                    $('#r-pass').val('');
                    $('#r-cpass').val('');
                } else {
                    $('#username-error').html("");
                    $('#email-error').html("");
                }
            }
        });
    }

    return false;
}

function register2() {
    var username = $('#r-username2').val();
    var email = $('#r-email2').val();
    var pass = $('#r-pass2').val();
    var cpass = $('#r-cpass2').val();

    if (username.trim() == '') {
        $('#register-error').html("<span>Please enter username</span>");
    } else if (email.trim() == '') {
        $('#register-error').html("<span>Please enter email</span>");
    } else if (pass.trim() == '') {
        $('#register-error').html("<span>Please enter password</span>");
    } else if (cpass.trim() == '') {
        $('#register-error').html("<span>Please enter confirm password</span>");
    } else {
        if (pass != cpass) {
            $('#register-error').html("<span>Please enter password again</span>");
            $('#r-pass2').val('');
            $('#r-cpass2').val('');
        } else {
            $.ajax({
                type: "post",
                url: "http://localhost:8080/register",
                datatype: 'json',
                contentType : 'application/json; charset=utf-8',
                data: JSON.stringify({'username': username, 'email': email, 'password': pass}),
                success: function(status){
                    location.reload();
                },
                error:function(jqxhr){
                    if (jqxhr.responseText == "duplicate-username") {
                        $('#register-error').html("<span>Username already exists</span>");
                        $('#r-pass2').val('');
                        $('#r-cpass2').val('');
                    } else if (jqxhr.responseText == "duplicate-email") {
                        $('#register-error').html("<span>Email already exists</span>");
                        $('#r-pass2').val('');
                        $('#r-cpass2').val('');
                    }
                }
            });
        }
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
        url: 'http://localhost:8080/removeCart/' + id,
        success: function () {
            $.ajax({
                type: 'get',
                url: 'http://localhost:8080/getCartInfo',
                success: function (data2) {
                    $('#cartSubtotal').html(data2.subtotal);
                    $('#cartFee').html(data2.shippingFee);
                    $('#cartTotal').html(data2.total);
                }
            });
        }
    });
}

function addToCart(id) {
    var qtyf = $('#qty-food').val();
    $.ajax({
        type: 'get',
        url: 'http://localhost:8080/addToCart/' + id + '/' + qtyf,
        success: function (data) {
            toastr.success('Add food to cart successfully');
            var qty = $('#cartSize').text();
            qty++;
            $('#cartSize').html(qty);

            var s = '<!-- Cartbox Single Item -->';
            s += '<div id="cart' + data.food.id + '">';
            s += '<div class="cartbox__item">';
            s += '<div class="cartbox__item__thumb">';
            s += '<a href="/detail/' + data.food.id + '">';
            s += '<img src="' + data.food.image + '" alt="small thumbnail">';
            s += '</a></div>';
            s += '<div class="cartbox__item__content">';
            s += '<h5><a href="/detail/' + data.food.id + '" class="product-name">' + data.food.name + '</a></h5>';
            s += '<p>Qty: <span>' + qtyf + '</span></p>';
            s += '<span class="price">$' + data.totalPrice + '</span></div>';
            s += '<button class="cartbox__item__remove" onclick="removeCart(' + data.food.id + ')">';
            s += '<i class="fa fa-trash"></i></button></div></div>';
            s += '<!-- //Cartbox Single Item -->';

            $('#cart-food').append(s);

            $.ajax({
                type: 'get',
                url: 'http://localhost:8080/getCartInfo',
                success: function (data2) {
                    $('#cartSubtotal').html(data2.subtotal);
                    $('#cartFee').html(data2.shippingFee);
                    $('#cartTotal').html(data2.total);
                }
            });
        },
        error:function(jqxhr){
            if (jqxhr.status == 406) {
                toastr.error('Food has been added to cart');
            }
        }
    })
}


function updateCart() {
    var foods = [];
    $('.row-food').each(function(){
        var id = $(this).find('input[type="hidden"]').eq(0).val();
        var qty = $(this).find('input[type="number"]').eq(0).val();
        var remove = $(this).find('input[type="checkbox"]').eq(0).is(':checked') ? true : false;
        foods.push({'foodId': id, 'quantity': qty, 'remove': remove});
    });
    $.ajax({
        type: "post",
        url: "http://localhost:8080/updateCart",
        datatype: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(foods),
        success: function () {
            foods.forEach(function (value) {
                if (value.remove) {
                    $('#rowcart' + value.foodId).remove();
                }
            });
            $.ajax({
                type: 'get',
                url: 'http://localhost:8080/getCartInfo',
                success: function (data) {
                    $('#subtotal').text(data.subtotal);
                    $('#fee').text(data.shippingFee);
                    $('#total').text(data.total);
                }
            });
            toastr.success("Update cart successfully");
        }
    });
}

function saveInfo() {
    var username = $('#user').val();
    var firstName = $('#firstName').val();
    var lastName = $('#lastName').val();
    var streetAddr = $('#streetAddress').val();
    var apartmentNum = $('#apartmentNumber').val();
    var city = $('#cityOrProvince').val();
    var district = $('#district').val();
    var email = $('#emailAccount').val();
    var phone = $('#phoneNumber').val();

    if (firstName == '' || lastName == '' || streetAddr == '' || city == '' || district == '' || email == '' || phone == '') {
        toastr.warning("Please enter full billing information");
    } else {
        $.ajax({
            type: "post",
            url: "http://localhost:8080/updateInfo",
            datatype: 'json',
            contentType : 'application/json; charset=utf-8',
            data: JSON.stringify({'username': username, 'firstName': firstName, 'lastName': lastName, 'streetAddress': streetAddr, 'apartmentNumber': apartmentNum, 'city': city, 'district': district, 'email': email, 'phone': phone}),
            success: function(status){
                toastr.success('Update your information successfully');
            },
            error:function(jqxhr){
                if (jqxhr.status == 406) {
                    toastr.error('Sorry! You have not logged in');
                }
            }
        });
    }
}

function order() {
    var firstName = $('#firstName').val();
    var lastName = $('#lastName').val();
    var streetAddr = $('#streetAddress').val();
    var apartmentNum = $('#apartmentNumber').val();
    var city = $('#cityOrProvince').val();
    var district = $('#district').val();
    var email = $('#emailAccount').val();
    var phone = $('#phoneNumber').val();

    if ($('#orderTotal').text() == "$0.0") {
        toastr.warning("Sorry! Your food cart is empty");
    } else {
        if (firstName == '' || lastName == '' || streetAddr == '' || city == '' || district == '' || email == '' || phone == '') {
            toastr.warning("Please enter full billing information");
        } else {
            $.ajax({
                type: "post",
                url: "http://localhost:8080/order",
                datatype: 'json',
                contentType : 'application/json; charset=utf-8',
                data: JSON.stringify({'firstName': firstName, 'lastName': lastName, 'streetAddress': streetAddr, 'apartmentNumber': apartmentNum, 'city': city, 'district': district, 'email': email, 'phone': phone}),
                success: function(status){
                    toastr.success("Congratulations! You have just ordered successfully");
                    setTimeout(function(){
                        window.location.replace("http://localhost:8080/home");
                    }, 1500);
                }
            });
        }
    }
}

function updateFood(id) {
    var name = $('#name' + id).val().trim();
    var image = $('#image' + id).val().trim();
    var price = $('#price' + id).val().trim();
    var summary = $('#summary' + id).val().trim();
    var description = $('#description' + id).val().trim();
    var type = $('#type' + id).val().trim();
    var category = $('#category' + id).val().trim();

    if (name == '' ||image == '' || price == '' || summary == '' || description == '' || type == '' || category == '') {
        toastr.warning("Please enter full food information");
    } else {
        $.ajax({
            type: "post",
            url: "http://localhost:8080/admin/saveFood",
            datatype: 'json',
            contentType : 'application/json; charset=utf-8',
            data: JSON.stringify({'id': id, 'name': name, 'image': image, 'price': price, 'summary': summary, 'description': description, 'foodType': type, 'category': category}),
            success: function(status){
                toastr.success("Updated food information successfully");
            }
        });
    }
}

var delId;
function showDeleteFood(id) {
    delId = id;
    $('#deleteModal').modal('show');
}

function deleteFood() {
    $('#deleteModal').modal('hide');
    $.ajax({
        type: 'get',
        url: 'http://localhost:8080/admin/deleteFood/' + delId,
        success: function () {
            $('#row' + delId).remove();
            toastr.success("Deleted food successfully");
        }
    });
}

function createFood() {
    var name = $('#create-name').val().trim();
    var image = $('#create-image').val().trim();
    var price = $('#create-price').val().trim();
    var summary = $('#create-summary').val().trim();
    var description = $('#create-description').val().trim();
    var type = $('#create-type').val().trim();
    var category = $('#create-category').val().trim();

    if (name == '' ||image == '' || price == '' || summary == '' || description == '' || type == '' || category == '') {
        toastr.warning("Please enter full food information");
    } else {
        $.ajax({
            type: "post",
            url: "http://localhost:8080/admin/saveFood",
            datatype: 'json',
            contentType : 'application/json; charset=utf-8',
            data: JSON.stringify({'name': name, 'image': image, 'price': price, 'summary': summary, 'description': description, 'foodType': type, 'category': category}),
            success: function(status){
                $('#createModal').modal('hide');
                toastr.success("Created new food successfully");
            }
        });
    }
}

var finishId;
function showFinishModal(id) {
    finishId = id;
    $('#finishModal').modal('show');
}

function finishOrder() {
    $('#finishModal').modal('hide');
    var radioVal = $('input[name="finish"]:checked').val();
    $.ajax({
        type: "get",
        url: "http://localhost:8080/admin/finishOrder/" + radioVal + "/" + finishId,
        datatype: 'json',
        contentType : 'application/json; charset=utf-8',
        success: function(){
            $('#row' + finishId).remove();
            toastr.success("Order has " + radioVal + " successfully");
        }
    });
}

function sendEmail() {
    var name = $('#name').val();
    var email = $('#email').val();
    var phone = $('#phone').val();
    var message = $('#message').val();

    if (name == '' || email == '' || phone == '' || message == '') {
        toastr.warning("Please enter full information");
    } else {
        if (!email.match(emailRegex)) {
            toastr.warning("Your email is invalid");
        } else if (!phone.match(phoneRegex)) {
            toastr.warning("Your phone is invalid");
        } else {
            $.ajax({
                type: "post",
                url: "http://localhost:8080/sendEmail",
                datatype: 'json',
                contentType : 'application/json; charset=utf-8',
                data: JSON.stringify({'name': name, 'email': email, 'phone': phone, 'content': message}),
                success: function(){
                    toastr.success("Thanks for supporting your idea by sending email");
                }
            });
        }
    }
}

function loadMap() {
    var latlng = new google.maps.LatLng(16.053178, 108.182051);
    var myOptions = {
        zoom: 13,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map_container"),myOptions);
    var marker = new google.maps.Marker({
        position: latlng,
        map: map,
        title:"Aahar Foods, Danang!"
    });
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(16.073536, 108.202500),
        map: map,
        title:"Aahar Foods, Danang!"
    });
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(16.049882, 108.222614),
        map: map,
        title:"Aahar Foods, Danang!"
    });
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(16.072078, 108.150707),
        map: map,
        title:"Aahar Foods, Danang!"
    });
}