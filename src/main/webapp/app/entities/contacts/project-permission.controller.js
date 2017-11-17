(function () {
    'use strict';

    angular.module('smartLpcApp').controller('ProjectPermission',
        ProjectPermission);

    ProjectPermission.$inject = ['$ngConfirm','DateUtils', '$http', 'entity', 'ContactPrivileges', '$rootScope', '$uibModal', '$scope', '$state', 'Contacts', 'ContactsSearch', 'AlertService'];

    function ProjectPermission($ngConfirm,DateUtils, $http, entity, ContactPrivileges, $rootScope, $uibModal, $scope, $state, Contacts, ContactsSearch, AlertService) {
        console.log("Project Permission Controller");
        var vm = this;
        vm.contactDTO = entity;
        vm.contacts = [];
        vm.relatedContacts = [];
        vm.contacts = vm.contactDTO.contacts;

        $rootScope.savedContact = [];
        vm.selectedProjects = [];

        $rootScope.savedContact.push(vm.contacts);

        vm.downloadType = [{
            'id': 0,
            'value': "NONE"
        }, {
            'id': 1,
            'value': "ALL"
        }, {
            'id': 2,
            'value': "Lock Approved"
        }];
        vm.exclusives = [{
            'id': 0,
            'value': "NONE"
        }, {
            'id': 1,
            'value': "BASIC"
        }, {
            'id': 2,
            'value': "MASTER"
        }];

        vm.openModalProject = function () {

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/contacts/projectsList.html',
                controller: 'ProjectListController',
                size: 'xl',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    isMultiExecAdd: function () {
                        return false;
                    }
                },
            });
        };

        $rootScope.selectedProjects = [];

        $rootScope.$watch(function () {
            return $rootScope.selectedProjects;
        }, function () {
            if ($rootScope.selectedProjects.length == 0) {

            } else {
                for (var i = 0; i < $rootScope.selectedProjects.length; i++) {
                    vm.selectedProjects.push($rootScope.selectedProjects[i]);
                }

            }
        });

        vm.removeSelected = function (index) {

            vm.selectedProjects.splice(index, 1);

        };

        vm.save = function () {
            vm.isSaving = true;
            console.log("Saving Contact Privilege : "+JSON.stringify(vm.selectedProjects));
            for (var i = 0; i < vm.selectedProjects.length; i++) {

                ContactPrivileges.update(vm.selectedProjects[i], onSaveSuccess, onSaveError);
                if (i == vm.selectedProjects.length - 1) {

                    $ngConfirm({
                        title: 'Success!',
                        content: "<small>Projects and its Privileges have been added/updated to the Contact </small>: <strong>"+vm.contacts.fullName+"</strong>",
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
                }
            }
        };

        var onSaveSuccess = function (result) {
          /*  $ngConfirm({
                title: 'Success!',
                content: "<small>Projects and its Privileges have been added/updated to the Contact </small>: <strong>"+vm.contacts.fullName+"</strong>",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-red',
                        action: function () {
                        }
                    }
                }
            });*/
            vm.isSaving = false;


        };

        var onSaveError = function () {
            $ngConfirm({
                title: 'Error!',
                content: "<small>Error in adding Projects to the Contact : </small>: <strong>"+vm.contacts.fullName+"</strong>",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-red',
                        action: function () {
                        }
                    }
                }
            });
            alert("Error in adding Projects to the Contact : " + vm.contacts.fullName);
        };

        $http({
            method: 'GET',
            url: 'api/contact/project/' + vm.contactDTO.contacts.id

        }).then(
            function successCallback(response) {
                response.data.expireDate = DateUtils.convertLocalDateToServer(response.data.expireDate);
                vm.projects = response.data;


                for (var i = 0; i < vm.projects.length; i++) {
                    vm.selectedProjects.push(vm.projects[i]);

                }

            }, function errorCallback(response) {

            });


        // select deselect all from top rows

        vm.clickedThis = function (type, value) {


            for (var i = 0; i < vm.selectedProjects.length; i++) {
                if (angular.equals(type, 'checkAllInternalUsers')) {
                    vm.selectedProjects[i].internal = value;
                }
                else if (angular.equals(type, 'checkAllExec')) {
                    vm.selectedProjects[i].exec = value;
                }
                else if (angular.equals(type, 'checkAllVendor')) {
                    vm.selectedProjects[i].vendor = value;
                }
                else if (angular.equals(type, 'checkAllPrint')) {
                    vm.selectedProjects[i].print = value;
                }
                else if (angular.equals(type, 'checkAllCaptioning')) {
                    vm.selectedProjects[i].captioning = value;
                }
                else if (angular.equals(type, 'checkAllEmail')) {
                    vm.selectedProjects[i].email = value;
                }

                else if (angular.equals(type, 'checkAllPriorityPix')) {
                    vm.selectedProjects[i].priorityPix = value;
                }
                else if (angular.equals(type, 'checkAllReleaseAll')) {
                    vm.selectedProjects[i].releaseExclude = value;
                }
                else if (angular.equals(type, 'checkAllSeeLock')) {
                    vm.selectedProjects[i].lockApproveRestriction = value;
                }

                else if (angular.equals(type, 'checkAllHidden')) {
                    vm.selectedProjects[i].hidden = value;
                }
                else if (angular.equals(type, 'checkAllVideo')) {
                    vm.selectedProjects[i].hasVideo = value;
                }
                else if (angular.equals(type, 'checkAllSensitive')) {
                    vm.selectedProjects[i].viewSensitive = value;
                }
                else if (angular.equals(type, 'expireDate')) {
                    vm.selectedProjects[i].expireDate = value;
                }
                else if (angular.equals(type, 'checkAllReadonly')) {
                    vm.selectedProjects[i].readOnly = value;
                }
                else if (angular.equals(type, 'downloadType')) {
                    vm.selectedProjects[i].downloadType = value;
                }
                else if (angular.equals(type, 'exclusives')) {
                    vm.selectedProjects[i].exclusives = value;
                }
                else if (angular.equals(type, 'checkAllViewOnly')) {
                    vm.selectedProjects[i].isABCViewer = value;
                }
                else if (angular.equals(type, 'checkAllWelcomeMessage')) {
                    vm.selectedProjects[i].welcomeMessage = value;
                }
                else if (angular.equals(type, 'checkAllTalentMgmt')) {
                    vm.selectedProjects[i].talentManagement = value;
                }
                else if (angular.equals(type, 'checkAllSOMgmt')) {
                    vm.selectedProjects[i].signoffManagement = value;
                }
                else if (angular.equals(type, 'checkAllGlobalAlbum')) {
                    vm.selectedProjects[i].globalAlbum = value;
                }
                else if (angular.equals(type, 'checkAllUpload')) {
                    vm.selectedProjects[i].fileUpload = value;
                }
                else if (angular.equals(type, 'checkAllAdhoc')) {
                    vm.selectedProjects[i].adhocLink = value;
                }
                else if (angular.equals(type, 'checkAllRetouch')) {
                    vm.selectedProjects[i].retouch = value;
                }
                else if (angular.equals(type, 'checkAllCritique')) {
                    vm.selectedProjects[i].critiqueIt = value;
                }
                else if (angular.equals(type, 'checkAllDeleteAssets')) {
                    vm.selectedProjects[i].deleteAssets = value;
                }
                else if (angular.equals(type, 'checkAllDisabled')) {
                    vm.selectedProjects[i].disabled = value;
                }
                else if (angular.equals(type, 'checkAllWatermark')) {
                    vm.selectedProjects[i].watermark = value;
                } else if (angular.equals(type, 'checkAllFinalized')) {
                    vm.selectedProjects[i].showFinalizations = value;
                }
                else if (angular.equals(type, 'outerWatermark')) {
                    vm.selectedProjects[i].watermarkOuterTransparency = value;
                }
                else if (angular.equals(type, 'innerWatermark')) {
                    vm.selectedProjects[i].watermarkInnerTransparency = value;
                }
            }
        };


        vm.removeSelected = function (index) {

            var r = confirm("Are you sure you want to remove Contact Privilege for Project : \n" + vm.selectedProjects[index].project.name);

            if (r == true) {

                ContactPrivileges.delete({id: vm.selectedProjects[index].id});
                vm.selectedProjects.splice(index, 1);
            } else {

            }
        };

        vm.close = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
