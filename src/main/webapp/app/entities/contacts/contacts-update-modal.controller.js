(function () {
    'use strict';

    angular.module('smartLpcApp').controller('ContactsModalUpdateController',
        ContactsModalUpdateController);

    ContactsModalUpdateController.$inject = ['$uibModalInstance','$ngConfirm', 'entity', '$state', '$http',
        '$uibModal', '$scope', '$rootScope', '$stateParams', 'Contacts',
        'Lookups', 'Departments', 'User'];

    function ContactsModalUpdateController($uibModalInstance,$ngConfirm, entity, $state, $http, $uibModal, $scope,
                                      $rootScope, $stateParams, Contacts, Lookups, Departments, User) {
        console.log("ContactsModalUpdateController");

        var vm = this;
        console.log("Getting cont : "+entity.contacts.id)
        vm.contactDTO = entity;
        const originalData = entity;
        vm.contacts = [];

        vm.relatedContacts = [];

        console.log(" == = > "+JSON.stringify(vm.contactDTO))
        vm.contacts = vm.contactDTO.contacts;
        vm.relatedContacts = vm.contactDTO.contactRelationships;


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
            $uibModalInstance.dismiss('cancel');

        };

        vm.saveAndAdd = function () {

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
                            $state.go("contacts", {}, {reload: true});
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
            $uibModalInstance.dismiss('cancel');
        };

        vm.lookUpContact = function (id) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/entities/contacts/contacts-update-modal.html',
                controller: 'ContactsUpdateController',
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
                                id: $stateParams.id
                            }).$promise;
                        }]
                }
            })
        };

    }
})();
