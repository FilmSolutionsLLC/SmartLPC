(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ContactsSelectController', ContactsSelectController);

    ContactsSelectController.$inject = ['$scope', '$state','Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function ContactsSelectController ($scope, $state, Contacts, Lookups, Departments, User, ContactsSearch, ParseLinks, AlertService, pagingParams, paginationConstants) {
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

        vm.companies = null;

        function loadAll () {
            if (pagingParams.search) {
                ContactsSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Contacts.query({
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
                vm.contacts = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear () {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
            //$uibModalInstance.dismiss('cancel');
        }

        vm.relatedContacts = [];

        vm.selectedContact = function (compid) {

           // alert("Company Id is "+compid.id);

            Contacts.get({id: compid.id}, function (result) {
                vm.companies = result;
                vm.relatedContacts.push(vm.companies);
                console.log(JSON.stringify(vm.companies));

                console.log("....total related contacts : "+vm.relatedContacts.length);

            });
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
