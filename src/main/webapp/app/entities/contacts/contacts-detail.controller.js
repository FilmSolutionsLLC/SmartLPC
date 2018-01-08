(function() {
	'use strict';

	angular.module('smartLpcApp').controller('ContactsDetailController',
			ContactsDetailController);

	ContactsDetailController.$inject = ['$ngConfirm', '$scope', '$rootScope',
			'$stateParams', 'entity', 'Contacts', 'Lookups', 'Departments',
			'User' ];

	function ContactsDetailController($ngConfirm,$scope, $rootScope, $stateParams, entity,
			Contacts, Lookups, Departments, User) {
        console.log("Contacts Detail Controller ");

        var vm = this;


        vm.contacts = [];
        vm.relatedContacts = [];
        vm.contactDTO = entity;

        vm.load = function (id) {

            Contacts.get({
                id: id
            }, function (result) {


                vm.contactDTO = result;

            });

        };
        vm.load(vm.contactDTO.contacts.id);

        var unsubscribe = $rootScope.$on('smartLpcApp:contactsUpdate',
            function (event, result) {
                vm.contacts = result;
            });
        $scope.$on('$destroy', unsubscribe);

        vm.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=600,height=600');
            popupWin.document.open();
            popupWin.document
                .write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">'
                    + printContents + '</body></html>');
            popupWin.document.close();
        };
    }
})();
