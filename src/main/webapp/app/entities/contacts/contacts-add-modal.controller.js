/**
 * Created by macbookpro on 11/3/16.
 */
(function () {
    'use strict';

    angular.module('smartLpcApp').controller('ContactsAddModalController',
        ContactsAddModalController);

    ContactsAddModalController.$inject = ['$uibModalStack','elementID','$uibModalInstance','$ngConfirm', '$rootScope', '$state',
        '$uibModal', '$scope', 'entity', 'Contacts',
        'Lookups', 'Departments', 'User', '$http', 'ContactRelationships'];

    function ContactsAddModalController($uibModalStack,elementID,$uibModalInstance,$ngConfirm,$rootScope, $state, $uibModal,
                                   $scope, entity, Contacts, Lookups, Departments, User,
                                   $http, $mdDialog, ContactRelationships) {


        var vm = this;
        vm.contacts = entity;

        $rootScope.savedContact = [];

        vm.lookupss = {};

        $http({
            method: 'GET',
            url: 'api/lookups/contacts/type'
        }).then(function successCallback(response) {
            vm.lookupss = response.data;

        }, function errorCallback(response) {

        });

        $rootScope.relationships = null;
        vm.relatedContacts = [];

        vm.count = 0;
        $rootScope.$watch(function () {
            return $rootScope.relationships;
        }, function () {
            if ($rootScope.relationships == null) {


            } else {

                vm.currrentOBJ = $rootScope.relationships;

                if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.contacts.contactCompany')) {
                    vm.contacts.companyContact = vm.currrentOBJ.data;
                } else if (angular.equals(vm.currrentOBJ.elementID, 'relatedContact')) {
                    vm.relatedContacts.push({
                        "isPrimaryContact": false,
                        "contact_b": vm.currrentOBJ.data,
                        "relationshipType": vm.currrentOBJ.data.typeValue
                    });
                    vm.count++;
                } else {

                }
            }
        });

        vm.departmentss = Departments.query();
        vm.users = User.query();

        vm.states = ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California',
            'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia',
            'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas',
            'Kentucky', 'Louisiana', 'Maine', 'Maryland', 'Massachusetts',
            'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana',
            'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey',
            'New Mexico', 'New York', 'North Carolina', 'North Dakota',
            'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode Island',
            'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah',
            'Vermont', 'Virginia', 'Washington', 'West Virginia',
            'Wisconsin', 'Wyoming'];

        vm.load = function (id) {
            Contacts.get({
                id: id
            }, function (result) {
                vm.contacts = result;
            });
        };

        vm.contactsDTO = {
            "contacts": vm.contacts,
            "contactRelationships": vm.relatedContacts
        };

        vm.save = function () {

            vm.isSaving = true;
            if (vm.contacts.id === null) {
                Contacts.save(vm.contactsDTO, onSaveSuccess, onSaveError);
            } else {
                Contacts.update(vm.contactsDTO, onSaveSuccess, onSaveError);
            }

        };
        var onSaveSuccess = function (result) {
            vm.isSaving = false;

            $scope.savedID = result.id;
            $rootScope.savedContact.push(result);

            $ngConfirm({
                title: 'Success!',
                content: "Contact : <strong>" + vm.contacts.fullName + "</strong> has been created",
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
            //$uibModalInstance.dismiss('cancel');
            $scope.currentOBJ = {
                "elementID": elementID,
                "data": result
            };
            console.log("DAta in rootscope : "+JSON.stringify($scope.currentOBJ));

            $rootScope.relationships = $scope.currentOBJ;
            $uibModalStack.dismissAll('cancel')
        };

        vm.saveAndAdd = function () {
            vm.isSaving = true;
            Contacts.save(vm.contactsDTO, onSaveAddSuccess, onSaveError);
        };

        $rootScope.relatedContacts = [];

        var onSaveAddSuccess = function (result) {
            vm.isSaving = false;
            $ngConfirm({
                title: 'Success!',
                content: "Contact : <strong>" + vm.contacts.fullName + "</strong> has been created",
                type: 'green',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-green',
                        action: function () {
                            $state.reload();
                            vm.contactsDTO = null;
                        }
                    }
                }
            });


        };

        var onSaveError = function () {
            vm.isSaving = false;
        };
        vm.clear = function () {
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        vm.openCalendar = function (date) {
            vm.datePickerOpenStatus[date] = true;
        };

        vm.goBack = function () {
            window.history.back();
        };

        $scope.related = [];
        vm.addRelated = function () {
            var newItemNo = $scope.related.length + 1;
            $scope.related.push({
                'id': 'related' + newItemNo
            });
        };

        vm.removeRelated = function (index) {
            vm.relatedContacts.splice(index, 1);
            $scope.related.splice(index, 1);
        };

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
                size: 'xl',
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


        vm.close = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.addType = function () {
            var type = prompt("Add New Option : ", "");
            console.log("type : "+type);
            if(type === null ) {
                alert("No status Entered");
            }else{
                vm.newType = {
                    tableName: 'contacts',
                    fieldName: 'type_id',
                    textValue: type,
                    id: null
                };


                Lookups.save(vm.newType, onSaveSuccess10, onSaveError10);


            }
        };
        var onSaveSuccess10 = function (result) {
            vm.lookupss.push(result);
            console.log("GOT NEW TYPES : "+JSON.stringify(result));
            alert("New types Created")
        };

        var onSaveError10 = function () {

        };

        vm.editModal = function (id) {

            // var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/contacts/contacts-update-modal.html',
                controller: 'ContactsUpdateController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'xl',
                resolve : {
                    translatePartialLoader : [
                        '$translate',
                        '$translatePartialLoader',
                        function($translate,
                                 $translatePartialLoader) {
                            $translatePartialLoader
                                .addPart('contacts');
                            return $translate.refresh();
                        } ],


                    entity : [ '$stateParams', 'Contacts',
                        function($stateParams, Contacts) {
                            // contactID: ['$stateParams',
                            // 'Contacts', function
                            // ($stateParams, Contacts) {

                            return Contacts.get({
                                id : $stateParams.id
                            }).$promise;
                        } ]

                }
                /*resolve: {
                    entity: ['ProjectRoles', function(ProjectRoles) {
                        return ProjectRoles.get({id : $stateParams.id}).$promise;
                    }]
                }*/
            });
        };
    }
})();
