/**
 * Created by macbookpro on 12/27/16.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('UpdateTagsController', UpdateTagsController);
    UpdateTagsController.$inject = ['projectID','$stateParams','$uibModal','id', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state', 'Projects'];

    function UpdateTagsController(projectID,$stateParams,$uibModal,id, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state, Projects) {


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


        vm.details = function (id) {
            console.log("Get Project Roles of ID: " + id);
        };


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

        vm.editTags = function (id) {
            console.log("id of project role : " + id);
            // var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/project-roles/project-roles-dialog.html',
                controller: 'ProjectRolesDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'md',
                resolve: {
                    id: function () {
                        return id;
                    },
                    entity: function () {
                        return null;
                    },
                    projectID: function () {
                        return projectID;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
                /*resolve: {
                    entity: ['ProjectRoles', function(ProjectRoles) {
                        return ProjectRoles.get({id : $stateParams.id}).$promise;
                    }]
                }*/
            });
        };
        vm.addTags = function () {
            console.log("Creating new Tag")
            // var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({
                templateUrl: 'app/entities/project-roles/project-roles-dialog.html',
                controller: 'ProjectRolesDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'md',
                resolve: {
                    entity: function () {
                        return {
                            project: null,
                            soloKillPct: null,
                            groupKillPct: null,
                            miniFullDt: null,
                            fullFinalDt: null,
                            disabled: null,
                            characterName: null,
                            startDate: null,
                            daysWorking: null,
                            excSologroup: null,
                            notes: null,
                            tagName: null,
                            hotkeyValue: null,
                            expireDate: null,
                            tertiaryKillPct: null,
                            createdDate: null,
                            updatedDate: null,
                            id: null
                        };
                    },
                    id: function () {
                        return null;
                    },
                    projectID: function () {
                        return projectID;
                    }
                }
            });
        };
    }

})();
