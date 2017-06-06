(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('Storage_ServersDialogController', Storage_ServersDialogController);

    Storage_ServersDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Storage_Servers'];

    function Storage_ServersDialogController ($scope, $stateParams, $uibModalInstance, entity, Storage_Servers) {
        var vm = this;
        vm.storage_Servers = entity;
        vm.load = function(id) {
            Storage_Servers.get({id : id}, function(result) {
                vm.storage_Servers = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:storage_ServersUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.storage_Servers.id !== null) {
                Storage_Servers.update(vm.storage_Servers, onSaveSuccess, onSaveError);
            } else {
                Storage_Servers.save(vm.storage_Servers, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
