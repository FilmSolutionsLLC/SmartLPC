(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectRolesDialogController', ProjectRolesDialogController);

    ProjectRolesDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectRoles', 'Projects', 'Contacts', 'User'];

    function ProjectRolesDialogController ($scope, $stateParams, $uibModalInstance, entity, ProjectRoles, Projects, Contacts, User) {
        var vm = this;
        vm.projectRoles = entity;
        vm.projectss = Projects.query();
        vm.contactss = Contacts.query();
        vm.users = User.query();
        vm.load = function(id) {
            ProjectRoles.get({id : id}, function(result) {
                vm.projectRoles = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:projectRolesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.projectRoles.id !== null) {
                ProjectRoles.update(vm.projectRoles, onSaveSuccess, onSaveError);
            } else {
                ProjectRoles.save(vm.projectRoles, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.miniFullDt = false;
        vm.datePickerOpenStatus.fullFinalDt = false;
        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.expireDate = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
