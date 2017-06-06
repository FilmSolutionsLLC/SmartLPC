(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('BatchDetailController', BatchDetailController);

    BatchDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Batch', 'Projects', 'User'];

    function BatchDetailController($scope, $rootScope, $stateParams, entity, Batch, Projects, User) {
        var vm = this;
        vm.batch = entity;
        vm.load = function (id) {
            Batch.get({id: id}, function(result) {
                vm.batch = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:batchUpdate', function(event, result) {
            vm.batch = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
