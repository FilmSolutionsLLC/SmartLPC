(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectPurchaseOrdersDetailController', ProjectPurchaseOrdersDetailController);

    ProjectPurchaseOrdersDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ProjectPurchaseOrders', 'Projects', 'User'];

    function ProjectPurchaseOrdersDetailController($scope, $rootScope, $stateParams, entity, ProjectPurchaseOrders, Projects, User) {
        var vm = this;
        vm.projectPurchaseOrders = entity;
        vm.load = function (id) {
            ProjectPurchaseOrders.get({id: id}, function(result) {
                vm.projectPurchaseOrders = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:projectPurchaseOrdersUpdate', function(event, result) {
            vm.projectPurchaseOrders = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
