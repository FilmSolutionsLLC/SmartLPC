(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('LookupsDeleteController',LookupsDeleteController);

    LookupsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lookups'];

    function LookupsDeleteController($uibModalInstance, entity, Lookups) {
        var vm = this;
        vm.lookups = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Lookups.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
