/**
 * Created by macbookpro on 12/27/16.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('UpdateTagsController', UpdateTagsController);
    UpdateTagsController.$inject = ['id', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state', 'Projects'];

    function UpdateTagsController(id, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state, Projects) {


        var vm = this;

        $http({
            method: 'GET',
            url: 'api/talents',
            params: {
                id: id,
                type: 'tags'

            }
        }).then(function successCallback(response) {
            vm.tagss = response.data;
            console.log("TAGS : ");
            console.log(JSON.stringify(vm.tagss));
            $scope.totalItems = vm.tagss.length;
        }, function errorCallback(response) {

        });


        vm.close = function () {

            $uibModalInstance.dismiss('cancel');
        };

        $scope.viewby = 10;
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
    }
})();
