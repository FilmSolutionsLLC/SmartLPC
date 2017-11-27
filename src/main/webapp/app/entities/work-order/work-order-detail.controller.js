(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderDetailController', WorkOrderDetailController);

    WorkOrderDetailController.$inject = ['$http','$scope', '$rootScope', '$stateParams', 'entity', 'WorkOrder', 'Lookups', 'Projects', 'User', 'Contacts'];

    function WorkOrderDetailController( $http,$scope, $rootScope, $stateParams, entity, WorkOrder, Lookups, Projects, User, Contacts) {
        var vm = this;
        vm.workOrderDTO = entity;
        console.log(JSON.stringify(vm.workOrderDTO));
        vm.purchaseOrders = [];
        if(angular.equals(vm.workOrderDTO.workOrder.project,null)) {

        }else{
            $http({
                method: 'GET',
                url: 'api/project-purchase-orders/projects/' + vm.workOrderDTO.workOrder.project.id
            }).then(function (response) {
                vm.purchaseOrders = response.data;
                console.log("project purchaseOrders : " + JSON.stringify(vm.purchaseOrders));
            });


            $http({
                method: 'GET',
                url: 'api/project-lab-tasks/projects/' + vm.workOrderDTO.workOrder.project.id
            }).then(function (response) {
                vm.labs = response.data;
                console.log("project LAB Tasks : " + JSON.stringify(vm.labs));
            });
        }

        vm.prevNext = {};
        $http({
            method: 'GET',
            url: 'api/prev/next/work-orders/' + vm.workOrderDTO.workOrder.id

        }).then(function successCallback(response) {
            vm.prevNext = response.data;
            console.log("Prev Next workOrders : " + JSON.stringify(vm.prevNext));
        }, function errorCallback(response) {

        });


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
