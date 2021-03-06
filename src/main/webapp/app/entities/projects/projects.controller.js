(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectsController', ProjectsController);

    ProjectsController.$inject = ['$http', '$scope', '$state', 'Projects', 'ProjectsSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function ProjectsController($http, $scope, $state, Projects, ProjectsSearch, ParseLinks, AlertService, pagingParams, paginationConstants) {
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

        function loadAll() {
            if (pagingParams.search) {
                ProjectsSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Projects.query({
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                console.log("sort by : " + result);
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            vm.z = [];

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.projectsDTO = data;
                console.log("ProjectsDTO : ", vm.projectsDTO);
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


        vm.delete = function (id) {
            var password = prompt("Enter password: ", "");

            if (password == null || password == "") {

            } else {
                $http({
                    method: 'POST',
                    url: 'api/project/delete',
                    data: password
                }).then(function successCallback(response) {

                    vm.success = response.data;
                    if (angular.equals(vm.success, 1)) {
                        alert("Project Delete Started : " + vm.projectsDTO.projects.id);
                    } else {
                        alert("Incorrect Password...");
                    }

                }, function errorCallback(response) {

                });
            }

            /*
			 * if(angular.equals(retVal,"abcd")){ alert("Password Correct :
			 * "+retVal); }else{ alert("Password Incorrect : "+retVal); }
			 */
        };
    }
})();
