(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('BatchDeleteController',BatchDeleteController);

    BatchDeleteController.$inject = ['$uibModalInstance', 'entity', 'Batch'];

    function BatchDeleteController($uibModalInstance, entity, Batch) {
        var vm = this;
        vm.batch = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Batch.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
