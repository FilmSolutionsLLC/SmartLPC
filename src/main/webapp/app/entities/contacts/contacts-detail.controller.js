(function() {
	'use strict';

	angular.module('smartLpcApp').controller('ContactsDetailController',
			ContactsDetailController);

	ContactsDetailController.$inject = [ '$scope', '$rootScope',
			'$stateParams', 'entity', 'Contacts', 'Lookups', 'Departments',
			'User' ];

	function ContactsDetailController($scope, $rootScope, $stateParams, entity,
			Contacts, Lookups, Departments, User) {
		console.log("ContactsDetailController ");
		console.log("print entity: ");

		console.log(JSON.stringify(entity));
		var vm = this;

		vm.contacts = [];
		vm.relatedContacts = [];
		vm.contactDTO = entity;
		vm.load = function(id) {
			Contacts.get({
				id : id
			}, function(result) {
				console.log("ContactsDetailController :  vm.load");
				vm.contactDTO = result.data;

			});

		};
		var unsubscribe = $rootScope.$on('smartLpcApp:contactsUpdate',
				function(event, result) {
					vm.contacts = result;
				});
		$scope.$on('$destroy', unsubscribe);

		vm.printDiv = function(divName) {
			var printContents = document.getElementById(divName).innerHTML;
			var popupWin = window.open('', '_blank', 'width=600,height=600');
			popupWin.document.open();
			popupWin.document
					.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">'
							+ printContents + '</body></html>');
			popupWin.document.close();
		}

		// socket impl

	}
})();
