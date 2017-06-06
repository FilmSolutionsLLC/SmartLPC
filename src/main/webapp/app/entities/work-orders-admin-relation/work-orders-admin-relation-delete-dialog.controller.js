(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrdersAdminRelationDeleteController',WorkOrdersAdminRelationDeleteController);

    WorkOrdersAdminRelationDeleteController.$inject = ['$uibModalInstance', 'entity', 'WorkOrdersAdminRelation'];

    function WorkOrdersAdminRelationDeleteController($uibModalInstance, entity, WorkOrdersAdminRelation) {
        var vm = this;
        vm.workOrdersAdminRelation = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            WorkOrdersAdminRelation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
