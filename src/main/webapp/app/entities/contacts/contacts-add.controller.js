/**
 * Created by macbookpro on 11/3/16.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ContactsAddController', ContactsAddController);

    ContactsAddController.$inject = ['pagingParams', '$rootScope', '$state', '$uibModal', '$scope', '$stateParams', 'entity', 'Contacts', 'Lookups', 'Departments', 'User', '$http', 'ContactRelationships'];

    function ContactsAddController(pagingParams, $rootScope, $state, $uibModal, $scope, $stateParams, entity, Contacts, Lookups, Departments, User, $http, $mdDialog, ContactRelationships) {

        var vm = this;
        vm.contacts = entity;

        $rootScope.savedContact = [];
        // vm.lookupss = Lookups.query();

        vm.lookupss = {};


        // vm.relatedContact = $rootScope.relatedContacts;
        //console.log("related Contact : ");
        //console.log(JSON.stringify(vm.relatedContact));

        $http({
            method: 'GET',
            url: 'api/lookups/contacts/type'
        }).then(function successCallback(response) {
            vm.lookupss = response.data;

        }, function errorCallback(response) {

        });

        $rootScope.relationships = null;
        vm.relatedContacts = [];
        if ($rootScope.relationships == null) {
            console.log("null");

        } else {
            console.log("not null");
            console.log($rootScope.relationships);
        }

        vm.count = 0;
        $rootScope.$watch(function () {
            return $rootScope.relationships;
        }, function () {
            if ($rootScope.relationships == null) {
                console.log("null rootscope");

            } else {
                console.log("not null");

                vm.currrentOBJ = $rootScope.relationships;
                console.log("========> " + JSON.stringify(vm.currentOBJ));
                if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.contacts.contactCompany')) {
                    console.log("found equal");

                    vm.contacts.companyContact = vm.currrentOBJ.data;
                    console.log(JSON.stringify(vm.contacts.companyContact));
                    console.log(vm.contacts.companyContact.fullName);
                } else if (angular.equals(vm.currrentOBJ.elementID, 'relatedContact')) {
                    console.log("count : " + vm.count);
                    //vm.relatedContacts.push(vm.currrentOBJ.data);
                    // vm.relatedContacts[vm.count].contactB = vm.currrentOBJ.data;
                    vm.relatedContacts.push({"isPrimaryContact": false, "contact_b": vm.currrentOBJ.data});
                    vm.count++;

                    console.log("related Contacts size " + vm.relatedContacts.length);
                } else {
                    console.log("not equal..");
                }
            }
        });

        vm.departmentss = Departments.query();
        vm.users = User.query();
        vm.contactss = Contacts.query();
        vm.states = ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California',
            'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia', 'Hawaii',
            'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana',
            'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota',
            'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire',
            'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota',
            'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode Island',
            'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont',
            'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
        ];


        vm.load = function (id) {
            Contacts.get({id: id}, function (result) {
                vm.contacts = result;
            });
        };


        vm.contactsDTO = {
            "contacts": vm.contacts,
            "contactRelationships": vm.relatedContacts
        }

        vm.save = function () {
            console.log("calling SAVE function....");
            vm.isSaving = true;
            if (vm.contacts.id !== null) {

                console.log(("updating contacts from admin /api "))
                console.log(JSON.stringify(vm.contacts));
                Contacts.update(vm.contacts, onSaveSuccess, onSaveError);

            } else {


                console.log("SAVING : ");
                console.log("--> 1");
                console.log("====== CONTACTS ====");
                console.log(JSON.stringify(vm.contacts));
                console.log("====== RELATED ====");
                console.log(JSON.stringify(vm.relatedContactss));


                console.log("====================================")
                console.log("==============TOTAL===============");
                // console.log(JSON.stringify(vm.contactsDTO));
                Contacts.save(vm.contactsDTO, onSaveSuccess, onSaveError);
                console.log(JSON.stringify(vm.contactsDTO));

                alert("Contact : " + vm.contacts.fullName + " has been saved");
            }

        };
        var onSaveSuccess = function (result) {
            vm.isSaving = false;
            //$scope.$emit('smartLpcApp:contactsUpdate', result);
            //$uibModalInstance.close(result);
            //$state.go('contacts', {}, {reload: true});// use for redirecting ...
            console.log("data saved : " + JSON.stringify(result.data));
            $rootScope.savedContact.push(result);

        };
        vm.saveAndAdd = function () {
            console.log("calling SAVE and ADD function....");
            vm.isSaving = true;
            Contacts.save(vm.contactsDTO, onSaveAddSuccess, onSaveError);
        };
        var onSaveAddSuccess = function (result) {
            vm.isSaving = false;
            //$scope.$emit('smartLpcApp:contactsUpdate', result);
            //$uibModalInstance.close(result);
            $state.go('contacts', {}, {reload: true});// use for redirecting ...
            console.log("data saved : " + JSON.stringify(result.data));
            //   $rootScope.savedContact = result;

        };


        var onSaveError = function () {
            vm.isSaving = false;
        };
        // vm.contacts.relatedContacts = [];

        vm.clear = function () {
            //$uibModalInstance.dismiss('cancel');
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
            console.log("related contacts added " + newItemNo);
            $scope.related.push({'id': 'related' + newItemNo});
        };

        vm.removeRelated = function (index) {
            console.log("related contacts removed .." + index);
            vm.relatedContacts.splice(index, 1);
            $scope.related.splice(index, 1);
        };


        vm.selectedContact = function (id) {
            vm.contacts.companyContact = id;
            console.log("Company selected : " + vm.contacts.companyContact);
        };


        vm.relatedContact = function (id) {
            Contacts.get({id: id}, function (result) {
                vm.contacts.relatedContacts.push(result);
            });
        };


        vm.companyContactInputBox = 'companyContactInputBox';


        vm.companyContact = [];

        vm.openModal = function (elementID) {

            console.log("id of textbox : " + elementID);
            //var ctrl = angular.element(id).data('$ngModelController');

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
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contacts');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            })
        };


        console.log("xxx value : " + vm.xxx);
    }
})();
