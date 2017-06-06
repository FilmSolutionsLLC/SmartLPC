(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrdersAdminRelationDetailController', WorkOrdersAdminRelationDetailController);

    WorkOrdersAdminRelationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'WorkOrdersAdminRelation', 'WorkOrder', 'User', 'RelationType'];

    function WorkOrdersAdminRelationDetailController($scope, $rootScope, $stateParams, entity, WorkOrdersAdminRelation, WorkOrder, User, RelationType) {
        var vm = this;
        vm.workOrdersAdminRelation = entity;
        vm.load = function (id) {
            WorkOrdersAdminRelation.get({id: id}, function(result) {
                vm.workOrdersAdminRelation = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:workOrdersAdminRelationUpdate', function(event, result) {
            vm.workOrdersAdminRelation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
