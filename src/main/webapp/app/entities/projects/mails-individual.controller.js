/**
 * Created by macbookpro on 12/27/16.
 */
(function() {
	'use strict';

	angular.module('smartLpcApp').controller('MailsIndividualController',
			MailsIndividualController);
	MailsIndividualController.$inject = [ 'Principal', 'talent', '$http',
			'$rootScope', 'Contacts', 'Lookups', 'Departments', 'User',
			'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope',
			'$state' ];

	function MailsIndividualController(Principal, talent, $http, $rootScope,
			Contacts, Lookups, Departments, User, ContactsSearch, AlertService,
			$uibModalInstance, $scope, $state) {

		var vm = this;
		vm.talent = talent;

		vm.individual = true;

		// console.log("Total Keys in JSON object: "+
		// Object.keys(vm.talent).length);
		vm.keyCount = Object.keys(vm.talent).length;

		vm.currentAccount = null;
		Principal.identity().then(function(account) {
			vm.currentAccount = account;

			console.log("Current User : " + JSON.stringify(vm.currentAccount));
		});
		// vm.checkBOX = {};

		vm.mailer = {};
		console.log("KeyCount : "+vm.keyCount);
		if (vm.keyCount < 50) {
			console.log("less than 50");
			$http({
				method : 'GET',
				url : 'api/mailer/talent'
			}).then(
					function successCallback(response) {
						console.log(response.data);
						console.log("in http get of talent");
						vm.mailer = response.data;
						var responseBody = vm.mailer.body;
						vm.tempTO = vm.talent.contact.fullName;
						vm.mailer.body = vm.mailer.body.replace(/XYZ/i,
								vm.currentAccount.fullName);
						vm.mailer.body = vm.mailer.body.replace(/XXXemail/i,
								vm.currentAccount.email);
						console.log("body : " + responseBody);
						vm.mailer.body = vm.mailer.body.replace("XXXXX",
								vm.talent.contact.username);
						vm.mailer.body = vm.mailer.body.replace("ZZZZZ",
								vm.talent.contact.password);

					}, function errorCallback(response) {

					});
		} else {
			$http({
				method : 'GET',
				url : 'api/mailer/execs'
			}).then(
					function successCallback(response) {
						console.log("in http get of execs");
						vm.mailer = response.data;
						var responseBody = vm.mailer.body;
						vm.tempTO = vm.talent.contact.fullName;
						vm.mailer.body = vm.mailer.body.replace(/XYZ/i,
								vm.currentAccount.fullName);
						vm.mailer.body = vm.mailer.body.replace(/XXXemail/i,
								vm.currentAccount.email);
						vm.mailer.body = vm.mailer.body.replace("XXXXX",
								vm.talent.contact.username);
						vm.mailer.body = vm.mailer.body.replace("ZZZZZ",
								vm.talent.contact.password);
						console.log("body : " + responseBody);

					}, function errorCallback(response) {

					});
		}

		vm.close = function() {

			$uibModalInstance.dismiss('cancel');
		};

		// send mail
		vm.mailerList = [];

		vm.temp = [];
		vm.save = function() {

			vm.temp.push(vm.tempTO);
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