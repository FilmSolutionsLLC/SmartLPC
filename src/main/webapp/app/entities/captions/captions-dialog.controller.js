(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('CaptionsDialogController', CaptionsDialogController);

    CaptionsDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Captions', 'Projects'];

    function CaptionsDialogController ($scope, $stateParams, $uibModalInstance, entity, Captions, Projects) {
        var vm = this;
        vm.captions = entity;
        vm.projectss = Projects.query();
        vm.load = function(id) {
            Captions.get({id : id}, function(result) {
                vm.captions = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:captionsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.captions.id !== null) {
                Captions.update(vm.captions, onSaveSuccess, onSaveError);
            } else {
                Captions.save(vm.captions, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.captionDttm = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
