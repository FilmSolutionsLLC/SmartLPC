(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ContactPrivilegesDialogController', ContactPrivilegesDialogController);

    ContactPrivilegesDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ContactPrivileges', 'Projects', 'Contacts', 'User'];

    function ContactPrivilegesDialogController ($scope, $stateParams, $uibModalInstance, entity, ContactPrivileges, Projects, Contacts, User) {
        var vm = this;
        vm.contactPrivileges = entity;
        vm.projectss = Projects.query();
        vm.contactss = Contacts.query();
        vm.users = User.query();
        vm.load = function(id) {
            ContactPrivileges.get({id : id}, function(result) {
                vm.contactPrivileges = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:contactPrivilegesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.contactPrivileges.id !== null) {
                ContactPrivileges.update(vm.contactPrivileges, onSaveSuccess, onSaveError);
            } else {
                ContactPrivileges.save(vm.contactPrivileges, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.lastLoginDt = false;
        vm.datePickerOpenStatus.lastLogoutDt = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;
        vm.datePickerOpenStatus.expireDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
