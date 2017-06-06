(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderDetailController', WorkOrderDetailController);

    WorkOrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'WorkOrder', 'Lookups', 'Projects', 'User', 'Contacts'];

    function WorkOrderDetailController($scope, $rootScope, $stateParams, entity, WorkOrder, Lookups, Projects, User, Contacts) {
        var vm = this;
        vm.workOrderDTO = entity;
        console.log(JSON.stringify(vm.workOrderDTO));

        /*console.log(JSON.stringify(vm.workOrderDTO));
        vm.workOrder = vm.workOrderDTO.workOrder;
        vm.workOrderAbcFiles = vm.workOrderDTO.workOrderAbcFiles;
        vm.workOrderAbcHdds = vm.workOrderDTO.workOrderAbcHdds;
        vm.workOrdersAdminRelations = vm.workOrderDTO.workOrdersAdminRelations;
        console.log(" ===== > " + JSON.stringify(vm.workOrder));
        console.log(" ===== > " + JSON.stringify(vm.workOrderAbcFiles));
        console.log(" ===== > " + JSON.stringify(vm.workOrderAbcHdds));
        console.log(" ===== > " + JSON.stringify(vm.workOrdersAdminRelations));

        Projects.get({id: vm.workOrderDTO.workOrder.project.id}, function (result) {
            vm.projectsDTO = result;
            vm.purchaseOrders = vm.projectsDTO.projectPurchaseOrderses;
            vm.labs = vm.projectsDTO.projectLabTaskses;

        });
        console.log(" ===== > " + JSON.stringify(vm.workOrder));
        console.log(" ===== > " + JSON.stringify(vm.workOrderAbcFiles));
        console.log(" ===== > " + JSON.stringify(vm.workOrderAbcHdds));
        console.log(" ===== > " + JSON.stringify(vm.workOrdersAdminRelations));

        vm.load = function (id) {
            WorkOrder.get({id: id}, function (result) {
                vm.workOrderDTO = result;
                vm.workOrder = vm.workOrderDTO.workOrder;
                vm.workOrderAbcFiles = vm.workOrderDTO.workOrderAbcFiles;
                vm.workOrderAbcHdds = vm.workOrderDTO.workOrderAbcHdds;
                vm.workOrdersAdminRelations = vm.workOrderDTO.workOrdersAdminRelations;
                Projects.get({id: vm.workOrderDTO.workOrder.project.id}, function (result) {
                    vm.projectsDTO = result;
                    vm.projects = vm.projectsDTO.projects;
                    vm.purchaseOrders = vm.projectsDTO.projectPurchaseOrderses;
                    vm.labs = vm.projectsDTO.projectLabTaskses;
                });
                if (angular.equals(vm.workOrderDTO.workOrder.type.textValue, 'PKO')) {
                    vm.showPKOFlag = true;
                }
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:workOrderUpdate', function (event, result) {
            vm.workOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
*/
    }
})();
