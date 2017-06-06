(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ImageDialogController', ImageDialogController);

    ImageDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Image', 'Batch', 'User'];

    function ImageDialogController ($scope, $stateParams, $uibModalInstance, entity, Image, Batch, User) {
        var vm = this;
        vm.image = entity;
        vm.batchs = Batch.query();
        vm.users = User.query();
        vm.load = function(id) {
            Image.get({id : id}, function(result) {
                vm.image = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:imageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.image.id !== null) {
                Image.update(vm.image, onSaveSuccess, onSaveError);
            } else {
                Image.save(vm.image, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.releaseTime = false;
        vm.datePickerOpenStatus.ingestTime = false;
        vm.datePickerOpenStatus.quickpickSelectedTime = false;
        vm.datePickerOpenStatus.createdTime = false;
        vm.datePickerOpenStatus.updatedTime = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
