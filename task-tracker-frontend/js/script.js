$(document).ready(function () {
    
    const host = "http://localhost:8080"
    
    //modals
    const signUpModal = bootstrap.Modal.getOrCreateInstance('#sign-up-modal');
    const loginModal = bootstrap.Modal.getOrCreateInstance('#login-modal');
    const logoutModal = bootstrap.Modal.getOrCreateInstance('#logout-modal');

    let jwtToken;
    
    //headers
    const contentTypeHeader = {
        "Content-Type": "application/json"
    }
    let authHeader;
    let multiHeader = {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + jwtToken
    }

    //regex for email validation
    const regex = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/;
    
    let email;

    const logoutButtons = $('#logout-buttons');
    logoutButtons.hide();

    if (jwtToken == null) {
        jwtToken = sessionStorage.getItem("jwtToken");
    }
    
    function updateJwtHeader() {
        if (jwtToken != null) {
            authHeader = {
                "Authorization": `Bearer ${jwtToken}`
            }
        }
    }
    
    updateJwtHeader();

    function loadContent() {
        if (authHeader == null) {
            showDefaultContent();
        }
        fetch(`${host}/user`, {
            headers: authHeader,
        }).then(response => {
            if (response.ok) {
                $("#auth-buttons").hide();
                logoutButtons.show();
                response.json().then(data => {
                    email = data.email;
                    $('#username').text(email);
                    $('.content').show();
                    loadTasks();
                });
            }
        }).catch(error => alert(error));
    }

    loadContent();
    
    function loadTasks() {
        fetch(`${host}/tasks/finished`, {
            headers: authHeader
        }).then(
            (response) => response.json().then(
                (tasks) => showTasks($('#finished-tasks'), tasks)
            )
        ).catch(() => alert("Error"));
        
        fetch(`${host}/tasks/unfinished`, {
            headers: authHeader
        }).then(
            (response) => response.json().then(
                (tasks) => showTasks($('#planed-tasks'), tasks)
            ),
        ).catch(() => alert("Error"));
        
        function showTasks(list, tasks) {
            console.log("List element:", list);
            console.log("Tasks:", tasks);
            list.empty();
            $.each(tasks, (index, task) => {
                console.log("Processing task:", task);
                
                const li = $("<li></li>");
                const div = $("<div></div>");
                li.addClass("list-group-item");
                div.addClass("col-9");
                li.append($('<p></p>').text(task.uuid).hide());
                div.append($('<h4></h4>').text(task.title));
                div.append($('<p></p>').text(task.text));
                if (task.finishedTime != null) {
                    div.append($('<p></p>').text(`Finished at ${task.finishedTime}`));
                } else {
                    li.append($('<button></button>').addClass("col-3 btn btn-primary finish-btn")
                        .text("Finish"))
                }
                li.append(div);

                list.append(li);
                
                console.log("Added task to list:", li);
            });
            console.log("Final list element:", list);
        }
    }
    
    $('.finish-btn').on("click", () => {
        event.stopPropagation();
        event.stopImmediatePropagation();
        fetch(`${host}/task/finish`, {
            method: 'POST',
            headers: multiHeader,
            body: `{
                "uuid": "${$(this).parent().find("p").text()}"
            }`
        }).then(response => {
            response.ok ? loadTasks() : alert("Error");
        })
    })

    let incorrect = false;

    $('#login-password').on("change", function () {
        const password = $(this).val();
        const passwordError = $('#login-password-error');
        if (password.length < 8) {
            passwordError.text("Minimum password length 8 characters");
            incorrect = true;
        } else if (password.length > 32) {
            passwordError.text("Maximum password length 32");
            incorrect = true;
        } else if (!/\d/.test(password)) {
            passwordError.text("Password must contains minimum 1 numeric characters");
            incorrect = true;
        } else if (!/[a-zA-Z]/g.test(password)) {
            passwordError.text("Password must contains minimum 1 latin letter");
            incorrect = true;
        } else incorrect = false;
    }); //TODO

    $('#sign-up-button').on("click", () => signUpModal.show());
    $('#login-button').on("click", () => loginModal.show());
    $('#logout-button').on("click", () => logoutModal.show());
    $('#close-modal-btn-sign-up').on("click", () => signUpModal.hide());
    $('#close-modal-btn-login').on("click", () => loginModal.hide());
    $('#close-modal-btn-logout').on("click", () => logoutModal.hide());

    $('#sign-up').on("submit", function (e) {
            e.preventDefault();
            fetch(`${host}/user`, {
                method: 'POST',
                headers: contentTypeHeader,
                body: `{
                    "email": "${$('#sign-up-email').val()}",
                    "password": "${$('#sign-up-password').val()}",
                    "firstName": "${$('#sign-up-confirmPassword').val()}"
                }`
            }).then(response => {
                if (!response.ok) {
                    response.json().then(errorInfo => {
                        $('#sign-up-error').text(errorInfo.message);
                    });
                } else {
                    jwtToken = response.headers.get('Authorization');
                    localStorage.setItem("jwtToken", jwtToken);
                    signUpModal.hide();
                    updateJwtHeader();
                    loadContent();
                }
            })
        });

    $('#login').on("submit", function (e) {
        if (incorrect) return;
        e.preventDefault();
        
        fetch(`${host}/auth/login`, {
            method: 'POST',
            headers: contentTypeHeader,
            credentials: "include",
            body: `{
                "email": "${$('#login-email').val()}",
                "password": "${$('#login-password').val()}"
            }`
        }).then(response => {
            if (!response.ok) {
                response.json().then(errorInfo => {
                    $('#login-error').text(errorInfo.message);
                });
                return;
            }
            jwtToken = response.headers.get("Authorization");
            sessionStorage.setItem("jwtToken", jwtToken);
            loginModal.hide();
            updateJwtHeader();
            loadContent();
        });
    });
    
    $('#logout').on("submit", function (e) {
        e.preventDefault();
        fetch(`${host}/auth/logout`, {
            method: 'POST',
            headers: authHeader
        }).then(response => {
            if (!response.ok) {
                sessionStorage.removeItem("jwtToken");
                jwtToken = null;
                authHeader = null;
                logoutModal.hide();
                showDefaultContent();
            }
        });
    });
    
    function showDefaultContent () {
        $('#auth-buttons').show();
        logoutButtons.hide();
        let unfinishedTasks = $('#unfinished-tasks');
        unfinishedTasks.empty();
        $('#finished-tasks').empty();
        let li = $("<li></li>")
        li.addClass("list-group-item");
        li.append($('<h4></h4>').text("Lets start!"));
        li.append($('<p></p>').text("Sing up or login to see your tasks"));
        unfinishedTasks.append();
        unfinishedTasks.show();
    }
    
});