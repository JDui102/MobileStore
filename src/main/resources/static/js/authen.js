$(document).ready(function() {
    // Handle validate login/register form
    $("#btn-submit").click(function (e) {
        e.preventDefault();
        let isValid = true;

        // Handle validate not empty
        $('.form input').each(function () {
            if ($(this).val() === '') {
                isValid = false;
                $(this).siblings('p.text-danger').text($(this).prev('label').text() + " can't be left empty!").show();
            } else {
                $(this).siblings('p.text-danger').hide();
            }
        });

        // Handle validate email format
        const emailInput = $('#email');
        if (emailInput.length > 0 && emailInput.val().trim() != '') {
            const emailValue = emailInput.val();
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(emailValue)) {
                isValid = false;
                emailInput.siblings('p.text-danger').text("Invalid email!").show();
            } else {
                emailInput.siblings('p.text-danger').hide();
            }
        }

        // Option submit form
        if (isValid) {
            $('.form').submit();
        }
    });

    // Show password
    $('#show-password-checkbox').change(function() {
        const passwordField = $('#password');

        if ($(this).is(':checked')) {
            passwordField.attr('type', 'text');
        } else {
            passwordField.attr('type', 'password');
        }
    });
});