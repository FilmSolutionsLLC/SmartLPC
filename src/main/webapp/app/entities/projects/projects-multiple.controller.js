/**
 * Created by macbookpro on 1/12/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('MultiProjectController', MultiProjectController);

    MultiProjectController.$inject = ['$http', '$uibModal', '$rootScope', '$rootScope', '$scope', '$state', 'Contacts', 'ContactsSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function MultiProjectController($http, $uibModal, $rootScope, $scope, $state, Contacts, ContactsSearch, ParseLinks, AlertService, pagingParams, paginationConstants) {
        var vm = this;

        vm.loadAll = loadAll;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;

        vm.url = "api/contacts?page=0";
        vm.searchUrl = "api/_search/contacts?query=";
        console.log(" URL : " + vm.url);
        vm.selectChecked = selectChecked;
        vm.addSelected = addSelected;
        vm.removeSelected = removeSelected;
        vm.openModalProject = openModalProject;
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

        vm.selectedContacts = [];
        vm.selectedCheckBox = null;
        function selectChecked(contacts) {
            if (vm.selectedContacts.indexOf(contacts) == -1) {
                vm.selectedContacts.push(contacts);
                console.log("Checking  :  " + vm.selectedCheckBox[contacts.id]);
                console.log("contact selected : " + contacts.id);
            } else {

                console.log("item already exists");
                vm.selectedContacts.splice(vm.selectedContacts.indexOf(contacts), 1);
            }
        };

        function addSelected() {

            console.log("Length : " + vm.selectedContacts.length);
        };
        function removeSelected(index) {

            console.log("removing id : " + vm.selectedCheckBox[vm.selectedContacts[index].id]);
            vm.selectedCheckBox[vm.selectedContacts[index].id] = false;
            vm.selectedContacts.splice(index, 1);

        };

        $rootScope.savedContact = null;
        function addSelected() {
            console.log("adding selected contacts");
            $rootScope.savedContact = vm.selectedContacts;
            openModalProject();
        };

        function openModalProject() {
            console.log("opening modal");
            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/contacts/projectsList.html',
                controller: 'ProjectListController',
                size: 'xl',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    isMultiExecAdd: function () {
                        return true;
                    }
                },
            });
        };


        vm.searchFilter = function () {

          console.log("Filter Query : "+vm.filter);
            $http({
                method: 'GET',
                //url: 'api/disableproject/' + vm.projectsDTO.projects.id,
                url: 'api/search/contact/project',
                params: {
                    query: vm.filter
                }
            }).then(function successCallback(response) {
                vm.contactsDTO = response.data;
                vm.totalItems = vm.contactsDTO.length;

            }, function errorCallback(response) {

            });
        };

        vm.selectAll = function () {
            vm.selectedContacts.push(vm.contactsDTO);
        };
    }
})();
