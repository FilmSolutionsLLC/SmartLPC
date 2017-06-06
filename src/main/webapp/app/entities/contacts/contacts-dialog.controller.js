(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ContactsDialogController', ContactsDialogController);

    ContactsDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Contacts', 'Lookups', 'Departments', 'User', '$http'];

    function ContactsDialogController($scope, $stateParams, $uibModalInstance, entity, Contacts, Lookups, Departments, User, $http) {
        var vm = this;
        vm.contactsDTO = entity;
        console.log("printing entity : " + JSON.stringify(vm.contactsDTO));
        // vm.lookupss = Lookups.query();
        vm.lookupss = {};
        $http({
            method: 'GET',
            url: '/api/lookups/contacts/type'
        }).then(function successCallback(response) {
            vm.lookupss = response.data;
        }, function errorCallback(response) {

        });
        vm.departmentss = Departments.query();
        vm.users = User.query();
        vm.contactss = Contacts.query();
        vm.load = function (id) {
            Contacts.get({id: id}, function (result) {
                vm.contacts = result;
                console.log("printing entity : " + JSON.stringify(vm.contactsDTO));
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:contactsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            console.log("saving..");
            vm.isSaving = true;
            if (vm.contacts.id !== null) {
                Contacts.update(vm.contacts, onSaveSuccess, onSaveError);
            } else {
                Contacts.save(vm.contacts, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        vm.openCalendar = function (date) {
            vm.datePickerOpenStatus[date] = true;
        };

        $scope.related = [];


        vm.addRelated = function () {
            console.log("added ..");
            var newItemNo = $scope.related.length + 1;
            $scope.related.push({'id': 'related' + newItemNo});
        };

        vm.removeRelated = function (index) {
            //console.log("index : " + index);
            $scope.related.splice(index, 1);
        };
    }
})();
