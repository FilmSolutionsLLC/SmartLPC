(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectsDeleteController',ProjectsDeleteController);

    ProjectsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Projects'];

    function ProjectsDeleteController($uibModalInstance, entity, Projects) {
        var vm = this;
        vm.projects = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Projects.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
