/**
 * Created by macbookpro on 2/16/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderOpenController', WorkOrderOpenController);

    WorkOrderOpenController.$inject = ['$http', '$scope', '$state'];

    function WorkOrderOpenController($http, $scope, $state) {
        var vm = this;

        vm.workOrders = [];
        $http({
            method: 'GET',
            url: 'api/reports/work-orders',
            params: {
                reportType: 'open_work_orders'
            }
        }).then(function (response) {
            vm.workOrders = response.data;
            console.log("total open workOrders : " + vm.workOrders.length);
            $scope.totalItems = vm.workOrders.length;
        });

        $scope.viewby = 20;

        $scope.currentPage = 1;
        $scope.itemsPerPage = $scope.viewby;
        $scope.maxSize = 5; //Number of pager buttons to show

        console.log("total items : " + $scope.totalItems);
        $scope.setPage = function (pageNo) {
            $scope.currentPage = pageNo;
        };

        $scope.pageChanged = function () {
            console.log('Page changed to: ' + $scope.currentPage);
        };

        $scope.setItemsPerPage = function (num) {
            $scope.itemsPerPage = num;
            $scope.currentPage = 1; //reset to first paghe
        };

    }
})();
