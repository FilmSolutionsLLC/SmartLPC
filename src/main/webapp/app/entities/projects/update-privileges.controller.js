/**
 * Created by macbookpro on 12/27/16.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('UpdatePrivilegesController', UpdatePrivilegesController);
    UpdatePrivilegesController.$inject = ['id', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state', 'Projects'];

    function UpdatePrivilegesController(id, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state, Projects) {


        var vm = this;

        $http({
            method: 'GET',
            url: 'api/talents',
            params: {
                id: id,
                type: 'privileges'

            }
        }).then(function successCallback(response) {
            vm.privileges = response.data;
            console.log("Privileges : ");
            console.log(JSON.stringify(vm.privileges));
            $scope.totalItems = vm.privileges.length;
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


        vm.editPrivilege = function (id) {
            console.log("id of textbox : " + id);
            //var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/update-albums.html',
                controller: 'UpdateAlbumsController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    id: function () {
                        return id;
                    },
                    projectID: function () {
                        return vm.projectsDTO.projects.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
        };
    }
})();
