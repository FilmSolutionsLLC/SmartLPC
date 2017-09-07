(function() {
	'use strict';

	angular.module('smartLpcApp').controller('ChatController', ChatController);

	ChatController.$inject = [ '$http', 'Principal', '$stateParams',
			'$rootScope', '$scope', '$state', 'translatePartialLoader' ];

	function ChatController($http, Principal, $stateParams, $rootScope, $scope,
			$state, translatePartialLoader) {
		var vm = this;
		
		
		vm.arrToSplit = "2014-06-02 06:29:51 Elliot O. Rodger’s Killings in California Followed Years of Withdrawal http://t.co/quDK3UqX2Z                                                           nytimes";
		
		console.log("arrToSplit : ",vm.arrToSplit);
		//vm.breakIT = [];
		vm.breakIT = vm.arrToSplit.split(/\s+/);
		
		vm.arr1  = vm.breakIT[0].trim().concat(" ").concat(vm.breakIT[1]);
		vm.arr2 = "";
		
		for(var i = 2;i<vm.breakIT.length - 2;i++){
			console.log("Index : "+i + " : "+vm.breakIT[i]);
			vm.arr2 = vm.arr2.concat(vm.breakIT[i]).concat(" ");
		}
		vm.arr3 = vm.breakIT[vm.breakIT.length-2];
		vm.arr4 = vm.breakIT[vm.breakIT.length-1];
		
		
		console.log("arr1 : ",vm.arr1);
		console.log("arr2 : ",vm.arr2);
		console.log("arr3 : ",vm.arr3);
		console.log("arr4 : ",vm.arr4);
		
		
		vm.hello = [];
		console.log("Chat Controller - Smart LPC");
		vm.currentAccount = null;
		Principal.identity().then(function(account) {
			vm.currentAccount = account;

			console.log("Current User : " + JSON.stringify(vm.currentAccount));
		});
		vm.chats=[];
		$(document).ready(
				function() {
					var messageList = $("#messages");

					// defined a connection to a new socket endpoint
					var socket = new SockJS('/stomp');

					var stompClient = Stomp.over(socket);

					stompClient.connect({}, function(frame) {
						// subscribe to the /topic/message endpoint
						stompClient.subscribe("/topic/message", function(data) {
							var message = data.body;
							if (angular.equals(message, "")) {
							} else {
								vm.hello = message.split(":")
								// vm.chats.push(message);
								
								if (vm.hello[0].trim() === vm.currentAccount.fullName.trim()) {
									vm.chatOwner = true;
									console.log("owner");
									console.log("vm.hello[0] : "+vm.hello[0]);
									console.log("vm.currentAccount.fullName : "+vm.currentAccount.fullName);
									messageList.append("<li class='text-danger'><strong>" + vm.hello[0]
									+ "</strong>" + " : <i>" + vm.hello[1]
									+ "</i></li>");
						
								} else {
									console.log("vm.hello[0] : "+vm.hello[0]);
									console.log("vm.currentAccount.fullName : "+vm.currentAccount.fullName);
									
									console.log("not owner");
									vm.chatOwner = false;
									messageList.append("<li><strong>" + vm.hello[0]
									+ "</strong>" + " : " + vm.hello[1]
									+ "</li>");
						
								}
							/*
							 * vm.chats.push({ date: new Date(), owner:
							 * vm.hello[0], message: vm.hello[1] }) if
							 * (angular.equals(vm.hello[0],
							 * vm.currentAccount.fullName)) { vm.chatOwner =
							 * true; } else { vm.chatOwner = false; }
							 */
							}
						});
					});
				});

		vm.send = function() {
			vm.chat.id = null;
			vm.chat.user = vm.currentAccount.fullName;
			vm.chat.time = "2017-08-18";
			console.log(JSON.stringify(vm.chat));
			$http({
				method : 'POST',
				url : 'api/chat',
				data : vm.chat
			}).then(function successCallback(response) {
			}, function errorCallback(response) {
			});

			vm.chat = {};
		};
	}

})();
