(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('DepartmentsDetailController', DepartmentsDetailController);

    DepartmentsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Departments'];

    function DepartmentsDetailController($scope, $rootScope, $stateParams, entity, Departments) {
        var vm = this;
        vm.departments = entity;
        vm.load = function (id) {
            Departments.get({id: id}, function(result) {
                vm.departments = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:departmentsUpdate', function(event, result) {
            vm.departments = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
