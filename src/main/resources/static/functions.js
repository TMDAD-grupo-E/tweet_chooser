var subscription = null;
var newQuery = 0;

function registerTemplate() {
	template = $("#template").html();
	Mustache.parse(template);
	tt_template = $("#trends_template").html();
	Mustache.parse(tt_template);
}

function setConnected(connected) {
	var search = $('#submitsearch');
	search.prop('disabled', !connected);
}

function registerSendQueryAndConnect() {
    var socket = new SockJS("/twitter");
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        subscriptionTrends = stompClient.subscribe("/queue/trends", function(data) {

            console.log("Subscribed to trendings");

            tt=JSON.parse(data.body);
            		var array = [];
            		tt.forEach(function(element) {
            		    for(var trend in element){
            		    	array.push({'key':trend,'val':element[trend]});

            		    }
            		});
            		console.log({"trends": array})
            		$("#trendingsBlock").html(Mustache.render(tt_template, {"trends": array}));

        })
    });
	$("#search").submit(
			function(event) {
				event.preventDefault();
				if (subscription) {
					subscription.unsubscribe();
				}
				var query = $("#q").val();
				stompClient.send("/app/search", {}, query);
				newQuery = 1;
				subscription = stompClient.subscribe("/queue/search/" + query, function(data) {
					var resultsBlock = $("#resultsBlock");
					if (newQuery) {
                        resultsBlock.empty();
						newQuery = 0;
					}
					var tweet = JSON.parse(data.body);
                    resultsBlock.prepend(Mustache.render(template, tweet));
				});
			});
}

$(document).ready(function() {
	registerTemplate();
	registerSendQueryAndConnect();
});
