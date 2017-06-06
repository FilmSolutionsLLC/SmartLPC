/**
 * Created by macbookpro on 12/27/16.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('SimpleController', SimpleController);
    SimpleController.$inject = ['sendID', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state'];

    function SimpleController(sendID, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state) {


        $rootScope.owner = null;
//ths

        var vm = this;
        vm.loadAll = loadAll;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;

        vm.url = "api/contacts?page=0";
        vm.searchUrl = "api/_search/contacts?query=";
        console.log(" URL : " + vm.url);
        //vm.selectChecked = selectChecked;
        //vm.addSelected = addSelected;
        //vm.removeSelected = removeSelected;
        vm.selectedContact = selectedContact;
        vm.elementID = sendID;
        console.log("data got form contact : " + vm.elementID);

        vm.loadAll();


        function loadAll() {
            // $scope.data = [];
            vm.contactsDTO = [];
            if (vm.currentSearch) {
                console.log("search invoked");
                console.log("current search : " + vm.currentSearch);
                console.log("current search URL : " + vm.searchUrl);
                $http({
                    method: 'GET',
                    url: vm.searchUrl
                }).then(function successCallback(response) {

                    vm.contactsDTO = response.data;
                    console.log("Http search called");

                    vm.totalItems = response.headers('X-Total-Count');
                    console.log("length : " + vm.totalItems);

                }, function errorCallback(response) {
                    console.log("error in getting data.");
                });
            } else {
                $http({
                    method: 'GET',
                    url: vm.url
                }).then(function successCallback(response) {

                    vm.contactsDTO = response.data;
                    console.log("Http GET called");

                    vm.totalItems = response.headers('X-Total-Count');
                    console.log("length : " + vm.totalItems);

                }, function errorCallback(response) {
                    console.log("error in getting data.");
                });
            }
        }


        function transition() {

            console.log("page Number : " + vm.page);
            vm.url = "api/contacts?page=" + vm.page;
            console.log(" URL : " + vm.url);
            vm.loadAll();
        }

        function search(searchQuery) {
            console.log("search query called..");
            if (!searchQuery) {
                return vm.clear();
            }
            vm.page = 0;
            vm.currentSearch = searchQuery;
            vm.searchUrl = "api/_search/contacts?query=" + vm.currentSearch;


            vm.transition();
        }

        function clear() {
            vm.page = 0;
            vm.currentSearch = null;
            vm.searchQuery = null;
            vm.transition();
        }

        $rootScope.relatedContacts = [];
        function selectedContact(contacts){
            console.log("contact selected : "+contacts.id);
            $scope.currentOBJ = {
                "elementID": vm.elementID,
                "data": contacts
            };

            $rootScope.relationships = $scope.currentOBJ;
            $uibModalInstance.dismiss('cancel');

        };


        vm.close = function () {

            $uibModalInstance.dismiss('cancel');
        };







































































        /*


                vm.search = function (searchQuery) {
                    console.log("search query : " + searchQuery);
                    $scope.data = [];

                    $http({
                        method: 'GET',
                        url: '/api/_search/contacts',
                        params: {query: searchQuery}
                    }).then(function successCallback(response) {
                        $scope.data = response.data;
                        console.log("got data");
                        console.log(JSON.stringify($scope.contacts));
                        $scope.totalItems = $scope.data.length;

                        console.log("length : " + $scope.totalItems);

                    }, function errorCallback(response) {
                        console.log("error in getting data.");
                    });
                };

                vm.elementID = sendID;
                console.log("data got form contact : " + vm.elementID);

                $scope.data = [];
                $http({
                    method: 'GET',
                    url: '/api/contacts/get/getAll'
                }).then(function successCallback(response) {
                    $scope.data = response.data;
                    console.log("got data");
                    //console.log(JSON.stringify($scope.data));
                    $scope.totalItems = $scope.data.length;

                    console.log("length : " + $scope.totalItems);

                }, function errorCallback(response) {
                    console.log("error in getting data.");
                });

                $scope.viewby = 5;
                $scope.totalItems = $scope.data.length;
                $scope.currentPage = 1;
                $scope.itemsPerPage = $scope.viewby;
                $scope.maxSize = 5; //Number of pager buttons to show

                $scope.setPage = function (pageNo) {
                    $scope.currentPage = pageNo;
                };
                vm.clear = function () {
                    $uibModalInstance.dismiss('cancel');
                };

                $scope.pageChanged = function () {
                    console.log('Page changed to: ' + $scope.currentPage);
                };

                $scope.setItemsPerPage = function (num) {
                    $scope.itemsPerPage = num;
                    $scope.currentPage = 1; //reset to first paghe
                };


                $rootScope.relatedContacts = [];
                vm.selectedContact = function (id) {

                    Contacts.get({id: id.id}, function (result) {
                        vm.companies = result.contacts;
                        $scope.currentOBJ = {
                            "elementID": vm.elementID,
                            "data": result.contacts
                        };
                        //    $rootScope.relatedContacts.push();
                        //$rootScope.relatedContacts.push(vm.companies);
                        //$rootScope.relatedContact = vm.relatedContacts;
                        //$rootScope.owner = vm.companies.fullName;

                        $rootScope.relationships = $scope.currentOBJ;

                        //console.log(JSON.stringify(vm.companies));

                        // console.log("....total related contacts : " + $rootScope.relatedContacts.length);

                        // console.log("owner : " + JSON.stringify($rootScope.owner));


                    });
                    $uibModalInstance.dismiss('cancel');


                    //$uibModalInstance.close({xxx: 'rohan'});
                };

        */

    }
})();
