(function () {
    'use strict';

    angular.module('smartLpcApp').controller('ContactsUpdateController',
        ContactsUpdateController);

    ContactsUpdateController.$inject = ['$ngConfirm', 'entity', '$state', '$http',
        '$uibModal', '$scope', '$rootScope', '$stateParams', 'Contacts',
        'Lookups', 'Departments', 'User'];

    function ContactsUpdateController($ngConfirm, entity, $state, $http, $uibModal, $scope,
                                      $rootScope, $stateParams, Contacts, Lookups, Departments, User) {
        console.log("Contact Update Controller");

        var vm = this;
        vm.contactDTO = entity;
        const originalData = entity;
        vm.contacts = [];

        vm.relatedContacts = [];

        vm.contacts = vm.contactDTO.contacts;
        vm.relatedContacts = vm.contactDTO.contactRelationships;

        console.log(" == = > "+JSON.stringify(vm.contactDTO))
        vm.prevNext = {};0
        $http({
            method: 'GET',
            url: 'api/contact/prev/next/' + vm.contactDTO.contacts.id

        }).then(function successCallback(response) {
            vm.prevNext = response.data;
            console.log("Prev Next Contacts : " + JSON.stringify(vm.prevNext));
        }, function errorCallback(response) {

        });

        vm.load = function (id) {

            Contacts.get({
                id: id
            }, function (result) {
                vm.contactDTO = result.data;
                vm.originalData = vm.contactDTO;
            });

        };

        var unsubscribe = $rootScope.$on('smartLpcApp:contactsUpdate',
            function (event, result) {
                vm.contacts = result;
            });
        $scope.$on('$destroy', unsubscribe);


        vm.lookupss = {};
        $http({
            method: 'GET',
            url: 'api/lookups/contacts/type'
        }).then(function successCallback(response) {
            vm.lookupss = response.data;

        }, function errorCallback(response) {

        });

        vm.departmentss = Departments.query();
        vm.users = User.query();


        vm.selectedContact = function (id) {
            vm.contacts.companyContact = id;
        };

        vm.relatedContact = function (id) {
            Contacts.get({
                id: id
            }, function (result) {
                vm.contacts.relatedContacts.push(result);
            });
        };

        vm.companyContactInputBox = 'companyContactInputBox';

        vm.companyContact = [];

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
                    }
                },
                translatePartialLoader: ['$translate',
                    '$translatePartialLoader',
                    function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
            })
        };

        vm.count = 0;
        $rootScope
            .$watch(
                function () {
                    return $rootScope.relationships;
                },
                function () {
                    if ($rootScope.relationships == null) {
                    } else {
                        vm.currrentOBJ = $rootScope.relationships;
                        if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.contacts.contactCompany')) {
                            vm.contactDTO.contacts.companyContact = vm.currrentOBJ.data;
                        } else if (angular.equals(vm.currrentOBJ.elementID, 'relatedContact')) {
                            vm.contactDTO.contactRelationships.pop();
                            vm.contactDTO.contactRelationships.push({
                                "isPrimaryContact": false,
                                "contact_b": vm.currrentOBJ.data,
                                "relationshipType": vm.currrentOBJ.data.typeValue
                            });
                            vm.count++;
                        } else {

                        }
                    }
                });

        vm.save = function () {
            vm.isSaving = true;
            Contacts.update(vm.contactDTO, onSaveSuccess, onSaveError);
        };

        var onSaveSuccess = function (result) {
            vm.isSaving = false;
            $ngConfirm({
                title: 'Success!',
                content: "Contact : <strong>" + result.fullName + "</strong> has been updated",
                type: 'green',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-green',
                        action: function () {
                        }
                    }
                }
            });
            $state.go('contacts-detail', {'id':result.id}, {reload: true});

        };

        vm.saveAndNext = function () {

            vm.isSaving = true;
            Contacts.update(vm.contactDTO, onSaveAddSuccess, onSaveError);
        };


        var onSaveAddSuccess = function (result) {
            vm.isSaving = false;
            $ngConfirm({
                title: 'Success!',
                content: "Contact : <strong>" + result.fullName + "</strong> has been Updated",
                type: 'green',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-green',
                        action: function () {
                            $state.go("contacts.edit", {id:vm.prevNext.next}, {reload: true, notify: true});
                        }
                    }
                }
            });


        };


        var onSaveError = function () {
            vm.isSaving = false;
            $ngConfirm({
                title: 'Error!',
                content: "Contact : <strong>" + result.fullName + "</strong> was not Updated.",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-red',
                        action: function () {
                            $state.go("contacts", {}, {reload: true});
                        }
                    }
                }
            });
        };

        $scope.related = [];

        vm.addRelated = function () {
            vm.contactDTO.contactRelationships.push({
                "isPrimaryContact": false,
                "contact_b": null,
                "relationshipType": null
            });

        };

        vm.removeRelated = function (index) {
            vm.contactDTO.contactRelationships.splice(index, 1);
        };

        vm.addPrivilege = function (id) {

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/contacts/project-permission.html',
                controller: 'ProjectPermission',
                controllerAs: 'vm',
                size: 'md',
                scope: $scope,


                resolve: {
                    id: function () {
                        return id;
                    },

                    translatePartialLoader: ['$translate',
                        '$translatePartialLoader',
                        function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('contacts');
                            $translatePartialLoader.addPart('projects');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                }
            });
        };

        vm.close = function () {

            if (_.isEqual(originalData, vm.contactDTO)) {
                console.log("Equal Data");
            } else {
                console.log("Not Equal Data");
            }
            window.history.back();
            console.log("==> " + JSON.stringify(originalData));
            console.log("==> " + JSON.stringify(vm.contactDTO));
        };

        vm.lookUpContact = function (id) {

            var modalInstance = $uibModal.open({
                templateUrl: 'app/entities/contacts/contacts-update-modal.html',
                controller: 'ContactsModalUpdateController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'xl',
                resolve: {
                    translatePartialLoader: [
                        '$translate',
                        '$translatePartialLoader',
                        function ($translate,
                                  $translatePartialLoader) {
                            $translatePartialLoader
                                .addPart('contacts');
                            return $translate.refresh();
                        }],


                    entity: ['$stateParams', 'Contacts',
                        function ($stateParams, Contacts) {
                            // contactID: ['$stateParams',
                            // 'Contacts', function
                            // ($stateParams, Contacts) {

                            return Contacts.get({
                                id: id
                            }).$promise;
                        }]
                }
            })
        };


        vm.blockUser = function () {
            console.log("Block User : "+vm.block);
            if(vm.block === true) {
                vm.contacts.loginAttempt = 4;
            }else{
                vm.contacts.loginAttempt = 0;
            }
        };

        vm.delete  = function () {
            $ngConfirm({
                title: 'Warning!',
                content: "Are you sure you want to delete Contact: <strong>"+vm.contactDTO.contacts.fullName+"</strong>",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-red',
                        action: function () {
                            Contacts.delete({id: vm.contactDTO.contacts.id});
                            window.history.back();
                        }
                    },
                    cancel: {
                        text: 'Cancel',
                        btnClass: 'btn-green',
                        action: function () {

                        }
                    }
                }
            });
        };

    }
})();
