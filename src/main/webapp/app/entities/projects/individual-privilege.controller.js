/**
 * Created by macbookpro on 6/20/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('IndividualPrivilegesController', IndividualPrivilegesController);
    IndividualPrivilegesController.$inject = ['$ngConfirm','$uibModal', 'contact', 'project', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state', 'Projects', 'ContactPrivileges'];

    function IndividualPrivilegesController($ngConfirm,$uibModal, contact, project, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state, Projects, ContactPrivileges) {
        var vm = this;

        console.log(project);
        console.log(contact);
        vm.downloadType = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "ALL"}, {'id': 2, 'value': "Lock Approved"}];
        vm.exclusives = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "BASIC"}, {'id': 2, 'value': "MASTER"}];

        vm.namedAlbumList = [];
        vm.namedAlbumListSaving = [];

        vm.namedActorList = [];
        vm.namedActorListSaving = [];


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
            if(vm.contactPrivileges.contactPrivileges.defaultAlbum !== null) {
                vm.defaultAlbum = {
                    'id': vm.contactPrivileges.contactPrivileges.defaultAlbum.id,
                    'type': vm.contactPrivileges.contactPrivileges.defaultAlbum.album_name,
                    'value': vm.contactPrivileges.contactPrivileges.defaultAlbum.album_descriptions
                };
            }
            if(vm.contactPrivileges.contactPrivilegeAlbums !== null) {
                for (var i = 0; i < vm.contactPrivileges.contactPrivilegeAlbums.length; i++) {

                    vm.temp = {
                        'id': vm.contactPrivileges.contactPrivilegeAlbums[i].albumNodeID,
                        'type': vm.contactPrivileges.contactPrivilegeAlbums[i].albumID,
                        'value': vm.contactPrivileges.contactPrivilegeAlbums[i].albumID
                    };
                    vm.namedAlbumList.push(vm.temp);
                    console.log("vm.namedAlbumList : " + JSON.stringify(vm.namedAlbumList));
                }
            }
            if(vm.contactPrivileges.contactPrivilegeReviewers !== null) {
                for (var i = 0; i < vm.contactPrivileges.contactPrivilegeReviewers.length; i++) {
                    vm.temp = {
                        'id': vm.contactPrivileges.contactPrivilegeReviewers[i].tagID,
                        'type': vm.contactPrivileges.contactPrivilegeReviewers[i].reviewer,
                        'value': vm.contactPrivileges.contactPrivilegeReviewers[i].reviewer
                    };
                    vm.namedActorList.push(vm.temp);

                }
            }
            console.log("data  : " + JSON.stringify(vm.contactPrivileges));
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
                    vm.contactPrivileges.contactPrivileges.contact = vm.currrentOBJ.data;
                    $rootScope.relationships = null;
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
                        return vm.contactPrivileges.contactPrivileges.project.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            }).result.then(function (result) {
                vm.selectedAlbum = result;
                console.log("Selected Album returned from modal box : "+JSON.stringify(vm.selectedAlbum));
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
                        return vm.contactPrivileges.contactPrivileges.project.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            }).result.then(function (result) {
                vm.currrentOBJ = result;
                console.log("Selected Album returned from modal box : "+JSON.stringify(vm.currrentOBJ));
                if (angular.equals(vm.currrentOBJ.elementID, 'defaultAlbum')) {
                    vm.defaultAlbum = vm.currrentOBJ.data;
                    vm.temp = {
                        id: vm.defaultAlbum.id
                    };
                    console.log("Default Album : " + JSON.stringify(vm.defaultAlbum));
                    vm.contactPrivileges.contactPrivileges.defaultAlbum = vm.temp;
                } else if (angular.equals(vm.currrentOBJ.elementID, 'namedAlbum')) {

                    vm.namedAlbumList.push(vm.currrentOBJ.data);

                    console.log("Named Album : " + JSON.stringify(vm.currrentOBJ.data));


                    vm.temp = {
                        'contactPrivilegeID': vm.contactPrivileges.contactPrivileges.id,
                        'albumID': vm.currrentOBJ.data.type,
                        'albumNodeID': vm.currrentOBJ.data.id
                    };
                    vm.namedAlbumListSaving.push(vm.temp);
                    vm.contactPrivileges.contactPrivilegeAlbums.push(vm.temp);

                }

            });
        };

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
                        return vm.contactPrivileges.contactPrivileges.project.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            }).result.then(function (result) {
                vm.currrentOBJ = result;
                console.log("Selected actor returned from modal box : " + JSON.stringify(vm.currrentOBJ));
                console.log("Adding actor");


                vm.namedActorList.push(vm.currrentOBJ);

                vm.temp = {
                    'contactPrivilegeID': vm.contactPrivileges.contactPrivileges.id,
                    'reviewer': vm.currrentOBJ.type,
                    'tagID': vm.currrentOBJ.id
                };

                vm.contactPrivileges.contactPrivilegeReviewers.push(vm.temp);
                vm.namedActorListSaving.push(vm.temp);

                console.log("Added actor....");
            });
        };

        //vm.defaultAlbum = null;


        /*$rootScope.$watch(function () {
            return $rootScope.selectedAlbum;
        }, function () {
            if ($rootScope.selectedAlbum == null) {

                console.log("selectedAlbum null ");
            } else {
                vm.currrentOBJ = $rootScope.selectedAlbum;
                //$rootScope.selectedAlbum = {};
                if (angular.equals(vm.currrentOBJ.elementID, 'defaultAlbum')) {
                    vm.defaultAlbum = vm.currrentOBJ.data;
                    vm.temp = {
                        id: vm.defaultAlbum.id
                    };
                    console.log("Default Album : " + JSON.stringify(vm.defaultAlbum));
                    vm.contactPrivileges.contactPrivileges.defaultAlbum = vm.temp;
                } else if (angular.equals(vm.currrentOBJ.elementID, 'namedAlbum')) {

                    vm.namedAlbumList.push(vm.currrentOBJ.data);

                    console.log("Named Album : " + JSON.stringify(vm.currrentOBJ.data));


                    vm.temp = {
                        'contactPrivilegeID': vm.contactPrivileges.contactPrivileges.id,
                        'albumID': vm.currrentOBJ.data.type,
                        'albumNodeID': vm.currrentOBJ.data.id
                    };
                    vm.namedAlbumListSaving.push(vm.temp);
                    vm.contactPrivileges.contactPrivilegeAlbums.push(vm.temp);

                }

            }
        });*/


        /*$rootScope.$watch(function () {
            return $rootScope.selectedTalent;
        }, function () {
            if ($rootScope.selectedTalent == null) {

                console.log("selectedTalent null ");
            } else {
                console.log("Adding actor");
                console.log("Name : " + JSON.stringify($rootScope.selectedTalent));
                vm.currrentOBJ = $rootScope.selectedTalent;
                //$rootScope.selectedTalent = {};

                vm.namedActorList.push(vm.currrentOBJ);

                vm.temp = {
                    'contactPrivilegeID': vm.contactPrivileges.contactPrivileges.id,
                    'reviewer': vm.currrentOBJ.type,
                    'tagID': vm.currrentOBJ.id
                };

                vm.contactPrivileges.contactPrivilegeReviewers.push(vm.temp);
                vm.namedActorListSaving.push(vm.temp);

                console.log("Added actor....");
            }
        });*/

        vm.albumDelete = [];
        vm.reviewerDelete = [];

        vm.removeAlbums = function (index) {

            console.log("contactPrivilegeID : "+vm.contactPrivileges.contactPrivileges.id);
            console.log("album node ID : "+vm.namedAlbumList[index].id);
            vm.temp = {
                'contactPrivilegeID': vm.contactPrivileges.contactPrivileges.id,
                'albumNodeID': vm.namedAlbumList[index].id
            };
            vm.albumDelete.push(vm.temp);
            vm.namedAlbumList.splice(index, 1);

            vm.namedAlbumListSaving.splice(index, 1);
            vm.contactPrivileges.contactPrivilegeAlbums.splice(index,1);

        };
        vm.removeActors = function (index) {
            console.log("Before Remove : "+JSON.stringify(vm.contactPrivileges.contactPrivilegeReviewers));
            console.log("Removing Actors : "+JSON.stringify(vm.namedActorList[index]));
            console.log("contactPrivilegeID : "+vm.contactPrivileges.contactPrivileges.id);
            console.log("tag ID : "+vm.namedActorList[index].id);

            vm.temp = {
                'contactPrivilegeID': vm.contactPrivileges.contactPrivileges.id,
                'tagID': vm.namedActorList[index].id
            };
            vm.reviewerDelete.push(vm.temp);
            vm.namedActorList.splice(index, 1);
            console.log("Get Removeing just by index : "+JSON.stringify(vm.contactPrivileges.contactPrivilegeReviewers[index]));
            vm.contactPrivileges.contactPrivilegeReviewers.splice(index,1);
            console.log("Final CPR : "+JSON.stringify(vm.contactPrivileges.contactPrivilegeReviewers));
        };





        vm.save = function () {


            //vm.contactPrivileges.contactPrivilegeAlbums.(vm.namedAlbumListSaving);
           // vm.contactPrivileges.contactPrivilegeReviewers = vm.namedActorListSaving;

            console.log("Albums to remove : "+JSON.stringify(vm.albumDelete));
            console.log("Actors to remove : "+JSON.stringify(vm.reviewerDelete));

            console.log("Saving CP : " + JSON.stringify(vm.contactPrivileges));
            //ContactPrivileges.update(vm.contactPrivileges.contactPrivileges, onSaveSuccess, onSaveError);

            if(vm.albumDelete.length > 0){
                console.log("Removing albums");
                $http({
                    method: 'POST',
                    url: 'api/delete/contact-privileges/albums',
                    data: vm.albumDelete
                }).then(function successCallback(response) {
                    console.log('Deleted....');
                }, function errorCallback(response) {

                });
            }


            if(vm.reviewerDelete.length > 0){
                console.log("Removing actors");
                $http({
                    method: 'POST',
                    url: 'api/delete/contact-privileges/reviewers',
                    data: vm.reviewerDelete
                }).then(function successCallback(response) {
                    console.log('Deleted....');
                }, function errorCallback(response) {

                });
            }


            $http({
                method: 'POST',
                url: 'api/dto/contact-privileges',
                data: vm.contactPrivileges
            }).then(function successCallback(response) {
                console.log("updated");
                alert("Data Updated");
                $uibModalInstance.close();
            }, function errorCallback(response) {

            });
        };
        var onSaveSuccess = function (result) {

            alert("Data Updated");
            $uibModalInstance.close(result);

        };

        var onSaveError = function () {
            vm.isSaving = false;
        };



        vm.delete  = function () {
            $ngConfirm({
                title: 'Warning!',
                content: "Are you sure you want delete,can't undo this action ?",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-green',
                        action: function () {
                            ContactPrivileges.delete({id: vm.contactPrivileges.contactPrivileges.id});
                        }
                    },
                    cancel: {
                        text: 'Cancel',
                        btnClass: 'btn-red',
                        action: function () {
                        }
                    }

                }
            });
        };

        vm.clearRestart = function () {
            $ngConfirm({
                title: 'Warning!',
                content: "Are you sure you want delete this users restart information ?",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-green',
                        action: function () {
                            $http({
                                method: 'GET',
                                url: 'api/restart/clear',
                                params: {
                                    'id': vm.contactPrivileges.contactPrivileges.id,
                                    'isAll': false
                                }
                            }).then(function successCallback(response) {

                            }, function errorCallback(response) {

                            });
                        }
                    },
                    cancel: {
                        text: 'Cancel',
                        btnClass: 'btn-red',
                        action: function () {
                        }
                    }

                }
            });

        };
    }
})();
