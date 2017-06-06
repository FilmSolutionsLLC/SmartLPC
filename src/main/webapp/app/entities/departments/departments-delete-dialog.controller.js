(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('DepartmentsDeleteController',DepartmentsDeleteController);

    DepartmentsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Departments'];

    function DepartmentsDeleteController($uibModalInstance, entity, Departments) {
        var vm = this;
        vm.departments = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Departments.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
