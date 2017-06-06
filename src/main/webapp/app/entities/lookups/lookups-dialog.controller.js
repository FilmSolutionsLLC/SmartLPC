(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('LookupsDialogController', LookupsDialogController);

    LookupsDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lookups'];

    function LookupsDialogController ($scope, $stateParams, $uibModalInstance, entity, Lookups) {
        var vm = this;
        vm.lookups = entity;
        vm.load = function(id) {
            Lookups.get({id : id}, function(result) {
                vm.lookups = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:lookupsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.lookups.id !== null) {
                Lookups.update(vm.lookups, onSaveSuccess, onSaveError);
            } else {
                Lookups.save(vm.lookups, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
