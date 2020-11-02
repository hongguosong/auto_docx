var stompClient = {
    client: null,
    socket: null,
    connectCallback: null,
    closeCallback: null,
    connect: function () {
        this.socket = new SockJS('/gs-guide-websocket');
        this.client = Stomp.over(this.socket);
        this.client.connect({}, function (frame) {
            // StatisticReport subscribe
            stompClient.client.subscribe('/topic/notification', function (res) {
                var data = JSON.parse(res.body);
                // window.alert(msg);
                if(data.code == 0){
                    $.toast({
                        heading: "",
                        text: data.msg,
                        showHideTransition: 'slide',
                        icon: 'success',
                        loaderBg: '#f96868',
                        position: 'top-right',
                        hideAfter: 6000
                    });
                } else {
                    $.toast({
                        heading: "",
                        text: data.msg,
                        showHideTransition: 'slide',
                        icon: 'error',
                        loaderBg: '#f2a654',
                        position: 'top-right',
                        loader: false,
                        hideAfter: false
                    });
                }
            });

            if (typeof stompClient.connectCallback === "function") {
                stompClient.connectCallback();
            }
        });
    },
    setConnectCallback: function (fn) {
        this.connectCallback = fn;
    },
    setCloseCallback: function (fn) {
        this.closeCallback = fn;
    },
    consume: function (raw) {
        console.log(raw);
    },
    close: function () {
        if (this.client != null && this.client != undefined) {
            if (typeof stompClient.closeCallback === "function") {
                stompClient.closeCallback(this.client);
            }

            this.client.unsubscribe('/topic/notification');
            this.client.disconnect();
            this.client = null;
        }
    }
};


jQuery(function ($) {
    try {
        stompClient.connect();
    } catch (e) {
        console.log(e);
    }
    window.onbeforeunload = function () {
        try {
            stompClient.close();
        } catch (e) {
            console.log(e);
        }
    }
});