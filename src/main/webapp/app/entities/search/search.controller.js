(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('SearchController', SearchController);

    SearchController.$inject = ['WorkOrderSearch', '$stateParams', 'translatePartialLoader', '$http', '$scope', '$state', 'Contacts', 'ContactsSearch', 'AlertService'];

    function SearchController(WorkOrderSearch, $stateParams, translatePartialLoader, $http, $scope, $state, Contacts, ContactsSearch, AlertService) {
        var vm = this;

        vm.search = $stateParams.search;
        vm.searchType = $stateParams.type;
        vm.filter = $stateParams.filter;
        console.log("state param query : " + vm.search);
        console.log("search type       : " + vm.searchType);
        console.log("search filter       : " + (vm.filter));
        vm.contacts = [];
        vm.projects = [];
        vm.workOrders = [];
        if ((vm.searchType == 'contacts') || (vm.searchType == 'all')) {
            console.log("searching contacts");
            $http({
                method: 'GET',
                url: 'api/_search/contacts',
                params: {query: vm.search}
            }).then(function successCallback(response) {
                vm.contacts = response.data;
                //console.log("got data");
                //console.log(JSON.stringify($scope.contacts));
                $scope.totalItems = vm.contacts.length;

                // console.log("length : " + $scope.totalItems);

            }, function errorCallback(response) {
                console.log("error in getting data.");
            });

        }
        if ((vm.searchType == 'projects') || (vm.searchType == 'all')) {
            console.log("searching projects");
            $http({
                method: 'GET',
                url: 'api/_search/projects',
                params: {query: vm.search}
            }).then(function successCallback(response) {

                /*   $scope.projects = response.data;

                 console.log("got data");
                 console.log(JSON.stringify($scope.projects));
                 $scope.totalItems = $scope.projects.length;
                 console.log("length : " + $scope.totalItems);
                 */
                vm.projectsDTO = response.data;

                // console.log(JSON.stringify(vm.projectsDTO));
                /*
                 for (var i = 0; i < vm.projectsDTO.length; i++) {

                 // console.log("======== > ");
                 // console.log(JSON.stringify(vm.projectsDTO[i].projects));
                 //vm.projects.push(vm.projectsDTO[i].projects);
                 vm.x = {
                 "projectRoles": vm.projectsDTO[i].projectRoles,
                 "id": vm.projectsDTO[i].projects.id,
                 "name": vm.projectsDTO[i].projects.name,
                 "status": vm.projectsDTO[i].projects.status
                 };
                 vm.projects.push(vm.x);
                 // console.log(JSON.stringify(vm.projects));

                 }*/

                if (vm.contacts.length < vm.projectsDTO.length) {
                    console.log("GREATER projects length  : " + vm.projectsDTO.length);
                    console.log("contacts length  : " + vm.contacts.length);
                    $scope.totalItems = vm.projectsDTO.length;
                } else {
                    console.log("SMALLER projects length  : " + vm.projectsDTO.length);
                    console.log("contacts length  : " + vm.contacts.length);
                    $scope.totalItems = vm.contacts.length;
                }
                // $scope.totalItems = vm.projects.length;
                // console.log("projects length  : " + vm.projectsDTO.length);
                // console.log("contacts length  : " + vm.contacts.length);
                console.log("totalLength  : " + $scope.totalItems);

            }, function errorCallback(response) {
                console.log("error in getting data.");
            });

        }

        if (vm.searchType == 'workOrder') {
            console.log("search work orders");
            /*     WorkOrderSearch.query({
                     query: vm.search,
                     size: 100000
                 }, onSuccess, onError);
     */


            $http({
                url: "api/search/work-orders/various",
                method: "GET",
                params: {
                    type: vm.filter,
                    query: vm.search
                }
            }).then(function successCallback(response) {
                for (var i = 0; i < response.data.length; i++) {
                    vm.workOrders.push(response.data[i]);
                    console.log("added" + i);
                }
                $scope.totalItemsWO = vm.workOrders.length;

                console.log(" ==> "+JSON.stringify(vm.workOrders));
            }, function errorCallback(response) {
            });
        }

        function onSuccess(data, headers) {

            for (var i = 0; i < data.length; i++) {
                vm.workOrders.push(data[i]);
                console.log("added" + i);
            }
            $scope.totalItemsWO = vm.workOrders.length;
        };

        function onError(error) {
            // AlertService.error(error.data.message);
        }


// view total items in list depending on who is larger

        $scope.viewby = 4;
        //$scope.totalItems = null;
        $scope.currentPage = 1;
        $scope.itemsPerPage = $scope.viewby;
        $scope.maxSize = 5; //Number of pager buttons to show


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
        vm.relatedContacts = [];
        vm.selectedContact = function (compid) {

            // alert("Company Id is "+compid.id);

            Contacts.get({id: compid.id}, function (result) {
                vm.searchResult = result;

                console.log("search : " + JSON.stringify($rootScope.searchResult));


            });
        };

    }
})();
