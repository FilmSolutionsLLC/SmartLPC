/**
 * Created by macbookpro on 1/23/17.
 */
(function () {
    'use strict';

    angular.module('smartLpcApp').controller('ProjectListController',
        ProjectListController);
    ProjectListController.$inject = ['ContactPrivileges', 'ProjectsSearch',
        'Projects', '$http', '$rootScope', 'Contacts', 'Lookups',
        'Departments', 'User', 'ContactsSearch', 'AlertService',
        '$uibModalInstance', '$scope', '$state'];

    function ProjectListController(ContactPrivileges, ProjectsSearch, Projects,
                                   $http, $rootScope, Contacts, Lookups, Departments, User,
                                   ContactsSearch, AlertService, $uibModalInstance, $scope, $state) {
        console.log("Project Permission Controller");
        var vm = this;

        vm.searchProject = function (query) {
            console.log("search query  : " + query);
            if (angular.isDefined(query)) {
                console.log("Project SEARCH called");

                ProjectsSearch.query({
                    query: query,
                    size: 1000000
                }, onSuccess, onError);
            } else {
                $scope.showLoader = true;
                console.log("Project GET called");
                $http({
                    method: 'GET',
                    url: 'api/idname/projects'
                }).then(
                    function (response) {
                        vm.projects = [];
                        console.log("total projects : "
                            + response.data.length);
                        vm.temp = response.data;
                        //console.log("first project " + JSON.stringify(vm.temp[0][1]));
                        // console.log("first id " + vm.temp[0][0]);
                        for (var i = 0; i < vm.temp.length; i++) {
                            $scope.tempObj = {
                                "id": vm.temp[i][0],
                                "name": vm.temp[i][1]
                            }
                            vm.projects.push($scope.tempObj);
                        }
                    })
            }
        };

        function onSuccess(data) {
            vm.projects = [];
            for (var i = 0; i < data.length; i++) {
                vm.projects.push(data[i].projects);
                // console.log("added" + i);
            }
            $scope.loading = false;
            console.log("Total projects found : " + vm.projects.length);
        }
        ;

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

                console.log("project selected : " + project.id);
            } else {

                console.log("item already exists");
                vm.selectedProjects.splice(
                    vm.selectedProjects.indexOf(project), 1);
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
                console.log("null rootscope saved contact");
                vm.con
            } else {
                console.log("not null");
                console.log("Saved contact  :  "
                    + JSON.stringify($rootScope.savedContact.id));
                $scope.name = $rootScope.savedContact.fullName;
                vm.contacts = $rootScope.savedContact;
                console.log("Saved Contact  ", vm.contacts);
            }
        });

        $rootScope.selectedProjects = [];

        vm.tempExecs = [];
        vm.save = function () {
            // $rootScope.selectedProjects = vm.selectedProjects;
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
                        "critiqueIt": vm.critique,
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
                    }
                    vm.isSaving = true;

                    vm.tempExecs.push(vm.execs);
                    ContactPrivileges.save(vm.execs, onSaveSuccess, onSaveError);

                    console.log("vm.tempExecs length : "+vm.tempExecs.length);



                }
                console.log("Ending here...")
                console.log("final vm.tempExecs length : "+vm.tempExecs.length);
                $rootScope.selectedProjects =  vm.tempExecs;
                console.log("$rootScope.selectedProjects length :" +$rootScope.selectedProjects.length);
                $uibModalInstance.dismiss('cancel');
            }

        };

        var onSaveSuccess = function (result) {
            // $scope.$emit('smartLpcApp:contactPrivilegesUpdate', result);
            console.log("save success...now closing the modal box");

            $rootScope.selectedProjects.push(vm.tempExecs);
            console.log("rootscope selectedProjects length : "+$rootScope.selectedProjects.length);

            // console.log("saved in db: " + result.data.id);
            // vm.isSaving = false;
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
