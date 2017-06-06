(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('BatchDialogController', BatchDialogController);

    BatchDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Batch', 'Projects', 'User'];

    function BatchDialogController ($scope, $stateParams, $uibModalInstance, entity, Batch, Projects, User) {
        var vm = this;
        vm.batch = entity;
        vm.projectss = Projects.query();
        vm.users = User.query();
        vm.load = function(id) {
            Batch.get({id : id}, function(result) {
                vm.batch = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:batchUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.batch.id !== null) {
                Batch.update(vm.batch, onSaveSuccess, onSaveError);
            } else {
                Batch.save(vm.batch, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.createdTime = false;
        vm.datePickerOpenStatus.updatedTime = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
