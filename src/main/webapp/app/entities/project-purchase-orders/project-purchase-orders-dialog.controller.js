(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectPurchaseOrdersDialogController', ProjectPurchaseOrdersDialogController);

    ProjectPurchaseOrdersDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectPurchaseOrders', 'Projects', 'User'];

    function ProjectPurchaseOrdersDialogController ($scope, $stateParams, $uibModalInstance, entity, ProjectPurchaseOrders, Projects, User) {
        var vm = this;
        vm.projectPurchaseOrders = entity;
        vm.projectss = Projects.query();
        vm.users = User.query();
        vm.load = function(id) {
            ProjectPurchaseOrders.get({id : id}, function(result) {
                vm.projectPurchaseOrders = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:projectPurchaseOrdersUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.projectPurchaseOrders.id !== null) {
                ProjectPurchaseOrders.update(vm.projectPurchaseOrders, onSaveSuccess, onSaveError);
            } else {
                ProjectPurchaseOrders.save(vm.projectPurchaseOrders, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.created_date = false;
        vm.datePickerOpenStatus.updated_date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
