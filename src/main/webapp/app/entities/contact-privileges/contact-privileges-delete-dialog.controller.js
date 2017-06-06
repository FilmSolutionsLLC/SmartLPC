(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ContactPrivilegesDeleteController',ContactPrivilegesDeleteController);

    ContactPrivilegesDeleteController.$inject = ['$uibModalInstance', 'entity', 'ContactPrivileges'];

    function ContactPrivilegesDeleteController($uibModalInstance, entity, ContactPrivileges) {
        var vm = this;
        vm.contactPrivileges = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ContactPrivileges.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
