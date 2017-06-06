(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectLabTasksDialogController', ProjectLabTasksDialogController);

    ProjectLabTasksDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectLabTasks', 'Projects', 'Lookups', 'User'];

    function ProjectLabTasksDialogController ($scope, $stateParams, $uibModalInstance, entity, ProjectLabTasks, Projects, Lookups, User) {
        var vm = this;
        vm.projectLabTasks = entity;
        vm.projectss = Projects.query();
        vm.lookupss = Lookups.query();
        vm.users = User.query();
        vm.load = function(id) {
            ProjectLabTasks.get({id : id}, function(result) {
                vm.projectLabTasks = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:projectLabTasksUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.projectLabTasks.id !== null) {
                ProjectLabTasks.update(vm.projectLabTasks, onSaveSuccess, onSaveError);
            } else {
                ProjectLabTasks.save(vm.projectLabTasks, onSaveSuccess, onSaveError);
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
