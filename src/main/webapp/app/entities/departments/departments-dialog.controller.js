(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('DepartmentsDialogController', DepartmentsDialogController);

    DepartmentsDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Departments'];

    function DepartmentsDialogController ($scope, $stateParams, $uibModalInstance, entity, Departments) {
        var vm = this;
        vm.departments = entity;
        vm.load = function(id) {
            Departments.get({id : id}, function(result) {
                vm.departments = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:departmentsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.departments.id !== null) {
                Departments.update(vm.departments, onSaveSuccess, onSaveError);
            } else {
                Departments.save(vm.departments, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
