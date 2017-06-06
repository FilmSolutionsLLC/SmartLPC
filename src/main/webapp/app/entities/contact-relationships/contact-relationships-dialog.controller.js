(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ContactRelationshipsDialogController', ContactRelationshipsDialogController);

    ContactRelationshipsDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ContactRelationships', 'User', 'Contacts'];

    function ContactRelationshipsDialogController ($scope, $stateParams, $uibModalInstance, entity, ContactRelationships, User, Contacts) {
        var vm = this;
        vm.contactRelationships = entity;
        vm.users = User.query();
        vm.contactss = Contacts.query();
        vm.load = function(id) {
            ContactRelationships.get({id : id}, function(result) {
                vm.contactRelationships = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:contactRelationshipsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.contactRelationships.id !== null) {
                ContactRelationships.update(vm.contactRelationships, onSaveSuccess, onSaveError);
            } else {
                ContactRelationships.save(vm.contactRelationships, onSaveSuccess, onSaveError);
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
