$(document).ready(function () {

    const regex = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/;

    const signInModal = bootstrap.Modal.getOrCreateInstance('#signInModal');
    const loginModal = bootstrap.Modal.getOrCreateInstance('#loginModal');
    $('#signInButton').click(function() {
        signInModal.show();
    });
    $('#loginButton').click(function() {
        loginModal.show();
    });
    $('#close-modal-btn-signin').click(function() {
        signInModal.hide();
    });
    $('#close-modal-btn-login').click(function() {
        loginModal.hide();
    })
    $('#signIn').submit(function(email, password, confirmPassword) {
        if (!regex.test(email)) {
            $('#signInEmailError').val('Invalid email address');
        } else {
            $('#signInEmailError').val('');
        }
        if (password !== confirmPassword) {
            $('#signInPasswordError').val('Passwords do not match');
        } else {
            $('#signInPasswordError').val('');
        }
        $().post("/user",
            {
                email: email,
                password: password,
                confirmPassword: confirmPassword
            },
            function(data){

            }
        )
        signInModal.hide();
    })
});