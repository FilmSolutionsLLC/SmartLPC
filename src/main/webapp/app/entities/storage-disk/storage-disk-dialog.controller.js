(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('Storage_DiskDialogController', Storage_DiskDialogController);

    Storage_DiskDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Storage_Disk', 'Storage_Servers'];

    function Storage_DiskDialogController ($scope, $stateParams, $uibModalInstance, entity, Storage_Disk, Storage_Servers) {
        var vm = this;
        vm.storage_Disk = entity;
        vm.storage_serverss = Storage_Servers.query();
        vm.load = function(id) {
            Storage_Disk.get({id : id}, function(result) {
                vm.storage_Disk = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:storage_DiskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.storage_Disk.id !== null) {
                Storage_Disk.update(vm.storage_Disk, onSaveSuccess, onSaveError);
            } else {
                Storage_Disk.save(vm.storage_Disk, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.lastUpdated = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
