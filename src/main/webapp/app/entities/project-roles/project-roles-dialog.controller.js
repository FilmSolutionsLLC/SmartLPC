(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectRolesDialogController', ProjectRolesDialogController);

    ProjectRolesDialogController.$inject = ['$http','projectID', 'entity', 'id', '$scope', '$stateParams', '$uibModalInstance', 'ProjectRoles', 'Projects', 'Contacts', 'User'];

    function ProjectRolesDialogController($http,projectID, entity, id, $scope, $stateParams, $uibModalInstance, ProjectRoles, Projects, Contacts, User) {
        console.log("ProjectRolesDialogController");
        console.log("Project ID: " + projectID);
        var vm = this;
        if (angular.equals(id, null)) {
            vm.projectRoles = entity;

        } else {
            vm.id = id;
            console.log("ID to get : " + vm.id);
            ProjectRoles.get({id: id}, function (result) {
                vm.projectRoles = result;
            });
            //vm.projectRoles = entity;
            console.log("Got Data : " + JSON.stringify(vm.projectRoles));
        }

        vm.hotkeys = [];
        // GET hotkeys
        $http({
            method: 'GET',
            url: 'api/hotkeys/'+projectID
        }).then(function successCallback(response) {
            vm.dHK = response.data;
            console.log("===> HOTKEYS : "+JSON.stringify(vm.dHK));
            for(var i=0;i<vm.dHK.length;i++){
                vm.hotkeys.push(vm.dHK[i].hotkey_value);
            }
            console.log("===> keys : "+JSON.stringify(vm.hotkeys));
        }, function errorCallback(response) {

        });

        // vm.projectss = Projects.query();
        // vm.contactss = Contacts.query();
        // vm.users = User.query();
        vm.load = function (id) {
            ProjectRoles.get({id: id}, function (result) {
                vm.projectRoles = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:projectRolesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
            //window.history.back();
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;

            if (vm.projectRoles.id !== null) {
                console.log("Updating Project Roles : "+JSON.stringify(vm.projectRoles));
                ProjectRoles.update(vm.projectRoles, onSaveSuccess, onSaveError);
            } else {
            	vm.projectRoles.project = {id: projectID};
                console.log("Saving Project Roles : "+JSON.stringify(vm.projectRoles));
                ProjectRoles.save(vm.projectRoles, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.miniFullDt = false;
        vm.datePickerOpenStatus.fullFinalDt = false;
        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.expireDate = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        vm.openCalendar = function (date) {
            vm.datePickerOpenStatus[date] = true;
        };

        vm.flag = false;
        vm.setHotkey = function () {
            vm.flag = false;
            console.log("Changed  to : "+vm.projectRoles.hotkeyValue);
            if(angular.equals(vm.projectRoles.hotkeyValue,"")){
                console.log("NO DATA")
                vm.flag = false;
            }else{
                for(var i = 0;i<vm.hotkeys.length;i++){
                    if(  (angular.equals(vm.projectRoles.hotkeyValue,vm.hotkeys[i])) || (angular.equals(vm.projectRoles.hotkeyValue,'Z')) || (angular.equals(vm.projectRoles.hotkeyValue,'X'))){
                        console.log("Already Used ");
                        vm.flag = true;
                    }else{

                    }
                }
            }
        };
    }
})();
