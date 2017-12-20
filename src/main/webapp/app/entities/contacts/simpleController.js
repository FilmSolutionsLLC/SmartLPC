/**
 * Created by macbookpro on 12/27/16.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('SimpleController', SimpleController);
    SimpleController.$inject = ['$uibModal', 'sendID', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state'];

    function SimpleController($uibModal, sendID, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state) {


        $rootScope.owner = null;

        var vm = this;
        vm.loadAll = loadAll;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;

        vm.page = 0;
        vm.url = "api/contacts?page=0&sort=fullName,asc";
        vm.searchUrl = "api/_search/contacts?query=";

        vm.selectedContact = selectedContact;
        vm.elementID = sendID;
        vm.order = "asc";
        vm.sortBy = "fullName"
        vm.loadAll();


        vm.setSortOrder = function (field) {
            console.log("Sorting by : " + field);
            if (field === vm.sortBy) {
                if (vm.order === 'asc') {
                    vm.order = 'desc';
                } else {
                    vm.order = "asc";
                }

            } else {
                vm.order;

            }
            vm.sortBy = field;


            vm.url = "api/contacts?page=0&sort=".concat(field).concat(",").concat(vm.order);
            console.log("URL : " + vm.url);
            vm.searchUrl = "api/_search/contacts?query=";
            vm.loadAll();

        };

        function loadAll() {

            vm.contactsDTO = [];
            if (vm.currentSearch) {

                $http({
                    method: 'GET',
                    url: vm.searchUrl
                }).then(function successCallback(response) {
                    vm.contactsDTO = response.data;
                    vm.totalItems = response.headers('X-Total-Count');
                }, function errorCallback(response) {

                });
            } else {
                $http({
                    method: 'GET',
                    url: vm.url
                }).then(function mySuccess(response) {
                    vm.contactsDTO = response.data;
                    vm.totalItems =  response.headers('X-Total-Count');
                    vm.queryCount = vm.totalItems;

                    console.log("Headers: "+JSON.stringify(response));


                    console.log("vm.totalItems: "+(vm.totalItems));
                    console.log("queryCount: "+(vm.queryCount));
                    console.log("vm.page: "+(vm.page));

                }, function myError(response) {

                });
            }

            function sort() {

                console.log("Inside this ");
                var result = [vm.predicate + ','
                + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {


                vm.links = ParseLinks.parse(headers('link'));

                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.contacts = data;
                vm.page = pagingParams.page;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function transition() {


            console.log("Page: ", vm.page-1);
            vm.url = "api/contacts?page=" + (vm.page-1) + ("&sort=") + (vm.sortBy) + (',') + (vm.order);
            console.log("transition url : " + vm.url);
            vm.loadAll();
        }

        function search(searchQuery) {

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

        function selectedContact(contacts) {

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

        vm.addContact = function () {

            // var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/contacts/contacts-add-modal.html',
                controller: 'ContactsAddModalController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'xl',
                resolve: {
                    elementID: function () {
                        return vm.elementID;
                    },
                    entity: function () {
                        return {
                            // relatedContacts: null,
                            username : null,
                            password : null,
                            fullName : null,
                            title : '',
                            email : '',
                            email2 : '',
                            phoneOffice : '',
                            phoneAlternate : '',
                            phoneMobile : '',
                            phoneFax : '',
                            streetAddress : '',
                            streetAddress2 : '',
                            streetAddress3 : '',
                            city : '',
                            state : '',
                            zipcode : '',
                            country : '',
                            website : '',
                            notes : '',
                            sourceId : '',
                            createdDate : null,
                            updatedDate : null,
                            dashboard : false,
                            internalAccessOnly : false,
                            adhocExpiresIn : null,
                            adhocLimitViews : null,
                            adhocDownload : null,
                            adhocWatermarkText : null,
                            loginIp : null,
                            loginAttempt : 0,
                            attemptBasedLogin : true,
                            ipBasedLogin : false,
                            resetpassword : null,
                            companyContact : null,
                            createdByAdmin : null,
                            updatedByAdmin : null,
                            globalRestartColumns : 0,
                            globalRestartImagesPerPage : 0,
                            globalRestartImageSize : '',
                            globalRestartTime : null,
                            id : null
                        };
                    }/*,
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]*/
                }
                /*resolve: {
                    entity: ['ProjectRoles', function(ProjectRoles) {
                        return ProjectRoles.get({id : $stateParams.id}).$promise;
                    }]
                }*/
            });
        };


        vm.clearCurrent = function () {
            console.log("Remove Current");
            $scope.currentOBJ = {
                "elementID": vm.elementID,
                "data": null
            };

            $rootScope.relationships = $scope.currentOBJ;
            $uibModalInstance.dismiss('cancel');
        };
    }
})();




