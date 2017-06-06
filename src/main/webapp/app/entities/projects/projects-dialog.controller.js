(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectsDialogController', ProjectsDialogController);

    ProjectsDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Projects', 'Lookups', 'Contacts', 'User', 'Departments', 'Storage_Disk'];

    function ProjectsDialogController ($scope, $stateParams, $uibModalInstance, entity, Projects, Lookups, Contacts, User, Departments, Storage_Disk) {
        var vm = this;
        vm.projects = entity;
        vm.lookupss = Lookups.query();
        vm.contactss = Contacts.query();
        vm.users = User.query();
        vm.departmentss = Departments.query();
        vm.storage_disks = Storage_Disk.query();
        vm.load = function(id) {
            Projects.get({id : id}, function(result) {
                vm.projects = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:projectsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.projects.id !== null) {
                Projects.update(vm.projects, onSaveSuccess, onSaveError);
            } else {
                Projects.save(vm.projects, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;
        vm.datePickerOpenStatus.shootDate = false;
        vm.datePickerOpenStatus.tagDate = false;
        vm.datePickerOpenStatus.reminderDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
