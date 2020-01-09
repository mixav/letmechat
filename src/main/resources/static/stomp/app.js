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
    stompClient.reconnect_delay = 5000;
//    stompClient.debug = function(str) {};
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        channels.forEach( function(channel){
            stompClient.subscribe('/chat/' + channel.id, function (response) {
                if(parseInt(response.headers.subscription) === activeChannel.id)
                    showMessage(JSON.parse(response.body));
                    //TODO add informing about messages in nonactive channels
            }, { id: channel.id });
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
        stompClient.send("/app/receiver/" + activeChannel.id, {}, JSON.stringify({'message': message}));
        $("#message").val("");
    }
    $("#message").focus();
}

function showMessage(content) {
    $("#pool").append("<tr><td>" + content.username + ": " + content.message + "</td></tr>");
    $('div.overflow-auto').scrollTop($('div.overflow-auto').prop('scrollHeight'));
}

function setActiveChannel(channel) {

    if(activeChannel != null)
        $('#'+activeChannel.id).removeClass("font-weight-bold");
    activeChannel = channel;
    $('#'+activeChannel.id).addClass("font-weight-bold");
    $("#conversation th").html(activeChannel.name);
    $("#pool").html("");//TODO show older messages
}

function changeChannel(e){
    var clickedId = parseInt(e.target.id, 10);
    if( clickedId !== activeChannel.id){
        var newActive = channels.find(
            channel => {
                return channel.id === clickedId;
            }
        )
        setActiveChannel(newActive);
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