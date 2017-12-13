(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectRolesDialogController', ProjectRolesDialogController);

    ProjectRolesDialogController.$inject = ['$ngConfirm','$http','projectID', 'entity', 'id', '$scope', '$stateParams', '$uibModalInstance', 'ProjectRoles', 'Projects', 'Contacts', 'User'];

    function ProjectRolesDialogController($ngConfirm,$http,projectID, entity, id, $scope, $stateParams, $uibModalInstance, ProjectRoles, Projects, Contacts, User) {
        console.log("ProjectRolesDialogController");
        console.log("Project ID: " + projectID);

        var vm = this;
        vm.load = function (id) {
            ProjectRoles.get({id: id}, function (result) {
                vm.projectRoles = result;
            });
        };
        //get previous and next
        vm.prevNext = {};
        vm.prevNextFunction = function (id) {
            console.log('Getting prev and next of :'  +id);
            $http({
                method: 'GET',
                url: 'api/prev/next/talent/'+id+'/'+projectID
            }).then(function successCallback(response) {
                vm.prevNext = response.data;
                console.log("PreVNext : "+JSON.stringify(vm.prevNext));
            }, function errorCallback(response) {

            });
        };



        if (angular.equals(id, null)) {
            vm.projectRoles = entity;

        } else {
            vm.id = id;
            console.log("ID to get : " + vm.id);
            /*ProjectRoles.get({id: id}, function (result) {
                vm.projectRoles = result;
            });*/
            vm.load(vm.id);
            //vm.projectRoles = entity;
            vm.prevNextFunction(vm.id);
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


        var onSaveSuccess = function (result) {
            //$scope.$emit('smartLpcApp:projectRolesUpdate', result);
            //$uibModalInstance.close(result);

            vm.isSaving = false;
            $ngConfirm({
                title: 'Success!',
                content: "Tag : <strong>" + result.tagName + "</strong> has been updated",
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
            //window.history.back();
        };

        var onSaveSuccess2 = function (result) {
            //$scope.$emit('smartLpcApp:projectRolesUpdate', result);
            //$uibModalInstance.close(result);

            vm.isSaving = false;
            $ngConfirm({
                title: 'Success!',
                content: "Tag : <strong>" + result.tagName + "</strong> has been created",
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
            //window.history.back();
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;

            if (vm.projectRoles.id !== null) {
                ProjectRoles.update(vm.projectRoles, onSaveSuccess, onSaveError);
            } else {
            	vm.projectRoles.project = {id: projectID};
                ProjectRoles.save(vm.projectRoles, onSaveSuccess2, onSaveError);
            }
        };









        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
            $state.reload();
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









        vm.next = function (id) {
            if(id === null){
                alert('You are viewing LAST record');
            }
            else{
                vm.load(id);
                vm.prevNextFunction(id);
            }
        };
        vm.previous = function (id) {

            if(id === null){
                alert('You are viewing FIRST record');
            }
            else{
                vm.load(id);
                vm.prevNextFunction(id);
            }
        };
        vm.delete = function () {
            $ngConfirm({
                title: 'Delete!',
                content: "Are You Sure you want to Delete this Tag?",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Delete',
                        btnClass: 'btn-red',
                        action: function () {
                            ProjectRoles.delete({id: id});

                            $ngConfirm({
                                title: 'Success!',
                                content: "Tag has been Deleted",
                                type: 'green',
                                typeAnimated: true,
                                theme: 'dark',
                                buttons: {
                                    confirm: {
                                        text: 'Success',
                                        btnClass: 'btn-green',
                                        action: function () {
                                            $uibModalInstance.dismiss('cancel');
                                        }
                                    }
                                }
                            });
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
