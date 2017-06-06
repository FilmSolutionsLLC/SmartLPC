(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectLabTasksDetailController', ProjectLabTasksDetailController);

    ProjectLabTasksDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ProjectLabTasks', 'Projects', 'Lookups', 'User'];

    function ProjectLabTasksDetailController($scope, $rootScope, $stateParams, entity, ProjectLabTasks, Projects, Lookups, User) {
        var vm = this;
        vm.projectLabTasks = entity;
        vm.load = function (id) {
            ProjectLabTasks.get({id: id}, function(result) {
                vm.projectLabTasks = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:projectLabTasksUpdate', function(event, result) {
            vm.projectLabTasks = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
