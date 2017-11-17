/**
 * Created by macbookpro on 6/20/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('IndividualPrivilegesController', IndividualPrivilegesController);
    IndividualPrivilegesController.$inject = ['$uibModal', 'contact', 'project', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state', 'Projects', 'ContactPrivileges'];

    function IndividualPrivilegesController($uibModal, contact, project, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state, Projects, ContactPrivileges) {
        var vm = this;

        console.log(project);
        console.log(contact);
        vm.downloadType = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "ALL"}, {'id': 2, 'value': "Lock Approved"}];
        vm.exclusives = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "BASIC"}, {'id': 2, 'value': "MASTER"}];

        $http({
            method: 'GET',
            //url: 'api/contact-privileges/'+id,
            url: 'api/get/contact/project',
            params: {
                'projectID': project,
                'contactID': contact
            }
        }).then(function successCallback(response) {
            vm.contactPrivileges = response.data;
        }, function errorCallback(response) {

        });
        vm.close = function () {

            $uibModalInstance.dismiss('cancel');
        };


        vm.openModal = function (elementID) {
            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/contacts/simpleModal.html',
                controller: 'SimpleController',
                size: 'lg',
                scope: $scope,
                controllerAs: 'vm',
                backdrop: 'static',
                resolve: {
                    sendID: function () {
                        return elementID;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
        };

        $rootScope.$watch(function () {
            return $rootScope.relationships;
        }, function () {
            if ($rootScope.relationships == null) {

            } else {


                vm.currrentOBJ = $rootScope.relationships;

                if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.execs_change')) {
                    vm.contactPrivileges.contact = vm.currrentOBJ.data;
                }
            }
        });


        vm.getAlbums = function (id) {

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/select-album.html',
                controller: 'SelectAlbumController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    id: function () {
                        return id;
                    },
                    projectID: function () {
                        return vm.contactPrivileges.project.id;
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


        vm.namedAlbum = function (id) {

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/select-album.html',
                controller: 'SelectAlbumController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    id: function () {
                        return id;
                    },
                    projectID: function () {
                        return vm.contactPrivileges.project.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
        }

        vm.namedActor = function (id) {

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/select-actor.html',
                controller: 'SelectActorController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    id: function () {
                        return id;
                    },
                    projectID: function () {
                        return vm.contactPrivileges.project.id;
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

        vm.defaultAlbum = null;
        vm.namedAlbumList = [];
        $rootScope.$watch(function () {
            return $rootScope.selectedAlbum;
        }, function () {
            if ($rootScope.selectedAlbum == null) {

                console.log("selectedAlbum null ");
            } else {
                vm.currrentOBJ = $rootScope.selectedAlbum;
                if (angular.equals(vm.currrentOBJ.elementID, 'defaultAlbum')) {
                    vm.defaultAlbum = vm.currrentOBJ.data;

                } else if (angular.equals(vm.currrentOBJ.elementID, 'namedAlbum')) {

                    vm.namedAlbumList.push(vm.currrentOBJ.data);

                }
            }
        });


        vm.namedActorList = [];
        $rootScope.$watch(function () {
            return $rootScope.selectedTalent;
        }, function () {
            if ($rootScope.selectedTalent == null) {

                console.log("selectedTalent null ");
            } else {
                vm.namedActorList.push($rootScope.selectedTalent);

            }
        });

        vm.removeAlbums = function (index) {
            vm.namedAlbumList.splice(index, 1);
        };
        vm.removeActors = function (index) {
            vm.namedActorList.splice(index, 1);
        };


        vm.save = function () {

            vm.defaultAlbumPOST_LIST = [];

            if (vm.defaultAlbum == null) {

            } else {
                vm.defaultAlbumPOST = {
                    id: null,
                    album_id: vm.defaultAlbum.id,
                    contact_id: vm.contactPrivileges.contact.id,
                    permission: 0,
                    name: vm.contactPrivileges.contact.username
                };
                vm.defaultAlbumPOST_LIST.push(vm.defaultAlbumPOST);
                console.log("defaultAlbumPOST " + JSON.stringify(vm.defaultAlbumPOST));
                console.log("defaultAlbum " + JSON.stringify(vm.defaultAlbum));

            }


            //for(var temp in vm.namedAlbumList){
            if (vm.namedAlbumList.length > 0) {
                for (var i = 0; i < vm.namedAlbumList.length; i++) {
                    vm.namedAlbumPOST = {
                        id: null,
                        album_id: vm.namedAlbumList[i].id,
                        contact_id: vm.contactPrivileges.contact.id,
                        permission: 1,
                        name: vm.contactPrivileges.contact.username
                    };
                    vm.defaultAlbumPOST_LIST.push(vm.namedAlbumPOST);
                }
            }

            console.log(JSON.stringify(vm.defaultAlbumPOST_LIST));
            if (vm.defaultAlbumPOST_LIST.length > 0) {
                $http({
                    method: 'POST',
                    url: 'api/album/permissions',
                    data: vm.defaultAlbumPOST_LIST
                }).then(function successCallback(response) {
                }, function errorCallback(response) {
                });
            }

            ContactPrivileges.update(vm.contactPrivileges, onSaveSuccess, onSaveError);
        }
        var onSaveSuccess = function (result) {

            alert("Data Updated");
            $uibModalInstance.close(result);

        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

    }
})();
