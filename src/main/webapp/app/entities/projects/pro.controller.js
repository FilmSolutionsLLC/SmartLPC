(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProController', ProController);

    ProController.$inject = ['ProjectsList','$http', '$scope', '$state', 'Projects', 'ProjectsSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function ProController(ProjectsList,$http, $scope, $state, Projects, ProjectsSearch, ParseLinks, AlertService, pagingParams, paginationConstants) {
        var vm = this;
        vm.loadAll = loadAll;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        vm.loadAll();
        vm.projects = [];
        vm.mainContactFlag = false;
        vm.mainUnitPublicistFlag = false;
        vm.url = "api/getall/projects";

/*
        $http({
            url: 'api/pro',
            method: 'GET',

            page: pagingParams.page - 1,
            size: paginationConstants.itemsPerPage


        }).success(function (data,status, headers) {
            vm.projects = data;

            console.log("header link : " + headers()['link']);
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;

            console.log("vm.projects : " + vm.projects.length);
            vm.page = pagingParams.page;
        }).error(function (data,status, headers) {
            console.log("data not recieved");
        });
*/


        function loadAll() {
            if (pagingParams.search) {
                ProjectsSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                console.log("in else");
                ProjectsList.query({
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== '1') {
                    result.push('1');
                }
                return result;
            }

            vm.z = [];
            function onSuccess(data, headers) {
                console.log("http get success");
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
               // vm.projects = data;
                console.log("vm.projects : " + vm.projects.length);
                vm.page = pagingParams.page;
            }

            function onError(data) {
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
            vm.predicate = 'name';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }

    }
})();
