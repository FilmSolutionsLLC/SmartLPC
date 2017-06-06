(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderAbcFileDetailController', WorkOrderAbcFileDetailController);

    WorkOrderAbcFileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'WorkOrderAbcFile', 'WorkOrder', 'Lookups'];

    function WorkOrderAbcFileDetailController($scope, $rootScope, $stateParams, entity, WorkOrderAbcFile, WorkOrder, Lookups) {
        var vm = this;
        vm.workOrderAbcFile = entity;
        vm.load = function (id) {
            WorkOrderAbcFile.get({id: id}, function(result) {
                vm.workOrderAbcFile = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:workOrderAbcFileUpdate', function(event, result) {
            vm.workOrderAbcFile = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
