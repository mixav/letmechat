var stompClient = null;

function setConnected(connected) {
    $("#send").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#pool").html("");
}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        $.get('api/channels', function(data){
            data.forEach( function(channel){
                stompClient.subscribe('/chat/' + channel.id, function (response) {
                     showMessage(JSON.parse(response.body));
                });
            })
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var message = $("#message").val();
    if(message.length > 0){
        stompClient.send("/app/receiver/1", {}, JSON.stringify({'message': message}));
        $("#message").val("");
    }
}

function showMessage(content) {
    $("#pool").append("<tr><td>" + content.username + ": " + content.message + "</td></tr>");
}

$(function () {
    $("#main-content form").on('submit', function (e) {
        e.preventDefault();
    });
    $("header form").on('submit', function (e) {
            disconnect();
    });
    $( "#send" ).click(function() { sendMessage(); });
    connect();
});