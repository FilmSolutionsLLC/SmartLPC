/**
 * Created by macbookpro on 12/27/16.
 */
(function() {
	'use strict';

	angular.module('smartLpcApp')
			.controller('MailsController', MailsController);
	MailsController.$inject = [ 'Principal', 'talents', '$http', '$rootScope',
			'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch',
			'AlertService', '$uibModalInstance', '$scope', '$state' ];

	function MailsController(Principal, talents, $http, $rootScope, Contacts,
			Lookups, Departments, User, ContactsSearch, AlertService,
			$uibModalInstance, $scope, $state) {

		var vm = this;
		vm.talent = talents;
	
		console.log("Mail Controller called..Total Talents or Execs : "
				+ vm.talent.length);
		console.log("Total Keys in JSON object 1:   "
				+ Object.keys(vm.talent[0]).length);
	
		vm.individual = false;
		vm.mailerList = [];

		vm.keyCount = Object.keys(vm.talent[0]).length;
		// console.log(JSON.stringify(vm.talent))
		// + JSON.stringify($scope.roles));
		//
		// $scope.user = {
		// roles : [ $scope.roles[1] ]
		// };
		// $scope.checkAll = function() {
		// $scope.user.roles = angular.copy($scope.roles);
		// };
		// $scope.uncheckAll = function() {
		// $scope.user.roles = [];
		// };
		// $scope.checkFirst = function() {
		// $scope.user.roles.splice(0, $scope.user.roles.length);
		// $scope.user.roles.push($scope.roles[0]);
		// };

		vm.currentAccount = null;
		Principal.identity().then(function(account) {
			vm.currentAccount = account;

			console.log("Current User : " + JSON.stringify(vm.currentAccount));
		});
		vm.checkBOX = {};

		vm.mailer = {};
		if (vm.keyCount < 50) {
			$http({
				method : 'GET',
				url : 'api/mailer/talent'
			}).then(
					function successCallback(response) {

						vm.mailer = response.data;
						var responseBody = vm.mailer.body;
						vm.mailer.body = vm.mailer.body.replace(/XYZ/i,
								vm.currentAccount.fullName);
						vm.mailer.body = vm.mailer.body.replace(/XXXemail/i,
								vm.currentAccount.email);
						console.log("body : " + responseBody);

					}, function errorCallback(response) {

					});
		} else {
			$http({
				method : 'GET',
				url : 'api/mailer/execs'
			}).then(
					function successCallback(response) {

						vm.mailer = response.data;
						var responseBody = vm.mailer.body;
						vm.mailer.body = vm.mailer.body.replace(/XYZ/i,
								vm.currentAccount.fullName);
						vm.mailer.body = vm.mailer.body.replace(/XXXemail/i,
								vm.currentAccount.email);
						console.log("body : " + responseBody);

					}, function errorCallback(response) {

					});
		}

		vm.checkAll = function() {
			console.log("In checkall");
			vm.select = true;
			angular.forEach(vm.checkBOX, function(obj) {
				console.log("in for each");
				obj.selected = true;
			})
			
			for(var i=0;i<vm.talent.length;i++){
				console.log("Adding name : ",vm.talent[i].contact.fullName);
				vm.mailerList.push(vm.talent[i]);
			}
		};

		vm.clearAll = function() {
			vm.select = false;
			angular.forEach(vm.checkBOX, function(obj) {
				console.log("in for each");
				obj.selected = false;
				
			})
			vm.mailerList = [];
		};
		vm.close = function() {

			$uibModalInstance.dismiss('cancel');
		};

		vm.to = [];
		vm.emailSelected = function(email, index) {
			console.log("index : " + index);
			console.log("Selected email : " + email);
			if (vm.checkBOX[index].selected == false) {
				console.log("not selected");
				vm.to.splice(index, 1);
			} else {
				vm.to.push(email);
				console.log("selected");
			}

			console.log("vm.to : " + vm.to);
		};
		
		vm.temp = [];
		vm.save = function() {
			
			for (var i = 0; i < vm.mailerList.length; i++) {
				vm.temp.push(vm.mailerList[i].contact.fullName);
			}
			vm.mailer.to = vm.temp;
			$http({
				method : 'POST',
				url : 'api/mailer',
				data : vm.mailer
			}).then(function successCallback(response) {
				$uibModalInstance.dismiss('cancel');
			}, function errorCallback(response) {

			});
		};
	}
})();