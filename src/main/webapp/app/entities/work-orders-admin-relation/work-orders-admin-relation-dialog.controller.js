(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrdersAdminRelationDialogController', WorkOrdersAdminRelationDialogController);

    WorkOrdersAdminRelationDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'WorkOrdersAdminRelation', 'WorkOrder', 'User', 'RelationType'];

    function WorkOrdersAdminRelationDialogController ($scope, $stateParams, $uibModalInstance, entity, WorkOrdersAdminRelation, WorkOrder, User, RelationType) {
        var vm = this;
        vm.workOrdersAdminRelation = entity;
        vm.workorders = WorkOrder.query();
        vm.users = User.query();
        vm.relationtypes = RelationType.query();
        vm.load = function(id) {
            WorkOrdersAdminRelation.get({id : id}, function(result) {
                vm.workOrdersAdminRelation = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:workOrdersAdminRelationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.workOrdersAdminRelation.id !== null) {
                WorkOrdersAdminRelation.update(vm.workOrdersAdminRelation, onSaveSuccess, onSaveError);
            } else {
                WorkOrdersAdminRelation.save(vm.workOrdersAdminRelation, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
