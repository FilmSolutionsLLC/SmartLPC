/**
 * Created by macbookpro on 1/23/17.
 */
(function () {
    'use strict';

    angular.module('smartLpcApp').controller('ProjectListController',
        ProjectListController);
    ProjectListController.$inject = ['isMultiExecAdd', '$ngConfirm', 'ContactPrivileges', 'ProjectsSearch',
        'Projects', '$http', '$rootScope', 'Contacts', 'Lookups',
        'Departments', 'User', 'ContactsSearch', 'AlertService',
        '$uibModalInstance', '$scope', '$state'];

    function ProjectListController(isMultiExecAdd, $ngConfirm, ContactPrivileges, ProjectsSearch, Projects,
                                   $http, $rootScope, Contacts, Lookups, Departments, User,
                                   ContactsSearch, AlertService, $uibModalInstance, $scope, $state) {
        console.log("Project Permission Controller");


        var vm = this;

        vm.multiExecsFlag = isMultiExecAdd;

        console.log("isMultiExecFlag : " + vm.multiExecsFlag);
        vm.searchProject = function (query) {

            if (angular.isDefined(query)) {
                ProjectsSearch.query({
                    query: query,
                    size: 1000000
                }, onSuccess, onError);
            } else {
                $scope.showLoader = true;

                $http({
                    method: 'GET',
                    url: 'api/idname/projects'
                }).then(
                    function (response) {
                        vm.projects = [];
                        vm.temp = response.data;

                        for (var i = 0; i < vm.temp.length; i++) {
                            $scope.tempObj = {
                                "id": vm.temp[i][0],
                                "name": vm.temp[i][1]
                            };
                            vm.projects.push($scope.tempObj);
                        }
                    })
            }
        };

        function onSuccess(data) {
            vm.projects = [];
            for (var i = 0; i < data.length; i++) {
                vm.projects.push(data[i].projects);

            }
            $scope.loading = false;
        };

        function onError(error) {
            // AlertService.error(error.data.message);
        }

        vm.filters = [];
        vm.addFilter = function () {
            vm.filters.push({
                "id": vm.filters.length + 1
            });
        };
        vm.removeFilter = function (index) {
            vm.filters.splice(index, 1);
        };

        vm.selectedProjects = [];
        $rootScope.selectedProjects = null;
        vm.selectedCheckBox = null;
        vm.selectProject = function (project) {
            if (vm.selectedProjects.indexOf(project) == -1) {
                vm.selectedProjects.push(project);

            } else {

                vm.selectedProjects.splice(vm.selectedProjects.indexOf(project), 1);
            }
        };
        vm.removeSelected = function (index) {

            console.log("removing id : "
                + vm.selectedCheckBox[vm.selectedProjects[index].id]);
            vm.selectedCheckBox[vm.selectedProjects[index].id] = false;
            vm.selectedProjects.splice(index, 1);

        };

        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.contacts = null;
        $rootScope.$watch(function () {
            return $rootScope.savedContact;
        }, function () {
            if ($rootScope.savedContact == null) {

            } else {
                $scope.name = $rootScope.savedContact.fullName;
                vm.contacts = $rootScope.savedContact;
            }
        });

        $rootScope.selectedProjects = [];

        vm.tempExecs = [];
        vm.save = function () {

            for (var j = 0; j < vm.contacts.length; j++) {

                for (var i = 0; i < vm.selectedProjects.length; i++) {

                    vm.execs = {
                        "contact": vm.contacts[j],
                        "project": vm.selectedProjects[i],
                        "exec": vm.exec,
                        "internal": vm.internal,
                        "captioning": vm.captioning,
                        "vendor": vm.vendor,
                        "email": vm.email,
                        "print": vm.print,
                        "lockApproveRestriction": vm.lockApproveRestriction,
                        "downloadType": vm.downloadType,
                        "priorityPix": vm.priorityPix,
                        "releaseExclude": vm.releaseExclude,
                        "watermark": vm.watermark,
                        "watermarkOuterTransparency": vm.watermarkOuterTransparency,
                        "watermarkInnerTransparency": vm.watermarkInnerTransparency,
                        "viewSensitive": vm.viewSensitive,
                        "isABCViewer": vm.isABCViewer,
                        "disabled": vm.disabled,
                        "exclusives": vm.exclusives,
                        "welcomeMessage": vm.welcomeMessage,
                        "expireDate": vm.expireDate,
                        "seesUntagged": vm.seesUntagged,
                        "talentManagement": vm.talentManagement,
                        "signoffManagement": vm.signoffManagement,
                        "datgeditManagement": vm.datgeditManagement,
                        "showFinalizations": vm.showFinalizations,
                        "readOnly": vm.readOnly,
                        "globalAlbum": vm.globalAlbum,
                        "critiqueIt": vm.critiqueIt,
                        "hasVideo": vm.hasVideo,
                        "adhocLink": vm.adhocLink,
                        "retouch": vm.retouch,
                        "fileUpload": vm.fileUpload,
                        "deleteAssets": vm.deleteAssets,
                        "restartColumns": 2,
                        "restartImagesPerPage": 20,
                        "restartImageSize": "Large",
                        "restartRole": null,
                        "restartImage": null,
                        "restartPage": 0,
                        "lastLoginDt": null,
                        "lastLogoutDt": null,
                        "createdDate": null,
                        "updatedDate": null,
                        "restartFilter": null,
                        "restartTime": null,
                        "loginCount": 0,
                        "defaultAlbum": null,
                        "createdByAdminUser": null,
                        "updatedByAdminUser": null,
                        "name": vm.contacts[j].username,
                        "title": null,
                        "description": null,
                        "author": "",
                        id: null
                    };
                    vm.isSaving = true;

                    vm.tempExecs.push(vm.execs);

                    if (angular.equals(vm.multiExecsFlag, true)) {
                        ContactPrivileges.save(vm.execs, onSaveSuccess, onSaveError);
                        vm.message = "Project(s) have been added to the Contact(s)"
                    }else {
                        vm.message = "Projects have been added to list.<br>Don't forget to <strong>Save All</strong>";
                    }
                }
                $rootScope.selectedProjects = vm.tempExecs;
            }
            $ngConfirm({
                title: 'Success!',
                content: vm.message,
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

        var onSaveSuccess = function (result) {
            vm.execs.id = result.id;
            vm.tempExecs.push(vm.execs);
            console.log("Result : " + JSON.stringify(result.id));
            //console.log("Got from DB cont privi : "+JSON.stringify(vm.execs));

            //$rootScope.selectedProjects.push(vm.tempExecs)
            // ;


        };

        var onSaveError = function () {
            vm.isSaving = false;
        };


        vm.downloadTypeList = [{
            'id': 0,
            'value': "NONE"
        }, {
            'id': 1,
            'value': "ALL"
        }, {
            'id': 2,
            'value': "Lock Approved"
        }];
        vm.exclusivesList = [{
            'id': 0,
            'value': "NONE"
        }, {
            'id': 1,
            'value': "BASIC"
        }, {
            'id': 2,
            'value': "MASTER"
        }];

    }
})();
