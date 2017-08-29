/**
 * Created by macbookpro on 6/20/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('IndividualPrivilegesController', IndividualPrivilegesController);
    IndividualPrivilegesController.$inject = ['id', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state', 'Projects','ContactPrivileges'];

    function IndividualPrivilegesController(id, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state, Projects,ContactPrivileges) {
    	 var vm = this;

    	 
    	 console.log("Id got: "+id)
        vm.downloadType = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "ALL"}, {'id': 2, 'value': "Lock Approved"}];
        vm.exclusives = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "BASIC"}, {'id': 2, 'value': "MASTER"}];

         $http({
             method: 'GET',
             url: 'api/contact-privileges/'+id,
         }).then(function successCallback(response) {
        	 vm.contactPrivileges = response.data;
        	 console.log("Got from Individual privileges controller : "+JSON.stringify(vm.contactPrivileges));
         }, function errorCallback(response) {

         });
         vm.close = function () {

             $uibModalInstance.dismiss('cancel');
         };

    
    }
})();