(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderAbcHddDetailController', WorkOrderAbcHddDetailController);

    WorkOrderAbcHddDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'WorkOrderAbcHdd', 'WorkOrder', 'Lookups'];

    function WorkOrderAbcHddDetailController($scope, $rootScope, $stateParams, entity, WorkOrderAbcHdd, WorkOrder, Lookups) {
        var vm = this;
        vm.workOrderAbcHdd = entity;
        vm.load = function (id) {
            WorkOrderAbcHdd.get({id: id}, function(result) {
                vm.workOrderAbcHdd = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:workOrderAbcHddUpdate', function(event, result) {
            vm.workOrderAbcHdd = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
