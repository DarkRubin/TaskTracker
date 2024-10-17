$(document).ready(function () {


    const host = "http://185.237.207.128:8080"

    //modals
    const signUpModal = bootstrap.Modal.getOrCreateInstance('#sign-up-modal');
    const loginModal = bootstrap.Modal.getOrCreateInstance('#login-modal');
    const logoutModal = bootstrap.Modal.getOrCreateInstance('#logout-modal');
    const editTaskModal = bootstrap.Modal.getOrCreateInstance('#edit-modal');

    const newTaskLi = $('#add-task-li');
    const calendarLi = $('#calendar-li');
    const unfinishedTasksButtons = $('.task-buttons');
    const finishedTasksButtons = $('.task-buttons-finished');
    const timer = $('#timer').hide();
    const chooser = $('#chooser');
    
    const toast = $('#api-error-toast');

    let jwtToken = null;

    let toWork = $('#work');

    let toBreak = $('#break');

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
            multiHeader = {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + jwtToken
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
        loadFinishedTasks();
        loadUnfinishedTasks()
    }
    
    function loadFinishedTasks() {
        fetch(`${host}/tasks/finished`, {
            headers: authHeader
        }).then(
            (response) => response.json().then(
                (tasks) => showTasks(tasks, $('#finished-tasks').empty().append(calendarLi), true)
            )
        ).catch(() => alert("Error"));
    }
    
    function loadUnfinishedTasks() {
        fetch(`${host}/tasks/unfinished`, {
            headers: authHeader
        }).then(
            (response) => response.json().then(
                (tasks) => showTasks(tasks, $('#planed-tasks').empty().append(newTaskLi), false)
            ),
        ).catch(() => alert("Error"));
    }
    
    function showTasks(tasks, list, tasksFinished) {
        $.each(tasks, (index, task) => {
            const li = $("<li></li>");
            const row = $("<div class='row'></div>");
            row.append($('<div class="col-1"></div>'));
            const div = $("<div class='col-8'></div>");
            li.addClass("list-group-item");
            div.append($('<p></p>').text(task.uuid).addClass("uuid visually-hidden"));
            div.append($('<h4 class="task-title"></h4>').text(task.title));
            div.append($('<p class="task-text"></p>').text(task.text));
            let doBefore = task.doBefore;
            if (doBefore != null) {
                let date = doBefore.substring(doBefore.indexOf("T") + 1, doBefore.length - 4);
                if (Date.parse(doBefore) < Date.now()) {
                    div.append($('<p class="task-do-before text-danger"></p>').text(date));
                } else {
                    div.append($('<p class="task-do-before"></p>').text(date));
                }
            }
            let finishedTime = task.finishedTime;
            if (finishedTime != null) {
                finishedTime = finishedTime.substring(finishedTime.indexOf("T") + 1, finishedTime.length - 1);
                div.append($('<p class="task-finish-time"></p>').text(finishedTime));
            }
            row.append(div);
            if (tasksFinished) {
                row.append(finishedTasksButtons.clone());
            } else{
                row.append(unfinishedTasksButtons.clone());
            }
            row.append($('<div class="col-1"></div>'));
            li.append(row);
            list.append(li);
        });
    }

    $(document).on("click", ".finish-btn", function (e) {
        e.stopPropagation();
        if (isNotAuthorized()) {
            loginModal.show();
            return;
        }
        const uuid = $(this).closest('.row').find('.uuid').text();
        fetch(`${host}/task/finish`, {
            method: 'PATCH',
            headers: multiHeader,
            body: `{
                "uuid": "${uuid}"
            }`
        }).then(response => {
            response.ok ? loadTasks() : alert("Error");
        });
    });

    $(document).on("click", '#add-task-submit', function (e) {
        e.preventDefault();
        const title = $('#task-title').val();
        if (title === "") {
            alert("Title cannot be empty");
            return;
        }
        if (isNotAuthorized()) {
            loginModal.show();
            return;
        }
        let doBefore = $('#task-do-before').val();
        if (doBefore !== null && doBefore !== undefined && doBefore !== "") {
            doBefore = new Date().setHours(parseInt(doBefore.substring(0, doBefore.indexOf(":"))),
                parseInt(doBefore.substring(doBefore.indexOf(":") + 1, doBefore.length)));
            let currentDate = new Date();
            currentDate.setTime(doBefore);
            doBefore = currentDate.toISOString();
        }
        fetch(`${host}/task`, {
            method: 'POST',
            headers: multiHeader,
            body: `{
                "title": "${title}",
                "text": "${$('#task-text').val()}",
                "doBefore": "${doBefore}"
            }`
        }).then(response =>
        response.ok ? loadTasks() : response.json().then(errorInfo => {alert(errorInfo.message)}));
    });

    $(document).on("click", '.edit-btn', function (e) {
        e.stopPropagation();
        if (isNotAuthorized()) {
            loginModal.show();
            return;
        }
        $('#edit-task-uuid').val($(this).closest('.row').find('.uuid').text())
        $('#edit-task-title').val($(this).closest('.row').find('.task-title').text());
        $('#edit-task-text').val($(this).closest('.row').find('.task-text').text());
        $('#edit-task-do-before').val($(this).closest('.row').find('.task-do-before').text());
        $('#edit-task-finished-time').val($(this).closest('.row').find('.task-finish-time').text());

        editTaskModal.show();
    });

    $('#submit-edit').on("click", function (e) {
        e.preventDefault();
        fetch(`${host}/task`, {
            method: 'PUT',
            headers: multiHeader,
            body: `{
                "uuid": "${$('#edit-task-uuid').val()}",
                "title": "${$('#edit-task-title').val()}",
                "text": "${$('#edit-task-text').val()}",
                "doBefore": "${$('#edit-task-do-before').val()}",
                "finishedTime": "${$('#edit-task-finished-time').val()}"
            }`
        }).then(response => {
            if (response.ok) {
                loadTasks();
                editTaskModal.hide();
            } else {
                response.json().then(errorInfo => {
                    $('#edit-task-error').text(errorInfo.message);
                });
            }
        });
    });

    $(document).on("click", '.continue-btn', function (e) {
        e.preventDefault();
        if (isNotAuthorized()) {
            loginModal.show();
            return;
        }
        const uuid = $(this).closest('.row').find('.uuid').text();
        fetch(`${host}/task/continue`, {
            method: 'PATCH',
            headers: multiHeader,
            body: `{
                "uuid": "${uuid}"
            }`
        }).then(response => {
            response.ok ? loadTasks() : handleError(response);
        });
    });

    
    $(document).on("click", '.delete-btn', function (e) {
        e.preventDefault();
        if (isNotAuthorized()) {
            loginModal.show();
            return;
        }
        fetch(`${host}/task`, {
            method: 'DELETE',
            headers: multiHeader,
            body: `{
                "uuid": "${$(this).closest('.row').find('.uuid').text()}"
            }`
        }).then(response => {
            if (response.ok) {
                loadTasks();
                editTaskModal.hide();
            } else {
                handleError(response.json());
            }
        });
    });
    
    $('#delete-modal').on("click", function (e) {
        e.preventDefault();
        if (isNotAuthorized()) {
            loginModal.show();
            return;
        }
        fetch(`${host}/task`, {
            method: 'DELETE',
            headers: multiHeader,
            body: `{
                "uuid": "${$('#edit-task-uuid').val()}"
            }`
        }).then(response => {
            if (response.ok) {
                loadTasks();
                editTaskModal.hide();
            } else {
                response.json().then(() => {
                    $('#edit-task-error').text("Cannot delete task");
                });
            }
        });
    });
    function isNotAuthorized() {
        return multiHeader.Authorization === "Bearer null";

    }

    let incorrect = false;

    $('#sign-up-password').on("change", function () {
        const password = $(this).val();
        const passwordError = $('#sign-up-password-error');
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
    $(document).on("click", '#do-before-checkbox', () => {
        if ($('#do-before-checkbox').val()) {
            $('#task-do-before').removeAttr("disabled")
        } else {
            $('#task-do-before').attr("disabled", "disabled");
        }
        
    });
    $('#sign-up-button').on("click", () => signUpModal.show());
    $('#login-button').on("click", () => loginModal.show());
    $('#logout-button').on("click", () => logoutModal.show());
    $('#close-modal-btn-sign-up').on("click", () => signUpModal.hide());
    $('#close-modal-btn-login').on("click", () => loginModal.hide());
    $('#close-modal-btn-logout').on("click", () => logoutModal.hide());
    $('#close-modal-btn-edit').on("click", () => editTaskModal.hide());
    $('#move-to-sign-up').on("click", () => {
        loginModal.hide();
        signUpModal.show();
    });
    $('#move-to-login').on("click", () => {
        signUpModal.hide();
        loginModal.show();
    });
    $(document).on("click", '#calendar-btn', function (e) {
        e.preventDefault();
        let date = $('#calendar-date').val();
        if (isNotAuthorized()) {
            loginModal.show();
            return;
        }
        if (date === "") {
            loadFinishedTasks();
            return;
        }
        date = date + "T00:00:00.000Z";
        fetch(`${host}/tasks/unfinished/${date}`, {
            method: 'GET',
            headers: multiHeader
        }).then(response => {
            if (response.ok) {
                response.json().then(tasks => {
                    showTasks(tasks, $('#finished-tasks').empty().append(calendarLi), true);
                });
            } else alert(response.json().then(message => message.message));
        })
    });

    $('#login-email').on("input", function () {
        let email = $('#login-email').val();
        if (regex.test(email)) {
            incorrect = false;
            $('#login-email-error').text('');
        } else {
            incorrect = true;
            $('#login-email-error').text("Invalid email address");
        }
    });

    $('#sign-up-email').on("input", function () {
        let email = $('#sign-up-email').val();
        if (regex.test(email)) {
            incorrect = false;
            $('#sign-up-email-error').text('');
        } else {
            incorrect = true;
            $('#sign-up-email-error').text("Invalid email address");
        }
    });

    $('#sign-up').on("submit", function (e) {
        e.preventDefault();
        if (incorrect) return;
        fetch(`${host}/user`, {
            method: 'POST',
            headers: contentTypeHeader,
            body: `{
                    "email": "${$('#sign-up-email').val()}",
                    "password": "${$('#sign-up-password').val()}"
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
        e.preventDefault();
        if (incorrect) return;
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
            method: 'DELETE',
            headers: authHeader
        }).then(response => {
            if (response.ok) {
                sessionStorage.removeItem("jwtToken");
                jwtToken = null;
                authHeader = null;
                logoutModal.hide();
                showDefaultContent();
            }
        });
    });

    function showDefaultContent() {
        $('#auth-buttons').show();
        logoutButtons.hide();
        let unfinishedTasks = $('#planed-tasks');
        unfinishedTasks.empty().append(newTaskLi);
        let finishedTasks = $('#finished-tasks');
        finishedTasks.empty().append(calendarLi);
        showTasks([
            {
                uuid: "1",
                title: "Log in or Sign-up",
                text: "For enable all features you need to lod in or sign-up",
                finishedTime: null,
            }
        ], unfinishedTasks, false);
    }
    
    function handleError(error) {
        error.json().then(errorInfo => {
            const message = errorInfo.message;
            if (message === "Authentication token is expired, login for get new token" || 
                message === "Invalid JWT token") {
                updateJwtToken();
            }
            if (message === "User not found" || 
                message === "User not authorized or token is expired") {
                showDefaultContent();
                loginModal.show();
            } else {
                $(toast).find('.toast-body').text(message);
                toast.toast("show");
            }
        });
    }
    
    

    $(document).on("click", "#less-work", function (e) {
        e.preventDefault();
        if (toWork.text() > 10) {
            toWork.text(parseInt(toWork.text()) - 5);
        }
    });

    $(document).on("click", "#more-work", function (e) {
        e.preventDefault();
        if (toWork.text() < 120) {
            toWork.text(parseInt(toWork.text()) + 5);
        }
    });

    $(document).on("click", "#less-break", function (e) {
        e.preventDefault();
        if (toBreak.text() > 5) {
            toBreak.text(parseInt(toBreak.text()) - 5);
        }
    });

    $(document).on("click", "#more-break", function (e) {
        e.preventDefault();
        if (toBreak.text() < 40) {
            toBreak.text(parseInt(toBreak.text()) + 5);
        }
    });
    
    let stop = false;
    
    $(document).on("click", "#start-btn", function (e) {
        e.preventDefault();
        if (isNotAuthorized()) {
            loginModal.show();
            return;
        }
        chooser.hide();
        timer.show();
        stop = false
        pomodoroTimer().then();
    });

    const minutes = $('#minutes');
    const seconds = $('#seconds');
    
    async function pomodoroTimer() {
        minutes.text(toWork.text());
        seconds.text(0);
        setInterval(function () {
            if (stop) {
                clearInterval(id);
                return;
            }
            let min = parseInt(minutes.text());
            let sec = parseInt(seconds.text());
            if (sec === 0) {
                if (min === 0) {
                    clearInterval(id);
                } else {
                    min--;
                    sec = 59;
                }
            } else {
                sec--;
            }
            minutes.text(min);
            if (sec < 10) {
                seconds.text("0" + sec)
            } else {
                seconds.text(sec);
            }
        }, 1000)
    }
    
    $(document).on("click", "#end-btn", function (e) {
        e.preventDefault();
        stop = true
        chooser.show();
        timer.hide();
    })
    
    
    function updateJwtToken() {
        fetch(`${host}/user`, {
            headers: multiHeader
        }).then(response => {
            if (response.ok) {
                jwtToken = response.headers.get("Authorization");
                updateJwtHeader();
            } else handleError(response);
        })
        
    }

});
