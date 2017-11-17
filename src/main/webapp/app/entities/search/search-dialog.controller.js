(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('SearchDialogController', SearchDialogController);

    SearchDialogController.$inject = ['$state', '$scope', '$stateParams', '$uibModalInstance', 'User', '$http', 'Contacts', 'Projects'];

    function SearchDialogController($state, $scope, $stateParams, $uibModalInstance, User, $http, Contacts, Projects) {
        var vm = this;

        vm.$state = $state;

        vm.users = User.query();

        vm.load = function (id) {
        };

        vm.contacts = [];
        vm.projects = [];

        vm.workOrderFilter = [
                {id: 'project',name: 'Project Name'},
                {id: 'id',name: 'Work Order ID'},
                {id: 'po',name: 'PO Record'},
                //{id: 'requestor',name: 'Requestor Name'},
                {id: 'desc',name: 'Work Description'},
                {id: 'invoice',name: 'Invoice'},
        ];
        vm.contactsProjectFilter = [ {id: 'contact',name: 'Contacts'}, {id: 'project',name: 'Projects'}, {id: 'all',name: 'All Tables'},]

        vm.contactProjectSearch  = function () {

            if(angular.equals(vm.typeCP.name,'Contacts')){
                console.log("Searching contact : "+vm.search.query);
                vm.searchContacts(vm.search.query);
            } else if(angular.equals(vm.typeCP.name,'Projects')){
                vm.searchProjects(vm.search.query);
            } else{
                vm.searchBoth(vm.search.query);
            }
        };

        vm.searchContacts = function (query) {

            console.log("query  : " + query);

            $uibModalInstance.dismiss('cancel');

            $state.transitionTo('search.display', {search: query, type: 'contacts' , filter: null})

        };

        vm.searchProjects = function (query) {
            console.log("searching projects" + query);
            $uibModalInstance.dismiss('cancel');
            $state.go('search.display', {search: query, type: 'projects' , filter: null})
        };

        vm.searchBoth = function (query) {
            console.log("searching contacts and projects" + query);
            $uibModalInstance.dismiss('cancel');
            $state.go('search.display', {search: query, type: 'all'})
        };

        vm.filterCriteria = '';
        vm.searchWorkOrder = function (query) {
            console.log("searching work order " + query);


            $uibModalInstance.dismiss('cancel');

            console.log("Searching workorder");
            console.log("Type : "+JSON.stringify(vm.type));
            console.log("Query : ",vm.search);

            $state.go('search.display', {search: query, type: 'workOrder', filter: vm.type.id})
        };

        var onSaveSuccess = function (result) {
            $uibModalInstance.close(result);
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
        };

        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
            window.history.back();
        };


    }
})();
