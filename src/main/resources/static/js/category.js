$(document).ready(function() {
    // Handle input in search input field
    $('#searchInput').on('input', function() {
        if ($(this).val().trim() === '') {
            $('#submitButton').prop('disabled', true); // Disable submit button if search input is empty
            //openSearchHistory(); // Open search history if search input is empty
        } else {
            $('#submitButton').prop('disabled', false); // Enable submit button if search input is not empty
        }
    });

    // Enable or disable notification
    const notification = $('.notification');
    if (notification.length) {
        setTimeout(function() {
            notification.addClass('notification--disable');
            setTimeout(() => notification.remove(), 1000);
        }, 3000);
    }

    // Focus field input
    const focus = $('.focus');
    const value = focus.val();
    focus.val('').val(value); // Set the value to the input and move the cursor to the end
    focus.focus(); // Focus
});
