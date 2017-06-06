(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderAbcFileDeleteController',WorkOrderAbcFileDeleteController);

    WorkOrderAbcFileDeleteController.$inject = ['$uibModalInstance', 'entity', 'WorkOrderAbcFile'];

    function WorkOrderAbcFileDeleteController($uibModalInstance, entity, WorkOrderAbcFile) {
        var vm = this;
        vm.workOrderAbcFile = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            WorkOrderAbcFile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
