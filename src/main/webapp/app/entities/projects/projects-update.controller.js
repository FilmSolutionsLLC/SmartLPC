(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectsUpdateController', ProjectsUpdateController);

    ProjectsUpdateController.$inject = ['ContactPrivileges', 'ProjectRoles', '$ngConfirm', '$anchorScroll', '$location', '$state', '$uibModal', '$http', '$scope', '$rootScope', '$stateParams', 'entity', 'Projects', 'Lookups', 'Contacts', 'User', 'Departments', 'Storage_Disk'];

    function ProjectsUpdateController(ContactPrivileges, ProjectRoles, $ngConfirm, $anchorScroll, $location, $state, $uibModal, $http, $scope, $rootScope, $stateParams, entity, Projects, Lookups, Contacts, User, Departments, Storage_Disk) {


        var vm = this;
        console.log("PROJECT UPDATE CONTROLLER");

        vm.projectsDTO = entity;
        console.log("ProjectsDTO : " + JSON.stringify(vm.projectsDTO));


        vm.users = User.query();
        vm.departmentss = Departments.query();

        vm.relatedContact = [];

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

        vm.prevNext = {};
        $http({
            method: 'GET',
            url: 'api/prev/next/' + vm.projectsDTO.projects.id

        }).then(function successCallback(response) {
            vm.prevNext = response.data;
            console.log("Prev Next Projects : " + JSON.stringify(vm.prevNext));
        }, function errorCallback(response) {

        });

        vm.talents = [];

        vm.status = {};
        $http({
            method: 'GET',
            url: 'api/lookups/projects/status'
        }).then(function successCallback(response) {
            vm.status = response.data;
        }, function errorCallback(response) {

        });

        vm.projectType = {};
        $http({
            method: 'GET',
            url: 'api/lookups/projects/type'
        }).then(function successCallback(response) {
            vm.projectType = response.data;
        }, function errorCallback(response) {

        });


        vm.fileType = {};
        $http({
            method: 'GET',
            url: 'api/lookups/projects/filetype'
        }).then(function successCallback(response) {
            vm.fileType = response.data;
        }, function errorCallback(response) {

        });

        vm.labTask = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/project_lab_tasks/task_name_id'
        }).then(function successCallback(response) {
            vm.labTask = response.data;
        }, function errorCallback(response) {

        });

        // ADD PO / LAB / TALENTS / EXECS

        // PURCHASE ORDERS
        vm.runShowAdd = function () {
            console.log(" Run of Show added ..");
            vm.projectsDTO.projectPurchaseOrderses.push({
                "id": null,
                "po_number": null,
                "po_notes": null
            });
        };

        vm.runShowRemove = function (index) {
            console.log(" Run of Show removed : " + index);
            for (var i = 0; i < vm.projectsDTO.projectRoles.length; i++) {

            }
            vm.projectsDTO.projectPurchaseOrderses.splice(index, 1);
        };

        // LABS
        vm.addlab = function () {
            console.log("lab tasks added");
            vm.projectsDTO.projectLabTaskses.push({
                'id': null,
                'task_name': null
            });
        };

        vm.removelab = function (index) {
            console.log("lab tasks index : " + index);
            vm.projectsDTO.projectLabTaskses.splice(index, 1);
        };

        // TALENTS
        // vm.talents = vm.projectsDTO.projectRoles;
        // console.log("Sheldon " , vm.talents.length);


        vm.addTalent = function () {
            console.log("Adding new Talent");
            vm.projectsDTO.projectRoles.push({
                "id": null,
                "contact": {
                    'fullName': null
                },
                "relationship_type": "PKO_Tag",
                "soloKillPct": 50,
                "groupKillPct": 25,
                "characterName": "",
                "disabled": false,
                "excSologroup": false
            });
            // get related too.
            /*console.log(" get releated : ", vm.currrentOBJ.data.id);
            $http({
                method: 'GET', url: 'api/contacts/related/' +
                vm.currrentOBJ.data.id
            }).then(function successCallback(response) {
                vm.relatedContact.push(response.data);
            }, function
                errorCallback(response) {

            });

            console.log("Related Contact Length : ", vm.relatedContact.length);*/

        };

        //GET RELATED CONTACTS
        $http({
            method: 'GET', url: 'api/contacts/related/' + vm.projectsDTO.projects.id
        }).then(function successCallback(response) {
            vm.relatedContact = response.data;
            console.log("Total Related Contact found  : " + vm.relatedContact.length);
        }, function
            errorCallback(response) {

        });


        vm.deleteTalent = [];
        vm.removeTalent = function (index) {
            console.log("removing talent : " + index);


            for (var i = 0; i < vm.projectsDTO.projectRoles.length; i++) {

                if (vm.projectsDTO.projectRoles[i].id == index) {
                    console.log(JSON.stringify(vm.projectsDTO.projectRoles[i]))
                    // remove it
                    if (angular.equals(vm.projectsDTO.projectRoles[i].contact.fullName, null)) {
                        console.log("null");
                        vm.projectsDTO.projectRoles.splice(i, 1);
                    }
                    else if (confirm('Are you sure you want to remove Talent : ' + vm.projectsDTO.projectRoles[i].contact.fullName)) {
                        console.log("Removing project role:" + vm.projectsDTO.projectRoles[i].id);
                        vm.deleteTalent.push(vm.projectsDTO.projectRoles[i].id);
                        vm.projectsDTO.projectRoles.splice(i, 1);

                    } else {
                        // Do nothing!
                    }
                }
            }
        };

        // vm.projectsDTO.projectRoles.splice(index, 1);


        vm.notify = function (talent) {
            alert("Notification Email sent to : " + talent.contact.fullName);
        }
        // EXECS

        vm.addExec = function () {
            vm.projectsDTO.contactPrivileges.push({
                "contact": null,
                "exec": true,
                "downloadType": 0,
                "print": false,
                "email": false,
                "captioning": false,
                "talentManagement": false,
                "signoffManagement": false,
                "releaseExclude": false,
                "vendor": false,
                "lockApproveRestriction": false,
                "viewSensitive": false,
                "exclusives": 0,
                "seesUntagged": false,
                "hasVideo": false,
                "disabled": false,
                "datgeditManagement": false,
                "priorityPix": false,
                "readOnly": false,
                "restartColumns": 2,
                "restartImageSize": 'Large',
                "restartImagesPerPage": 20,
                "showFinalizations": false,
                "watermark": false,
                "internal": false,
                "globalAlbum": false,
                "loginCount": 0,
                "defaultAlbum": null,
                "critiqueIt": false,
                "adhocLink": false,
                "retouch": false,
                "fileUpload": false,
                "deleteAssets": false
            });

            $location.hash('bottom');
            // call $anchorScroll()
            $anchorScroll();
        };
        vm.deleteExec = [];
        vm.removeExec = function (index) {
            console.log("removing contact privilege :" + index);

            for (var i = 0; i < vm.projectsDTO.contactPrivileges.length; i++) {
                console.log("cp :" + vm.projectsDTO.contactPrivileges[i].id);
                if (vm.projectsDTO.contactPrivileges[i].id == index) {
                    console.log("found");
                    if (angular.equals(vm.projectsDTO.contactPrivileges[i].contact, null)) {
                        console.log("null");
                        vm.projectsDTO.contactPrivileges.splice(i, 1);
                    }else if (confirm('Are you sure you want to remove Exec : ' + vm.projectsDTO.contactPrivileges[i].contact.fullName)) {
                        console.log("Removing contact privilege:" + vm.projectsDTO.contactPrivileges[i].id);
                        vm.deleteExec.push(vm.projectsDTO.contactPrivileges[i].id);
                        vm.projectsDTO.contactPrivileges.splice(i, 1);

                    } else {
                        // Do nothing!
                    }

                }
            }
        };


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
                if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.owner')) {
                    console.log("found equal");

                    vm.projectsDTO.projects.owner = vm.currrentOBJ.data;

                    console.log(vm.projectsDTO.projects.owner.fullName);
                } else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.mainContact')) {

                    if (vm.addContact('Main Contact', vm.currrentOBJ.data) == false) {
                        console.log("Got False");
                        console.log("Add new")
                        vm.projectsDTO.projectRoles.push({
                            "contact": vm.currrentOBJ.data,
                            "relationship_type": "Main Contact",
                            "excSologroup": false,
                            "soloKillPct": 50,
                            "groupKillPct": 50,
                            "tertiaryKillPct": 50.0,
                            "disabled": true
                        });
                    } else {
                        console.log("Got True");
                        console.log("Dont Add")
                    }


                } else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.unitPublicist')) {

                    if (vm.addContact('Unit Publicist', vm.currrentOBJ.data) == false) {
                        console.log("Got False");
                        console.log("Add new")
                        vm.projectsDTO.projectRoles.push({
                            "contact": vm.currrentOBJ.data,
                            "relationship_type": "Unit Publicist",
                            "excSologroup": false,
                            "soloKillPct": 50,
                            "groupKillPct": 50,
                            "tertiaryKillPct": 50.0,
                            "disabled": true
                        });
                    } else {
                        console.log("Got True");
                        console.log("Dont Add")
                    }

                } else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.unitPhotographer')) {

                    if (vm.addContact('Unit Photographer', vm.currrentOBJ.data) == false) {
                        console.log("Got False");
                        console.log("Add new")
                        vm.projectsDTO.projectRoles.push({
                            "contact": vm.currrentOBJ.data,
                            "relationship_type": "Unit Photographer",
                            "excSologroup": false,
                            "soloKillPct": 50,
                            "groupKillPct": 50,
                            "tertiaryKillPct": 50.0,
                            "disabled": true
                        });
                    } else {
                        console.log("Got True");
                        console.log("Dont Add")
                    }


                } else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.labInfo')) {

                    if (vm.addContact('Lab', vm.currrentOBJ.data) == false) {
                        console.log("Got False");
                        console.log("Add new")
                        vm.projectsDTO.projectRoles.push({
                            "contact": vm.currrentOBJ.data,
                            "relationship_type": "Lab",
                            "excSologroup": false,
                            "soloKillPct": 50,
                            "groupKillPct": 50,
                            "tertiaryKillPct": 50.0,
                            "disabled": true
                        });
                    } else {
                        console.log("Got True");
                        console.log("Dont Add")
                    }


                } else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.productCompany')) {
                    vm.projectsDTO.projects.productionCompanyContact = vm.currrentOBJ.data;

                } else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.parentInfo')) {
                    vm.projectsDTO.projects.parentCompanyContact = vm.currrentOBJ.data;
                } else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.talents')) {

                    vm.projectsDTO.projectRoles.pop();
                    vm.projectsDTO.projectRoles.push({
                        'id': null,
                        "contact": vm.currrentOBJ.data,
                        "relationship_type": "PKO_Tag",
                        "soloKillPct": 50,
                        "groupKillPct": 25,
                        "characterName": "",
                        "excSologroup": false,
                        "disabled": true
                    });

                    vm.projectsDTO.contactPrivileges.push({
                        "contact": vm.currrentOBJ.data,
                        "exec": false,
                        "downloadType": 0,
                        "print": false,
                        "email": false,
                        "captioning": false,
                        "talentManagement": false,
                        "signoffManagement": false,
                        "releaseExclude": false,
                        "vendor": false,
                        "lockApproveRestriction": false,
                        "viewSensitive": false,
                        "exclusives": 0,
                        "seesUntagged": false,
                        "hasVideo": false,
                        "disabled": false,
                        "datgeditManagement": false,
                        "priorityPix": false,
                        "readOnly": false,
                        "restartColumns": 2,
                        "restartImageSize": 'Large',
                        "restartImagesPerPage": 20,
                        "showFinalizations": false,
                        "watermark": false,
                        "internal": false,
                        "globalAlbum": false,
                        "loginCount": 0,
                        "defaultAlbum": null,
                        "critiqueIt": false,
                        "adhocLink": false,
                        "retouch": false,
                        "fileUpload": false,
                        "deleteAssets": false
                    });
                    /*// get related too.
                    console.log(" get releated  : ", vm.currrentOBJ.data.id);
                    $http({
                        method: 'GET',
                        url: 'api/contacts/related/' + vm.currrentOBJ.data.id
                    }).then(function successCallback(response) {
                        vm.relatedContact.push(response.data);
                        console.log("Related Contact added..");
                    }, function errorCallback(response) {

                    });*/

                    //console.log("Related Contact Length :", vm.relatedContact.length);

                } else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.execs')) {
                    vm.projectsDTO.contactPrivileges.pop();
                    vm.projectsDTO.contactPrivileges.push({
                        "contact": vm.currrentOBJ.data,
                        "exec": true,
                        "downloadType": 0,
                        "print": false,
                        "email": false,
                        "captioning": false,
                        "talentManagement": false,
                        "signoffManagement": false,
                        "releaseExclude": false,
                        "vendor": false,
                        "lockApproveRestriction": false,
                        "viewSensitive": false,
                        "exclusives": 0,
                        "seesUntagged": false,
                        "hasVideo": false,
                        "disabled": false,
                        "datgeditManagement": false,
                        "priorityPix": false,
                        "readOnly": false,
                        "restartColumns": 2,
                        "restartImageSize": 'Large',
                        "restartImagesPerPage": 20,
                        "showFinalizations": false,
                        "watermark": false,
                        "internal": false,
                        "globalAlbum": false,
                        "loginCount": 0,
                        "defaultAlbum": null,
                        "critiqueIt": false,
                        "adhocLink": false,
                        "retouch": false,
                        "fileUpload": false,
                        "deleteAssets": false
                    });
                } else if (angular.equals(vm.currrentOBJ.elementID, 'relatedContact')) {
                    console.log("count : " + vm.count);
                    // vm.relatedContacts.push(vm.currrentOBJ.data);
                    // vm.relatedContacts[vm.count].contactB =
                    // vm.currrentOBJ.data;
                    vm.relatedContacts.push({
                        "isPrimaryContact": false,
                        "contact_b": vm.currrentOBJ.data
                    });
                    vm.count++;

                    console.log("related Contacts size " + vm.relatedContacts.length);
                } else {
                    console.log("not equal..");
                }
                vm.sortProjectRoles();
            }
        });


        vm.addContact = function (contactType, contact) {
            for (var i = 0; i < vm.projectsDTO.projectRoles.length; i++) {
                if (angular.equals(vm.projectsDTO.projectRoles[i].relationship_type, contactType)) {
                    vm.projectsDTO.projectRoles[i].contact = contact;
                    return true;
                }
            }
            return false;
        };
        vm.openModal = function (elementID) {

            console.log("id of textbox : " + elementID);
            // var ctrl = angular.element(id).data('$ngModelController');

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
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
        };


        vm.save = function () {

            console.log("ProjectsDTO");
            console.log(JSON.stringify(vm.projectsDTO));

            vm.isSaving = true;


            console.log("UPDATING entity projectsDTO");
            for (var i = 0; i < vm.deleteTalent.length; i++) {
                console.log("removing project role " + vm.deleteTalent[i]);
                ProjectRoles.delete({id: vm.deleteTalent[i]});
            }

            for (var i = 0; i < vm.deleteExec.length; i++) {
                console.log("removing contact privilege " + vm.deleteExec[i]);
                ContactPrivileges.delete({id: vm.deleteExec[i]});
            }

            Projects.update(vm.projectsDTO, onSaveSuccess, onSaveError);


        };
        var onSaveSuccess = function (result) {
            console.log('saving project...');
            // $scope.$emit('smartLpcApp:projectsUpdate', result);
            // $uibModalInstance.close(result);
            $ngConfirm({
                title: 'Success!',
                content: "Project : <strong>" + vm.projectsDTO.projects.fullName + "</strong> has been updated",
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
            vm.isSaving = false;
            $rootScope.relationships = null;
            $rootScope.execsContactMultiple = null;
            $state.go('projects-detail', {id: vm.projectsDTO.projects.id}, {reload: true});
            // ...
        };

        // send multiple email
        vm.sendMail = function (talents) {

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/mails.html',
                controller: 'MailsController',
                size: 'lg',
                scope: $scope,
                controllerAs: 'vm',
                backdrop: 'static',
                resolve: {


                    talents: function () {

                        return talents;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
        };
        // send single email
        vm.sendIndividualMail = function (talent) {

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/mails.html',
                controller: 'MailsIndividualController',
                size: 'lg',
                scope: $scope,
                controllerAs: 'vm',
                backdrop: 'static',
                resolve: {


                    talent: function () {

                        return talent;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
        };
        /*
		 * console.log(JSON.stringify(vm.projectsDTO)); vm.projectRolesTemp =
		 * []; vm.projectRoles = []; vm.contactPrivileges = [];
		 * vm.projectPurchaseOrders = []; vm.projectLabTaskses = []; vm.tags =
		 * [];
		 *
		 * vm.projects = vm.projectsDTO.projects; vm.projectRolesTemp =
		 * vm.projectsDTO.projectRoles; vm.contactPrivileges =
		 * vm.projectsDTO.contactPrivileges; vm.projectPurchaseOrders =
		 * vm.projectsDTO.projectPurchaseOrderses; vm.projectLabTaskses =
		 * vm.projectsDTO.projectLabTaskses;
		 *
		 * vm.users = User.query(); vm.departmentss = Departments.query();
		 * vm.storage_disks = Storage_Disk.query();
		 *
		 *
		 * vm.downloadType = [{0: "NONE"}, {1: "ALL"}, {2: "Locked Approved"}];
		 * vm.exclusives = [{0: "NONE"}, {1: "BASIC"}, {2: "MASTER"}];
		 *
		 *
		 * vm.talents = []; vm.status = {};
		 *
		 * $http({ method: 'GET', url: 'api/lookups/projects/status'
		 * }).then(function successCallback(response) { vm.status =
		 * response.data; }, function errorCallback(response) {
		 *
		 * });
		 *
		 * vm.projectType = {}; $http({ method: 'GET', url:
		 * 'api/lookups/projects/type' }).then(function
		 * successCallback(response) { vm.projectType = response.data; },
		 * function errorCallback(response) {
		 *
		 * });
		 *
		 *
		 * vm.fileType = {}; $http({ method: 'GET', url:
		 * 'api/lookups/projects/filetype' }).then(function
		 * successCallback(response) { vm.fileType = response.data; }, function
		 * errorCallback(response) {
		 *
		 * });
		 *
		 * vm.labTask = {}; $http({ method: 'GET', url:
		 * 'api/lookups/get/project_lab_tasks/task_name_id' }).then(function
		 * successCallback(response) { vm.labTask = response.data; }, function
		 * errorCallback(response) {
		 *
		 * });
		 *
		 * vm.tags = []; $scope.talents = []; if
		 * (!(angular.equals(vm.projectRolesTemp, []))) { for (var i = 0; i <
		 * vm.projectRolesTemp.length; i++) {
		 *
		 * if (angular.equals(vm.projectRolesTemp[i].relationship_type, "Main
		 * Contact")) { console.log("main contact added"); //vm.mainC =
		 * vm.projectRoles[i]; vm.projectRoles[0] = vm.projectRolesTemp[i]; }
		 * else if (angular.equals(vm.projectRolesTemp[i].relationship_type,
		 * "Unit Publicist")) { console.log("unit publicist added");
		 * vm.projectRoles[1] = vm.projectRolesTemp[i]; } if
		 * (angular.equals(vm.projectRolesTemp[i].relationship_type, "Unit
		 * Photographer")) { console.log("unit photographer added");
		 * vm.projectRoles[2] = vm.projectRolesTemp[i]; } if
		 * (angular.equals(vm.projectRolesTemp[i].relationship_type, "Lab")) {
		 * console.log("labs added"); vm.projectRoles[3] =
		 * vm.projectRolesTemp[i]; } if
		 * (angular.equals(vm.projectRolesTemp[i].relationship_type, "PKO_Tag")) {
		 * console.log("tags added"); vm.talents.push(vm.projectRolesTemp[i]); //
		 * console.log(JSON.stringify(vm.projectRolesTemp[i])); //
		 * console.log("-----> LENGTH of vm.tags" +
		 * JSON.stringify(vm.tags.length)); // console.log("-----> LENGTH of
		 * vm.tags = " + vm.tags.length); $scope.talents.push({'id':
		 * vm.tags.length}); } } } console.log("Purchase Orders total : " +
		 * vm.projectPurchaseOrders.length); console.log("Lab Tasks total : " +
		 * vm.projectLabTaskses.length); console.log("Execs total : " +
		 * vm.contactPrivileges.length); console.log("Project Roles total : " +
		 * vm.projectRoles.length);
		 *
		 * $scope.runShow = []; for (var i = 0; i <
		 * vm.projectPurchaseOrders.length; i++) { console.log("==== > Run of
		 * Show added : " + i); $scope.runShow.push({'id': 'runShow' + i}); }
		 * vm.runShowAdd = function () { console.log(" Run of Show added ..");
		 * var newItemNo = $scope.runShow.length + 1; $scope.runShow.push({'id':
		 * 'runShow' + newItemNo}); };
		 *
		 * vm.runShowRemove = function (index) { console.log(" Run of Show
		 * removed : " + index); $scope.runShow.splice(index, 1); };
		 *
		 * $scope.related = []; for (var i = 0; i < vm.projectLabTaskses.length;
		 * i++) { console.log("lab tasks present : " + i);
		 * $scope.related.push({'id': 'related' + i}); } vm.addlab = function () {
		 * console.log("lab tasks added"); var newItemNo = $scope.related.length +
		 * 1; $scope.related.push({'id': 'related' + newItemNo}); };
		 *
		 * vm.removelab = function (index) { console.log("lab tasks index : " +
		 * index); vm.projectLabTaskses.splice(index, 1);
		 * $scope.related.splice(index, 1); };
		 *
		 * vm.alert = function () { console.log("esdadasdas");
		 * window.alert("Wake up !!!!"); };
		 *
		 *
		 * for (var i = 0; i < vm.tags; i++) { $scope.talents.push({'id': i}); }
		 * vm.addTalent = function () { var talent = $scope.talents.length + 1;
		 * $scope.talents.push({'id': talent}); console.log("-------");
		 * console.log(JSON.stringify($scope.talents)); };
		 *
		 * vm.removeTalent = function (index) { vm.talents.splice(index, 1);
		 * $scope.talents.splice(index, 1) }; $scope.execs = []; for (var i = 0;
		 * i < vm.contactPrivileges.length; i++) { console.log("lab tasks
		 * present : " + i); $scope.execs.push({'id': 'execs' + i}); }
		 * vm.addExec = function () { var exec = $scope.execs.length + 1;
		 * $scope.execs.push({'id': 'execs' + exec}); }; vm.removeExec =
		 * function (index) { vm.contactPrivileges.splice(index, 1);
		 * $scope.execs.splice(index, 1); }
		 *
		 *
		 * vm.count = 0; $rootScope.$watch(function () { return
		 * $rootScope.relationships; }, function () { if
		 * ($rootScope.relationships == null) { console.log("null rootscope"); }
		 * else { console.log("not null");
		 *
		 * vm.currrentOBJ = $rootScope.relationships; console.log("========> " +
		 * JSON.stringify(vm.currentOBJ)); if
		 * (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.owner')) {
		 * console.log("found equal");
		 *
		 * vm.projects.owner = vm.currrentOBJ.data;
		 *
		 * console.log(vm.projects.owner.fullName); }
		 *
		 *
		 * else if (angular.equals(vm.currrentOBJ.elementID,
		 * 'field_vm.projects.mainContact')) { vm.projectRoles[0] = {"contact":
		 * vm.currrentOBJ.data, "relationship_type": "Main Contact"}; } else if
		 * (angular.equals(vm.currrentOBJ.elementID,
		 * 'field_vm.projects.unitPublicist')) { vm.projectRoles[1] =
		 * {"contact": vm.currrentOBJ.data, "relationship_type": "Unit
		 * Publicist"}; } else if (angular.equals(vm.currrentOBJ.elementID,
		 * 'field_vm.projects.unitPhotographer')) { vm.projectRoles[2] =
		 * {"contact": vm.currrentOBJ.data, "relationship_type": "Unit
		 * Photographer"}; } else if (angular.equals(vm.currrentOBJ.elementID,
		 * 'field_vm.projects.labInfo')) { vm.projectRoles[3] = {"contact":
		 * vm.currrentOBJ.data, "relationship_type": "Lab"}; } else if
		 * (angular.equals(vm.currrentOBJ.elementID,
		 * 'field_vm.projects.productCompany')) {
		 * vm.projects.productionCompanyContact = vm.currrentOBJ.data; } else if
		 * (angular.equals(vm.currrentOBJ.elementID,
		 * 'field_vm.projects.parentInfo')) { vm.projects.parentCompanyContact =
		 * vm.currrentOBJ.data; } else if
		 * (angular.equals(vm.currrentOBJ.elementID,
		 * 'field_vm.projects.talents')) { vm.talents.push({ "contact":
		 * vm.currrentOBJ.data, "relationship_type": "PKO_Tag", "soloKillPct":
		 * 50, "groupKillPct": 25, "characterName": "", "disabled": false }); }
		 * else if (angular.equals(vm.currrentOBJ.elementID,
		 * 'field_vm.projects.execs')) { vm.execss.push({ "contact":
		 * vm.currrentOBJ.data, "exec": true, "downloadType": 0, "print": false,
		 * "email": false, "captioning": false, "talentManagement": false,
		 * "signoffManagement": false, "releaseExclude": false, "vendor": false,
		 * "lockApproveRestriction": false, "viewSensitive": false,
		 * "exclusives": false, "seesUntagged": false, "hasVideo": false,
		 * "disabled": false, "datgeditManagement": false, "priorityPix": false,
		 * "readOnly": false, "restartColumns": 2, "restartImageSize": 'Large',
		 * "restartImagesPerPage": 20, "showFinalizations": false, "watermark":
		 * false, "internal": false
		 *
		 * }); } else if (angular.equals(vm.currrentOBJ.elementID,
		 * 'relatedContact')) { console.log("count : " + vm.count);
		 * //vm.relatedContacts.push(vm.currrentOBJ.data); //
		 * vm.relatedContacts[vm.count].contactB = vm.currrentOBJ.data;
		 * vm.relatedContacts.push({"isPrimaryContact": false, "contact_b":
		 * vm.currrentOBJ.data}); vm.count++;
		 *
		 * console.log("related Contacts size " + vm.relatedContacts.length); }
		 * else { console.log("not equal.."); } } });
		 *
		 *
		 * vm.openModal = function (elementID) {
		 *
		 * console.log("id of textbox : " + elementID); //var ctrl =
		 * angular.element(id).data('$ngModelController');
		 *
		 * var modalInstance = $uibModal.open({
		 *
		 * templateUrl: 'app/entities/contacts/simpleModal.html', controller:
		 * 'SimpleController', size: 'lg', scope: $scope, controllerAs: 'vm',
		 * backdrop: 'static', resolve: { sendID: function () { return
		 * elementID; }, translatePartialLoader: ['$translate',
		 * '$translatePartialLoader', function ($translate,
		 * $translatePartialLoader) {
		 * $translatePartialLoader.addPart('contacts');
		 * $translatePartialLoader.addPart('projects');
		 * $translatePartialLoader.addPart('global'); return
		 * $translate.refresh(); }] } }) }; //
		 * console.log(JSON.stringify(vm.projectsDTO)); vm.load = function (id) {
		 * Projects.get({id: id}, function (result) { vm.projectsDTO = result;
		 * vm.projects = vm.projectsDTO.projects; vm.projectRoles =
		 * vm.projectsDTO.projectRoles; vm.contactPrivileges =
		 * vm.projectsDTO.contactPrivileges; vm.projectPurchaseOrders =
		 * vm.projectsDTO.projectPurchaseOrders; vm.projectLabTaskses =
		 * vm.projectsDTO.projectLabTaskses;
		 *
		 * }); }; var unsubscribe = $rootScope.$on('smartLpcApp:projectsUpdate',
		 * function (event, result) { vm.projects = result; });
		 * $scope.$on('$destroy', unsubscribe);
		 *
		 * vm.save = function () { /!* console.log(" projectRoles : " +
		 * JSON.stringify(vm.projectRoles)); console.log(" talents : " +
		 * JSON.stringify(vm.talents)); console.log(" contactPrivileges : " +
		 * JSON.stringify(vm.contactPrivileges)); console.log(" project : " +
		 * JSON.stringify(vm.projects)); console.log(" labtask : " +
		 * JSON.stringify(vm.labs)); !/ if ($rootScope.isTemplate == true) {
		 * console.log("This is template"); vm.projects.id = null; }
		 * vm.projectsDTO = { "projects": vm.projects,
		 * "projectPurchaseOrderses": vm.projectPurchaseOrderses,
		 * "projectRoles": vm.projectRoles, "projectLabTaskses":
		 * vm.projectLabTaskses, "contactPrivileges": vm.contactPrivileges };
		 * console.log("ProjectsDTO");
		 * //console.log(JSsON.stringify(vm.projectsDTO));
		 * console.log("Projects: " + JSON.stringify(vm.projects));
		 *
		 * vm.isSaving = true;
		 *
		 * if (vm.projects.id !== null) { console.log("UPDATING entity
		 * projectsDTO"); Projects.update(vm.projectsDTO, onSaveSuccess,
		 * onSaveError); } else {
		 *
		 * console.log("==========================================");
		 * console.log("==========================================");
		 * console.log(JSON.stringify(vm.projectsDTO));
		 * Projects.save(vm.projectsDTO, onSaveSuccess, onSaveError); } }; var
		 * onSaveSuccess = function (result) { console.log('saving project...');
		 * $scope.$emit('smartLpcApp:projectsUpdate', result); //
		 * $uibModalInstance.close(result); vm.isSaving = false;
		 * $state.go('projects', {}, {reload: true});// use for redirecting ... };
		 *
		 * var onSaveError = function () { vm.isSaving = false; };
		 *
		 */
        vm.getCount = function (projects) {
            // console.log("Drive ID : " + JSON.stringify(diskID));
            $http({
                method: 'POST',
                url: 'api/count',
                data: projects
            }).then(function successCallback(response) {

                vm.count = response.data;
                console.log(vm.res);

                $ngConfirm({
                    title: 'Size of Project in GB',
                    //content: "<strong>"+vm.count+" Gb</strong> ",
                    content: "<strong>100 Gb</strong> <br>(temporary since drives have not mounted)",
                    type: 'red',
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

            }, function errorCallback(response) {

            });

        }


        vm.getTags = function (id) {
            console.log("id of textbox : " + id);
            // var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/update-tags.html',
                controller: 'UpdateTagsController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    id: function () {
                        return id;
                    },
                    projectID: function () {
                        return vm.projectsDTO.projects.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
        };
        vm.getPrivileges = function (id) {
            console.log("id of textbox : " + id);
            // var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/update-privileges.html',
                controller: 'UpdatePrivilegesController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    id: function () {
                        return id;
                    },
                    projectID: function () {
                        return vm.projectsDTO.projects.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
        }
        vm.getAlbums = function (id) {
            console.log("id of textbox : " + id);
            // var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/update-albums.html',
                controller: 'UpdateAlbumsController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    id: function () {
                        return id;
                    },
                    projectID: function () {
                        return vm.projectsDTO.projects.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
        };
        vm.editPrivilege = function (id) {
            console.log("id of textbox : " + id);
            // var ctrl = angular.element(id).data('$ngModelController');
            console.log("Project : "+vm.projectsDTO.projects.id);

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/individual-privilege.html',
                controller: 'IndividualPrivilegesController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    contact: function () {
                        return id;
                    },
                    project: function () {
                        return vm.projectsDTO.projects.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
        };

        vm.rename = function (project) {
            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/rename.html',
                controller: 'RenameController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    project: function () {
                        return vm.projectsDTO.projects;
                    },

                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
        };

        vm.delete = function () {
            var password = prompt("Enter password: ", "");
            console.log("Password : " + password);
            if (password === "") {
                alert("No Password Entered");
            } else {
                $http({
                    method: 'POST',
                    url: 'api/project/delete',
                    data: password
                }).then(function successCallback(response) {

                    vm.success = response.data;
                    if (angular.equals(vm.success, 1)) {
                        alert("Project Delete Started : " + vm.projectsDTO.projects.id);
                        Projects.delete({id: vm.projectsDTO.projects.id});
                        $state.go('projects', {}, {reload: true});
                    } else {
                        alert("Incorrect Password...");
                    }

                }, function errorCallback(response) {

                });
            }

            /*
			 * if(angular.equals(retVal,"abcd")){ alert("Password Correct :
			 * "+retVal); }else{ alert("Password Incorrect : "+retVal); }
			 */
        };


        // vm.talents = [];


        vm.openMultipleExecs = function (elementID) {

            console.log("multiple Execs");
            // var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/multiple-execs.html',
                controller: 'MultiExecsController',
                size: 'xl',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    sendID: function () {
                        return elementID;
                    },
                    translatePartialLoader: ['$translate',
                        '$translatePartialLoader',
                        function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('contacts');
                            $translatePartialLoader.addPart('projects');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }
                    ]
                }
            })
        };


        $rootScope.$watch(
            function () {
                return $rootScope.execsContactMultiple;
            },
            function () {
                if ($rootScope.execsContactMultiple == null) {

                    console
                        .log("null $rootScope.execsContactMultiple ");
                } else {
                    console
                        .log(" $rootScope.execsContactMultiple added to execss");

                    for (var i = 0; i < $rootScope.execsContactMultiple.length; i++) {
                        // vm.execss.push($rootScope.execsContactMultiple[i]);

                        vm.projectsDTO.contactPrivileges.push($rootScope.execsContactMultiple[i]);
                        console.log("added execss " + i);
                        // var exec = $scope.execs.length + 1;
                        // $scope.execs.push({'id' : 'execs' + exec });
                    }

                }
            });


        vm.enableDisableProject = function (action) {
            $ngConfirm({
                title: 'Warning!',
                content: "<small>Are You Sure You Want to <strong>" + action + "</strong> the Project </small>: <strong>" + vm.projectsDTO.projects.fullName + "</strong>",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-green',
                        action: function () {
                            console.log("YOU DISABLED PROJECT");
                            $http({
                                method: 'GET',
                                //url: 'api/disableproject/' + vm.projectsDTO.projects.id,
                                url: 'api/disableproject',
                                params: {
                                    id: vm.projectsDTO.projects.id,
                                    action: action
                                }
                            }).then(function successCallback(response) {
                                //vm.relatedContact.push(response.data);
                            }, function errorCallback(response) {

                            });
                        }
                    },
                    close: {
                        text: 'Cancel',
                        btnClass: 'btn-red',
                        action: function () {
                            // closes the modal
                            console.log("YOU PRESSED CANCEL");
                        }
                    }
                }
            });
        };

        $scope.isGeneric = function (tags) {
            return tags.contact.fullName !== 'generic pkotag';
        }

        vm.editTags = function (id) {
            console.log("id of project role : " + id);
            // var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/project-roles/project-roles-dialog.html',
                controller: 'ProjectRolesDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'md',
                resolve: {
                    id: function () {
                        return id;
                    },
                    entity: function () {
                        return null;
                    },
                    projectID: function () {
                        return vm.projectsDTO.projects.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
                /*resolve: {
                    entity: ['ProjectRoles', function(ProjectRoles) {
                        return ProjectRoles.get({id : $stateParams.id}).$promise;
                    }]
                }*/
            });
        };


        vm.mainC = {};
        vm.uPub = {};
        vm.uPhoto = {};
        vm.lab = {};

        vm.sortProjectRoles = function () {
            for (var i = 0; i < vm.projectsDTO.projectRoles.length; i++) {
                console.log("Relationship " + i + "  --> " + vm.projectsDTO.projectRoles[i].relationship_type);
                if (angular.equals(vm.projectsDTO.projectRoles[i].relationship_type, 'Main Contact')) {
                    vm.mainC = vm.projectsDTO.projectRoles[i];
                } else if (angular.equals(vm.projectsDTO.projectRoles[i].relationship_type, 'Unit Publicist')) {
                    vm.uPub = vm.projectsDTO.projectRoles[i];
                } else if (angular.equals(vm.projectsDTO.projectRoles[i].relationship_type, 'Unit Photographer')) {
                    vm.uPhoto = vm.projectsDTO.projectRoles[i];
                } else if (angular.equals(vm.projectsDTO.projectRoles[i].relationship_type, 'Lab')) {
                    vm.lab = vm.projectsDTO.projectRoles[i];
                }
            }
        };
        vm.sortProjectRoles();


        vm.saveAndClose = function () {

            console.log("ProjectsDTO");
            console.log(JSON.stringify(vm.projectsDTO));

            vm.isSaving = true;


            console.log("UPDATING entity projectsDTO");
            for (var i = 0; i < vm.deleteTalent.length; i++) {
                console.log("removing project role " + vm.deleteTalent[i]);
                ProjectRoles.delete({id: vm.deleteTalent[i]});
            }

            for (var i = 0; i < vm.deleteExec.length; i++) {
                console.log("removing contact privilege " + vm.deleteExec[i]);
                ContactPrivileges.delete({id: vm.deleteExec[i]});
            }

            Projects.update(vm.projectsDTO, onSaveSuccess2, onSaveError);


        };
        var onSaveSuccess2 = function (result) {
            console.log('saving project...');
            // $scope.$emit('smartLpcApp:projectsUpdate', result);
            // $uibModalInstance.close(result);
            $ngConfirm({
                title: 'Success!',
                content: "Project : <strong>" + vm.projectsDTO.projects.fullName + "</strong> has been updated",
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
            vm.isSaving = false;
            $rootScope.relationships = null;
            $rootScope.execsContactMultiple = null;
            $state.go('projects', {}, {reload: true}); // use for redirecting
            // ...
        };

        vm.saveAndNext = function () {

            console.log("ProjectsDTO");
            console.log(JSON.stringify(vm.projectsDTO));

            vm.isSaving = true;


            console.log("UPDATING entity projectsDTO");
            for (var i = 0; i < vm.deleteTalent.length; i++) {
                console.log("removing project role " + vm.deleteTalent[i]);
                ProjectRoles.delete({id: vm.deleteTalent[i]});
            }

            for (var i = 0; i < vm.deleteExec.length; i++) {
                console.log("removing contact privilege " + vm.deleteExec[i]);
                ContactPrivileges.delete({id: vm.deleteExec[i]});
            }

            Projects.update(vm.projectsDTO, onSaveSuccess3, onSaveError);


        };

        var onSaveSuccess3 = function (result) {
            console.log('saving project...');
            // $scope.$emit('smartLpcApp:projectsUpdate', result);
            // $uibModalInstance.close(result);
            $ngConfirm({
                title: 'Success!',
                content: "Project : <strong>" + vm.projectsDTO.projects.fullName + "</strong> has been updated",
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
            vm.isSaving = false;
            $rootScope.relationships = null;
            $rootScope.execsContactMultiple = null;
            $state.go('projects.edit', {id: vm.prevNext.next}, {reload: true}); // use for redirecting
            // ...
        };

        var onSaveError = function () {
            $rootScope.relationships = null;
            $rootScope.execsContactMultiple = null;
            $ngConfirm({
                title: 'Error!',
                content: "Project : <strong>" + vm.projectsDTO.projects.fullName + "</strong> was not updated",
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
            vm.isSaving = false;
        };


        vm.clearRestart = function () {
            $ngConfirm({
                title: 'Warning!',
                content: "Restart Data for all users on this project will be deleted",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-red',
                        action: function () {

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

        vm.enableEXT = function () {
            $ngConfirm({
                title: 'Warning!',
                content: "Are you sure you want to <strong>ENABLE</strong> ALL Executive users for this project?",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-red',
                        action: function () {
                            // enable
                            $http({
                                method: 'GET',
                                url: 'api/enable/execs/'+vm.projectsDTO.projects.id,
                            }).then(function successCallback(response) {
                                for(var i=0;i<vm.projectsDTO.contactPrivileges.length;i++){
                                    if(vm.projectsDTO.contactPrivileges[i].internal === false){
                                        vm.projectsDTO.contactPrivileges[i].disabled = false;
                                    }
                                }
                                alert("Successfully Enabled All Execs");
                            }, function errorCallback(response) {
                                alert("Error in Enabling Execs");
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
        vm.disableEXT = function () {
            $ngConfirm({
                title: 'Warning!',
                content: "Are you sure you want to <strong>DISABLE</strong> ALL Executive Users for this project?",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-red',
                        action: function () {
                            $http({
                                method: 'GET',
                                url: 'api/disable/execs/'+vm.projectsDTO.projects.id,
                            }).then(function successCallback(response) {
                                for(var i=0;i<vm.projectsDTO.contactPrivileges.length;i++){
                                    if(vm.projectsDTO.contactPrivileges[i].internal === false){
                                        vm.projectsDTO.contactPrivileges[i].disabled = true;
                                    }
                                }
                                alert("Successfully Disabled All Execs");
                            }, function errorCallback(response) {
                                alert("Error in Disabling Execs");
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

        vm.addStatus = function () {
            var status = prompt("Add New Option : ", "");
            console.log("status : " + status);
            if (status === null) {
                alert("No status Entered");
            } else {
                vm.newStatus = {
                    tableName: 'projects',
                    fieldName: 'status_id',
                    textValue: status,
                    id: null
                };

                Lookups.save(vm.newStatus, onSaveSuccess10, onSaveError10);


            }
        };
        var onSaveSuccess10 = function (result) {
            vm.status.push(result);
            console.log("GOT NEW STATUS : " + JSON.stringify(result));
            alert("New Status Created")
        };

        var onSaveError10 = function () {

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
    }
})();
