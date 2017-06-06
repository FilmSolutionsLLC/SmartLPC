(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ContactsDeleteController', ContactsDeleteController);

    ContactsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Contacts'];

    function ContactsDeleteController($uibModalInstance, entity, Contacts) {
        var vm = this;
        vm.contactDTO = entity;
      
        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Contacts.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
