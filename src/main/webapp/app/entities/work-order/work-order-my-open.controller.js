/**
 * Created by macbookpro on 2/16/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderMyOpenController', WorkOrderMyOpenController);

    WorkOrderMyOpenController.$inject = ['$http', '$scope', '$state'];

    function WorkOrderMyOpenController($http, $scope, $state) {
        var vm = this;

        console.log("My Open Work Order Controller");
        vm.workOrders = [];
        $http({
            method: 'GET',
            url: 'api/my-open/work-orders'
        }).then(function (response) {
            vm.workOrders = response.data;
            console.log("total my open workOrders : " + vm.workOrders.length);
            $scope.totalItems = vm.workOrders.length;
        });

        $scope.viewby = 10;

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
