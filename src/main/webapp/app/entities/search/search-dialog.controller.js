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

       
        vm.searchContacts = function (query) {

            console.log("query  : " + query);

            $uibModalInstance.dismiss('cancel');

            $state.transitionTo('search.display', {search: query, type: 'contacts'})

        };

        vm.searchProjects = function (query) {
            console.log("searching projects" + query);
            $uibModalInstance.dismiss('cancel');
            $state.go('search.display', {search: query, type: 'projects'})
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

            $state.go('search.display', {search: query, type: 'workOrder'})
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
        };


    }
})();
