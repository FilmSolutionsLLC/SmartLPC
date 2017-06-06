(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderAbcHddDeleteController',WorkOrderAbcHddDeleteController);

    WorkOrderAbcHddDeleteController.$inject = ['$uibModalInstance', 'entity', 'WorkOrderAbcHdd'];

    function WorkOrderAbcHddDeleteController($uibModalInstance, entity, WorkOrderAbcHdd) {
        var vm = this;
        vm.workOrderAbcHdd = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            WorkOrderAbcHdd.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
