/**
 * Created by macbookpro on 6/20/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('IndividualPrivilegesController', IndividualPrivilegesController);
    IndividualPrivilegesController.$inject = ['$uibModal','id', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state', 'Projects','ContactPrivileges'];

    function IndividualPrivilegesController($uibModal,id, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state, Projects,ContactPrivileges) {
    	 var vm = this;


    	 console.log("Id got: "+id)
        vm.downloadType = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "ALL"}, {'id': 2, 'value': "Lock Approved"}];
        vm.exclusives = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "BASIC"}, {'id': 2, 'value': "MASTER"}];

         $http({
             method: 'GET',
             url: 'api/contact-privileges/'+id,
         }).then(function successCallback(response) {
        	 vm.contactPrivileges = response.data;
        	 console.log("Got from Individual privileges controller : "+JSON.stringify(vm.contactPrivileges));
         }, function errorCallback(response) {

         });
         vm.close = function () {

             $uibModalInstance.dismiss('cancel');
         };


        vm.openModal = function (elementID) {

            console.log("id of textbox : " + elementID);
            // var ctrl = angular.element(id).data('$ngModelController');

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
                console.log("null rootscope");

            } else {
                console.log("not null");

                vm.currrentOBJ = $rootScope.relationships;
                console.log("========> " + JSON.stringify(vm.currentOBJ));
                if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.execs_change')) {
                    console.log("found equal");

                    vm.contactPrivileges.contact = vm.currrentOBJ.data;

                    console.log(vm.contactPrivileges.contact.fullName);
                }
            }
        });



        vm.getAlbums = function (id) {
            console.log("id of textbox : " + id);
            // var ctrl = angular.element(id).data('$ngModelController');

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
            console.log("id of textbox : " + id);
            // var ctrl = angular.element(id).data('$ngModelController');

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
            console.log("id of textbox : " + id);
            // var ctrl = angular.element(id).data('$ngModelController');

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

        vm.defaultAlbum = {};
        vm.namedAlbumList = [];
        $rootScope.$watch(function() {
            return $rootScope.selected;
        }, function() {
            if ($rootScope.selected == null) {
                console.log("null rootscope");

            } else {
                vm.currrentOBJ = $rootScope.selected;
                if (angular.equals(vm.currrentOBJ.elementID, 'defaultAlbum')) {
                    vm.defaultAlbum = vm.currrentOBJ.data;
                    console.log("Saving inside default album list");
                    console.log("default : "+JSON.stringify(vm.defaultAlbum))
                }else if (angular.equals(vm.currrentOBJ.elementID, 'namedAlbum')) {
                    console.log("Saving inside named album list")
                    vm.namedAlbumList.push(vm.currrentOBJ.data);
                    console.log("named : "+JSON.stringify(vm.namedAlbumList))
                }
            }
        });


        vm.namedActorList = [];
        $rootScope.$watch(function() {
            return $rootScope.selectedTalent;
        }, function() {
            if ($rootScope.selectedTalent == null) {
                console.log("null rootscope");

            } else {
                vm.namedActorList.push($rootScope.selectedTalent);
                console.log("talent : "+JSON.stringify(vm.namedActorList));
            }
        });

        vm.removeAlbums = function (index) {
            vm.namedAlbumList.splice(index,1);
        };
        vm.removeActors = function (index) {
            vm.namedActorList.splice(index,1);
        };





        vm.save = function () {
            // save in album_permissions  Don't know what is 0 and 1 in permission column
            // save in contact_privileges
            console.log("=================================================================")
            console.log("Saving Contact Privileges : "+JSON.stringify(vm.contactPrivileges));



            console.log("Saving Default Album      : "+JSON.stringify(vm.defaultAlbum));
            console.log("Saving Named Album        : "+JSON.stringify(vm.namedAlbumList));
            console.log("Saving Named Actor        : "+JSON.stringify(vm.namedActorList));
            console.log("=================================================================")

            //making pojo to post
            vm.defaultAlbumPOST_LIST = [];
            vm.defaultAlbumPOST = {
                id :  null,
                album_id : vm.defaultAlbum.id,
                contact_id : vm.contactPrivileges.contact.id,
                permission : 0,
                name : vm.contactPrivileges.contact.username
            };
            vm.defaultAlbumPOST_LIST.push(vm.defaultAlbumPOST);


            for(var temp in vm.namedAlbumList){
                vm.namedAlbumPOST = {
                    id :  null,
                    album_id : vm.namedAlbumList[temp].id,
                    contact_id : vm.contactPrivileges.contact.id,
                    permission : 1,
                    name : vm.contactPrivileges.contact.username
                };
                vm.defaultAlbumPOST_LIST.push(vm.namedAlbumPOST);
            };

            console.log("======>  "+JSON.stringify(vm.defaultAlbumPOST_LIST));
            $http({
                method: 'POST',
                url: 'api/album/permissions',
                data: vm.defaultAlbumPOST_LIST
            }).then(function successCallback(response) {
                console.log("Saved Albums : "+response);
            }, function errorCallback(response) {
                console.log("Error Saving Albums")
            });

            ContactPrivileges.update(vm.contactPrivileges, onSaveSuccess, onSaveError);
        }
        var onSaveSuccess = function (result) {
            console.log("Saved Contact Privileges...",result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

    }
})();
