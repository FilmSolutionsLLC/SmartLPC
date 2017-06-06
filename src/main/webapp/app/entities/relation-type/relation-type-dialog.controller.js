(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('RelationTypeDialogController', RelationTypeDialogController);

    RelationTypeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'RelationType'];

    function RelationTypeDialogController ($scope, $stateParams, $uibModalInstance, entity, RelationType) {
        var vm = this;
        vm.relationType = entity;
        vm.load = function(id) {
            RelationType.get({id : id}, function(result) {
                vm.relationType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:relationTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.relationType.id !== null) {
                RelationType.update(vm.relationType, onSaveSuccess, onSaveError);
            } else {
                RelationType.save(vm.relationType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
