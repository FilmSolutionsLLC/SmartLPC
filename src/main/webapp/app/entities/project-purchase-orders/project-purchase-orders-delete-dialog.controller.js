(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectPurchaseOrdersDeleteController',ProjectPurchaseOrdersDeleteController);

    ProjectPurchaseOrdersDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectPurchaseOrders'];

    function ProjectPurchaseOrdersDeleteController($uibModalInstance, entity, ProjectPurchaseOrders) {
        var vm = this;
        vm.projectPurchaseOrders = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ProjectPurchaseOrders.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
