/**
 * Created by macbookpro on 2/16/17.
 */
(function() {
    'use strict';

    angular.module('smartLpcApp').controller('MultipleWorkOrderReports',
        MultipleWorkOrderReports);

    MultipleWorkOrderReports.$inject = ['$stateParams','user','$http', '$scope', '$state' ];

    function MultipleWorkOrderReports($stateParams,user, $http, $scope, $state) {

        var vm = this;



        console.log("got data here : "+JSON.stringify(user));

        vm.sortOrder = 'asc';



        vm.workOrders9 = [];
        vm.workOrders4 = [];

        // GET MY OPEN
        vm.getMyOpen = function () {

            console.log("My Open Work Order ");

            $http({
                method: 'GET',
                url: 'api/my-open/work-orders'
            }).then(function (response) {
                vm.workOrders9 = response.data;
                console.log("total my open workOrders : " + vm.workOrders9.length);
                $scope.totalItems = vm.workOrders9.length;
            });

            $scope.viewby = 5; //number of elements to show

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
        };

        // GET PROCESSING
        vm.getProcessingLog = function (type) {

            console.log("Sorting according to : " + type);
            if(vm.sortOrder === "desc"){
                vm.sortOrder = 'asc';
            }else{
                vm.sortOrder = "desc";
            }


            $http({
                method: 'GET',
                url: 'api/processing/work-orders',
                params: {
                    'order': vm.sortOrder,
                    'field': type
                }
            }).then(function (response) {
                vm.workOrders4 = response.data;
                console.log("total processing workOrders : " + vm.workOrders4.length);
                console.log(JSON.stringify(vm.workOrders4[0]));
                $scope.totalItems2 = vm.workOrders4.length;

            });

            $scope.viewby2 = 5; //number of elements to show

            $scope.currentPage2 = 1;
            $scope.itemsPerPage2 = $scope.viewby2;
            $scope.maxSize2 = 5; // Number of pager buttons to show

            console.log("total items : " + $scope.totalItems2);
            $scope.setPage = function (pageNo2) {
                $scope.currentPage2 = pageNo2;
            };

            $scope.pageChanged = function () {
                console.log('Page changed to: ' + $scope.currentPage2);
            };

            $scope.setItemsPerPage = function (num2) {
                $scope.itemsPerPage2 = num2;
                $scope.currentPage2 = 1; // reset to first page
            };
        };



        vm.getMyOpen();
        vm.getProcessingLog(1);


    }
})();
