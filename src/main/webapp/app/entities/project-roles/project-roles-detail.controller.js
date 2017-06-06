(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectRolesDetailController', ProjectRolesDetailController);

    ProjectRolesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ProjectRoles', 'Projects', 'Contacts', 'User'];

    function ProjectRolesDetailController($scope, $rootScope, $stateParams, entity, ProjectRoles, Projects, Contacts, User) {
        var vm = this;
        vm.projectRoles = entity;
        vm.load = function (id) {
            ProjectRoles.get({id: id}, function(result) {
                vm.projectRoles = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:projectRolesUpdate', function(event, result) {
            vm.projectRoles = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
