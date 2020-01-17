$(function () {
    $('#registration').submit(function (event) {
        register(event);
    });

    $(':password').keyup(function () {
        if ($('#globalError:hidden').length !== 1) {
            if ($('#password').val() != $('#matchingPassword').val()) {
                $("#globalError").show();
            } else {
                $('#globalError').hide();
            }
        }
    });
});

function register(event) {
    event.preventDefault();
    $('.alert').hide();
    // $(".error-list").html("");
    if ($('#password').val() != $('#matchingPassword').val()) {
        $('#globalError').show();
        return;
    }
    var formData = $('form').serialize();
    $.post('/register', formData, function (data) {
        if (data.message == 'success') {
            window.location.href = '/login';
        }
    })
        .fail(function (data) {
            if (data.responseJSON && data.responseJSON.error == 'LoginAlreadyTakenException') {
                $('#loginError').show().html(data.responseJSON.message);
            }
        });
}