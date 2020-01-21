var stompClient = null;
var activeChannel = null;
var channels = [];

function setConnected(connected) {
    $('#send').prop('disabled', !connected);
    if (connected) {
        $('#conversation').show();
    }
    else {
        $('#conversation').hide();
    }
    $('#pool').html('');
}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.reconnect_delay = 5000;
//    stompClient.debug = function(str) {};
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        channels.forEach(connectChannel);
        $('#channels tbody').click(changeChannel);
        setActiveChannel(channels[0]);
    });
}

function connectChannel(channel){
    if(channel) {
        stompClient.subscribe(
            '/chat/' + channel.id,
            function (response) {
                if(parseInt(response.headers.subscription) === activeChannel.id) {
                    showMessage(JSON.parse(response.body));
                } else {
                    //TODO add informing about messages in nonactive channels
            }},
            { id: channel.id }
        );
        $('#channels tbody')
            .append('<tr><td style="cursor:pointer;" id=ch' + channel.id +'>' + channel.name + '</td></tr>');
    }
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log('Disconnected');
}

function sendMessage() {
    var message = $('#message').val();
    if(message.length > 0){
        stompClient.send('/app/receiver/' + activeChannel.id, {}, JSON.stringify({'message': message}));
        $('#message').val('');
    }
    $('#message').focus();
}

function showMessage(content) {
    $('#pool').append(getMessageElement(content));
    $('div.overflow-auto').scrollTop($('div.overflow-auto').prop('scrollHeight'));//TODO use id
}

function setActiveChannel(channel) {
    $.get('api/last/' + channel.id, function(data){
        if(activeChannel != null)
            $('#ch' + activeChannel.id).removeClass('font-weight-bold');
        activeChannel = channel;
        $('#ch' + activeChannel.id).addClass('font-weight-bold');
        $('#conversation th').html(activeChannel.name);
        $('#pool').html('');
        data.forEach(function(content){
            $('#pool').prepend(getMessageElement(content))}
        )
        $('div.overflow-auto').scrollTop($('div.overflow-auto').prop('scrollHeight'));//TODO use id
    })
}

function changeChannel(e){
    var clickedId = parseInt(e.target.id.match(/\d+/), 10);
    if( clickedId !== activeChannel.id){
        var newActive = channels.find(
            channel => {
                return channel.id === clickedId;
            }
        )
        if(newActive)
            setActiveChannel(newActive);
    }
}

function getMessageElement(content){
    return  '<tr><td id=' + content.id + ' class="row mx-0 p-2">' +
            getUserElement(content.username) +
            '<span class="col-sm-11 p-0 text-break">' + content.message + '</span>'+
            (content.time ? getTimeElement(new Date(content.time)): '') +
            '</td></tr>';
}

function getTimeElement(time){
    var date = new Date(time);
    return '<div class="message-time text-secondary" data-toggle="tooltip" data-placement="top" title="' + date.toLocaleDateString() + '">' +
           clearTime(date) + '</div>';
}

function getUserElement(username){
    return '<a href="/user/' + username +
            '" class="col-sm-1 p-0 text-decoration-none text-warning font-weight-bold message-user">' +
             username +
              '</a>';
}

function clearTime(date) {
    var h = (date.getHours() < 10 ? '0' : '') + date.getHours();
    var m = (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();
    return h + ':' + m;
}

function addChannel() {
    $.get('channel/list',function(data){
        $('#chlist tbody').html('');
        data.forEach(function(channel) {
            $('#chlist tbody')
                .append('<tr><td id=chan' + channel.id +' class="d-flex justify-content-between p-2">' +
                    '<div class="my-auto">' + channel.name + '</div>' +
                    checkChannel(channel) +
                    '</td></tr>');
        })
        $('#channelList').modal();
    })
}

function checkChannel(channel){
    if(channels.some(ch => ch.id === channel.id)) {
        return '<button onclick="leaveChannel(event)" id=list'+ channel.id +
                                   '  class="btn btn-sm btn-outline-light">Leave</button>'
    } else {
        return '<button onclick="joinChannel(event)" id=list'+ channel.id +
                                   '  class="btn btn-sm btn-outline-light">Join</button>'
    }
}

function createChannel() {
    var channelName = $('#newName').val();
    if(channelName.length > 0){
        $.post('channel/create',{'name':$('#newName').val()},function(response){
            $('#newName').val('');
            connectChannel(response);
            setActiveChannel(response);
            $('#channelList').modal('hide');
        })
    }

}

function joinChannel(e) {
    var clickedId = parseInt(e.target.id.match(/\d+/), 10);
    $.post('channel/subscribe/'+clickedId,function(response){
        channels.push(response);
        connectChannel(response);
        setActiveChannel(response);
        $('#channelList').modal('hide');
    })
}

function leaveChannel(e) {
    var clickedId = parseInt(e.target.id.match(/\d+/), 10);
    $.post('channel/unsubscribe/'+clickedId,function(response){
        disconnectChannel(response);
        var index = channels.findIndex(ch => response.id === ch.id);
        channels.splice(index, 1);
        if(activeChannel.id === response.id) {
            setActiveChannel(channels[0]);
        }
        $('#ch' + clickedId).parent().remove();
        $('#channelList').modal('hide');
    })
}

function disconnectChannel(channel) {
    stompClient.unsubscribe('/chat/' + channel.id);
}

$(function () {
    $('#main-content form').on('submit', function (e) { e.preventDefault(); });
    $('header form').on('submit', disconnect());
    $('#send').click(sendMessage);
    $.get('api/channels', function(data){
        channels = data;
        connect();
    })
    $('div.overflow-auto').scroll(function() {
        if ($('div.overflow-auto').scrollTop() == 0) {
            var messageId = $('#pool td').first().prop('id');
            if(!messageId) return;
            $.get('api/prev/' + activeChannel.id, { 'id': messageId }, function(data){
                data.forEach(function(message){ //TODO consider usage of for loop for break availability
                    if(message.id < messageId)
                        $('#pool').prepend(getMessageElement(message));
                })
            })
        }
    });
});