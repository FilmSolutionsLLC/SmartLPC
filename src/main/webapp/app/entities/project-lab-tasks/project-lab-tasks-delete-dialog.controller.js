(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectLabTasksDeleteController',ProjectLabTasksDeleteController);

    ProjectLabTasksDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectLabTasks'];

    function ProjectLabTasksDeleteController($uibModalInstance, entity, ProjectLabTasks) {
        var vm = this;
        vm.projectLabTasks = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ProjectLabTasks.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
