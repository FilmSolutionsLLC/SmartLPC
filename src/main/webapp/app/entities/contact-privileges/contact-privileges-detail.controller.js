(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ContactPrivilegesDetailController', ContactPrivilegesDetailController);

    ContactPrivilegesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ContactPrivileges', 'Projects', 'Contacts', 'User'];

    function ContactPrivilegesDetailController($scope, $rootScope, $stateParams, entity, ContactPrivileges, Projects, Contacts, User) {
        var vm = this;
        vm.contactPrivileges = entity;
        vm.load = function (id) {
            ContactPrivileges.get({id: id}, function(result) {
                vm.contactPrivileges = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:contactPrivilegesUpdate', function(event, result) {
            vm.contactPrivileges = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
