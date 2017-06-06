(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ContactRelationshipsDeleteController',ContactRelationshipsDeleteController);

    ContactRelationshipsDeleteController.$inject = ['$uibModalInstance', 'entity', 'ContactRelationships'];

    function ContactRelationshipsDeleteController($uibModalInstance, entity, ContactRelationships) {
        var vm = this;
        vm.contactRelationships = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ContactRelationships.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
