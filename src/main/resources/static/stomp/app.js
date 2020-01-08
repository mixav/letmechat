var stompClient = null;
var activeChannel = null;
var channels = [];

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
        channels.forEach( function(channel){
            stompClient.subscribe('/chat/' + channel.id, function (response) {
                 showMessage(JSON.parse(response.body));
            });
            $("#channels tbody")
                .append("<tr><td style=\"cursor:pointer;\" id="+ channel.id +">" + channel.name +"</td></tr>")
                .click(changeChannel);
        })
        setActiveChannel(channels[0]);
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
        stompClient.send("/app/receiver/" + activeChannel, {}, JSON.stringify({'message': message}));
        $("#message").val("");
    }
}

function showMessage(content) {
    $("#pool").append("<tr><td>" + content.username + ": " + content.message + "</td></tr>");
}

function setActiveChannel(channel) {

    if(activeChannel != null)
        $('#'+activeChannel.id).removeClass("font-weight-bold");
    activeChannel = channel;
    $('#'+activeChannel.id).addClass("font-weight-bold");
    $("#conversation th").html(activeChannel.name);
    $("#pool").html("");
}

function changeChannel(e){
    if(e.target.id && e.target.id != activeChannel.id){ // TODO
        var newActive = channels.find(
            channel => {
            console.log(channel.id);
            console.log(e.target.id);
                return channel.id === e.target.id
            }
        )
        setActiveChannel(newActive);
        console.log(newActive);
    }


}

$(function () {
    $("#main-content form").on('submit', function (e) { e.preventDefault(); });
    $("header form").on('submit', function (e) { disconnect(); });
    $( "#send" ).click(sendMessage);
    $.get('api/channels', function(data){
        channels = data;
        connect();
    })
});