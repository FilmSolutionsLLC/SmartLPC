(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectRolesDeleteController',ProjectRolesDeleteController);

    ProjectRolesDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectRoles'];

    function ProjectRolesDeleteController($uibModalInstance, entity, ProjectRoles) {
        var vm = this;
        vm.projectRoles = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ProjectRoles.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
