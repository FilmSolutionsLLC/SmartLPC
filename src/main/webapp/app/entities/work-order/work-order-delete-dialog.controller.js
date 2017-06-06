(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderDeleteController',WorkOrderDeleteController);

    WorkOrderDeleteController.$inject = ['$uibModalInstance', 'entity', 'WorkOrder'];

    function WorkOrderDeleteController($uibModalInstance, entity, WorkOrder) {
        var vm = this;
        vm.workOrder = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            WorkOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
