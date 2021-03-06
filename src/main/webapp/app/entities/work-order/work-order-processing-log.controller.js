(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderProcessingController', WorkOrderProcessingController);

    WorkOrderProcessingController.$inject = ['$http', '$scope', '$state', 'WorkOrderPorcessing', 'WorkOrderSearch', 'AlertService'];

    function WorkOrderProcessingController($http, $scope, $state, WorkOrderPorcessing, WorkOrderSearch, AlertService) {
        console.log("WorkOrderProcessingController");
        /*var vm = this;
        vm.loadAll = loadAll;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        vm.processed = false;
        vm.ingest = false;
        vm.loadAll();

        function loadAll() {
            if (pagingParams.search) {
                WorkOrderSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                WorkOrderPorcessing.query({
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.workOrders = data;
                console.log("1st entry "+JSON.stringify(vm.workOrders));
                vm.page = pagingParams.page;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search(searchQuery) {
            if (!searchQuery) {
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }

        $scope.myFilter = function (item) {
            return item.relation_type.relation === 'lab_processed';
        };*/

        var vm = this;


        vm.workOrders = [];

        vm.sort = function (type) {
            console.log("Sorting according to : " + type);
            if(vm.defaultSortType === "desc"){
                vm.defaultSortType = 'asc';
            }else{
                vm.defaultSortType = "desc";
            }


            $http({
                method: 'GET',
                url: 'api/processing/work-orders',
                params: {
                    'order': vm.defaultSortType,
                    'field': type
                }
            }).then(function (response) {
                vm.workOrders = response.data;
                console.log("total processing workOrders : " + vm.workOrders.length);
                console.log(JSON.stringify(vm.workOrders[0]));
                $scope.totalItems = vm.workOrders.length;

            });

            $scope.viewby = 15;

            $scope.currentPage = 1;
            $scope.itemsPerPage = $scope.viewby;
            $scope.maxSize = 5; // Number of pager buttons to show

            console.log("total items : " + $scope.totalItems);
            $scope.setPage = function (pageNo) {
                $scope.currentPage = pageNo;
            };

            $scope.pageChanged = function () {
                console.log('Page changed to: ' + $scope.currentPage);
            };

            $scope.setItemsPerPage = function (num) {
                $scope.itemsPerPage = num;
                $scope.currentPage = 1; // reset to first paghe
            };
        };

        vm.defaultSortType = 'asc';
        vm.defaultSortField = '1';

        vm.sort(vm.defaultSortField);



    }

})();
