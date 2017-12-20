(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectsSimilarAddController', ProjectsSimilarAddController);

    ProjectsSimilarAddController.$inject = ['$ngConfirm','$location','$state', '$uibModal', '$http', '$scope', '$rootScope', '$stateParams', 'entity', 'Projects', 'Lookups', 'Contacts', 'User', 'Departments', 'Storage_Disk'];

    function ProjectsSimilarAddController($ngConfirm,$location,$state, $uibModal, $http, $scope, $rootScope, $stateParams, entity, Projects, Lookups, Contacts, User, Departments, Storage_Disk) {


    	var vm = this;
        console.log("ProjectsSimilarAddController");

        vm.projectsDTO = entity;
        console.log("----> "+JSON.stringify(entity));


// GET ALL Related Entities
        vm.users = User.query();
        vm.departmentss = Departments.query();


        vm.downloadType = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "ALL"}, {'id': 2, 'value': "Lock Approved"}];
        vm.exclusives = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "BASIC"}, {'id': 2, 'value': "MASTER"}];
        vm.invoiceCreated = [{'id': 387,'value':'YES'},{'id': 388,'value':'NO'},{'id': 389,'value':'N/A'},{'id': 0,'value':''}]

        vm.projects = {};

        vm.projects.name = '';
        vm.projects.invoiceCreated = 388;
		vm.largestBrick = {};
		console.log("Getting largest brick");
		$http({
			method : 'GET',
			url : 'api/largest/storage-disks'
		}).then(function successCallback(response) {
			vm.largestBrick = response.data;
			console.log("Largest Brick ", vm.largestBrick);
			vm.projects.imageLocation = vm.largestBrick;
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



        //GET RELATED CONTACTS
        $http({
            method: 'GET', url: 'api/contacts/related/' + vm.projectsDTO.projects.id
        }).then(function successCallback(response) {
            vm.relatedContact = response.data;
            console.log("Total Related Contact found  : " + vm.relatedContact.length);
            console.log("----> "+JSON.stringify(vm.relatedContact));
        }, function
            errorCallback(response) {

        });



        // ADD PO / LAB / TALENTS / EXECS

        // PURCHASE ORDERS
        vm.runShowAdd = function () {
            console.log(" Run of Show added ..");
            vm.projectsDTO.projectPurchaseOrderses.push({"id": null, "po_number": null, "po_notes": null});
        };

        vm.runShowRemove = function (index) {
            console.log(" Run of Show removed : " + index);
            vm.projectsDTO.projectPurchaseOrderses.splice(index, 1);
        };

        // LABS
        vm.addlab = function () {
            console.log("lab tasks added");
            vm.projectsDTO.projectLabTaskses.push({'id': null, 'task_name': null});
        };

        vm.removelab = function (index) {
            console.log("lab tasks index : " + index);
            vm.projectsDTO.projectLabTaskses.splice(index, 1);
        };

        // TALENTS
        vm.addTalent = function () {

            vm.projectsDTO.projectRoles.push({
                'id': null,
                "contact": {'fullName': null},
                "relationship_type": "PKO_Tag",
                "soloKillPct": 50,
                "groupKillPct": 25,
                "characterName": "",
                "disabled": false,
                "excSologroup": false
            });
            // get related too.
            /*console.log(" get releated  : ", vm.currrentOBJ.data.id);
			$http({
				method : 'GET',
				url : 'api/contacts/related/' + vm.currrentOBJ.data.id
			}).then(function successCallback(response) {
				vm.relatedContact.push(response.data);
			}, function errorCallback(response) {

			});

			console.log("Related Contact Length : ",vm.relatedContact.length );*/

        };

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

        vm.notify = function(talent) {
			alert("Notification Email sent to : "+talent.contact.fullName);
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
                "deleteAssets": false,
                "watermarkInnerTransparency": 0.00,
                "watermarkOuterTransparency": 0.00,
                "restartRole": "EXEC"
            })
        };
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
                if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.owner')) {
                    console.log("found equal");

                    vm.projects.owner = vm.currrentOBJ.data;

                    console.log(vm.projects.owner.fullName);
                }


                else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.mainContact')) {
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
                }
                else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.unitPublicist')) {
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

                }
                else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.unitPhotographer')) {
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


                }
                else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.labInfo')) {
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
                }
                else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.productCompany')) {
                    vm.projects.productionCompanyContact = vm.currrentOBJ.data;

                }
                else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.parentInfo')) {
                    vm.projects.parentCompanyContact = vm.currrentOBJ.data;
                }
                else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.talents')) {

                    vm.projectsDTO.projectRoles.pop();
                    vm.projectsDTO.projectRoles.push({
                        'id': null,
                        "contact": vm.currrentOBJ.data,
                        "relationship_type": "PKO_Tag",
                        "soloKillPct": 50,
                        "groupKillPct": 25,
                        "characterName": "",
                        "disabled": false,
                        "excSologroup": false
                    });
                    vm.projectsDTO.contactPrivileges.push({
                        "contact": vm.currrentOBJ.data,
                        "exec" : false,
                        "downloadType" : 0,
                        "print" : false,
                        "email" : false,
                        "captioning" : false,
                        "talentManagement" : false,
                        "signoffManagement" : false,
                        "releaseExclude" : false,
                        "vendor" : false,
                        "lockApproveRestriction" : false,
                        "viewSensitive" : false,
                        "exclusives" : 0,
                        "seesUntagged" : false,
                        "hasVideo" : false,
                        "disabled" : false,
                        "datgeditManagement" : false,
                        "priorityPix" : false,
                        "readOnly" : false,
                        "restartColumns" : 2,
                        "restartImageSize" : 'Large',
                        "restartImagesPerPage" : 20,
                        "showFinalizations" : false,
                        "watermark" : false,
                        "internal" : false,
                        "globalAlbum": false,
                        "loginCount": 0,
                        "defaultAlbum": null,
                        "critiqueIt": false,
                        "adhocLink": false,
                        "retouch": false,
                        "fileUpload": false,
                        "deleteAssets": false,
                        "watermarkInnerTransparency": 0.00,
                        "watermarkOuterTransparency": 0.00,
                        "restartRole": 'REVIEWER'
                    });

                 // get related too.
                    /*console.log(" get releated  : ", vm.currrentOBJ.data.id);
					$http({
						method : 'GET',
						url : 'api/contacts/related/' + vm.currrentOBJ.data.id
					}).then(function successCallback(response) {
						vm.relatedContact.push(response.data);
					}, function errorCallback(response) {

					});

					console.log("Related Contact Length : ",vm.relatedContact.length );*/

                }
                else if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.projects.execs')) {
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
                        "deleteAssets": false,
                        "watermarkInnerTransparency": 0.00,
                        "watermarkOuterTransparency": 0.00,
                        "restartRole": 'EXEC'
                    });
                } else if (angular.equals(vm.currrentOBJ.elementID, 'relatedContact')) {
                    console.log("count : " + vm.count);
                    // vm.relatedContacts.push(vm.currrentOBJ.data);
                    // vm.relatedContacts[vm.count].contactB =
					// vm.currrentOBJ.data;
                    vm.relatedContacts.push({"isPrimaryContact": false, "contact_b": vm.currrentOBJ.data});
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

            if(angular.equals(vm.projects.name,'')){
                console.log("Blank")
                alert("Project Name Cannot be blank");
            }else {

                vm.projectsDTO.projects.id = null;
                vm.projects.name = vm.projects.name.toUpperCase();
                vm.projectsDTO.projects.name = vm.projects.name;
                vm.projectsDTO.projects.invoiceCreated = vm.projects.invoiceCreated;

                console.log("ProjectsDTO");
                console.log(JSON.stringify(vm.projectsDTO));

                vm.isSaving = true;


                console.log("Creating Similar   projectsDTO");
                var count = 0;
                for(var i = 0;i<vm.projectsDTO.projectRoles.length;i++){
                    if(vm.projectsDTO.projectRoles[i].relationship_type === 'PKO_Tag');
                    count++;
                }
                vm.projectsDTO.projects.actorsWithRights = count;
                Projects.save(vm.projectsDTO, onSaveSuccess, onSaveError);
            }
        };
        var onSaveSuccess = function (result) {
            $ngConfirm({
                title: 'Success!',
                content: "Project : <strong>"+vm.projects.name+"</strong> has been created",
                type: 'green',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-green',
                        action: function () {
                            $state.go('projects-detail', {id:result.id}, {reload: true});
                        }
                    }
                }
            });
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };



        vm.getCount = function (projects) {
            // console.log("Drive ID : " + JSON.stringify(diskID));
            $http({
                method: 'POST',
                url: 'api/count',
                data: projects
            }).then(function successCallback(response) {

                vm.count = response.data;
                console.log(vm.res);

                alert(vm.count);
            }, function errorCallback(response) {

            });

        };



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
                controllerAs:    'vm',
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

        vm.rename = function(project) {
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

		vm.delete = function() {
			var password = prompt("Enter password: ", "");
			$http({
                method: 'POST',
                url: 'api/project/delete',
                data: password
            }).then(function successCallback(response) {

                vm.success = response.data;
                if(angular.equals(vm.success,1)){
                	alert("Project Delete Started : "+vm.projectsDTO.projects.id);
                }else{
                	alert("Incorrect Password...");
                }

            }, function errorCallback(response) {

            });

			/*
			 * if(angular.equals(retVal,"abcd")){ alert("Password Correct :
			 * "+retVal); }else{ alert("Password Incorrect : "+retVal); }
			 */
		};

        vm.saveAndClose = function () {
            if(angular.equals(vm.projects.name,'')){
                console.log("Blank")
                alert("Project Name Cannot be blank");
            }else {
                vm.projectsDTO.projects.id = null;
                vm.projects.name = vm.projects.name.toUpperCase();
                vm.projectsDTO.projects.name = vm.projects.name;
                vm.projectsDTO.projects.invoiceCreated = vm.projects.invoiceCreated;


                console.log("ProjectsDTO");
                console.log(JSON.stringify(vm.projectsDTO));

                vm.isSaving = true;


                console.log("UPDATING entity projectsDTO");
                var count = 0;
                for(var i = 0;i<vm.projectsDTO.projectRoles.length;i++){
                    if(vm.projectsDTO.projectRoles[i].relationship_type === 'PKO_Tag');
                    count++;
                }
                vm.projectsDTO.projects.actorsWithRights = count;
                Projects.save(vm.projectsDTO, onSaveSuccess2, onSaveError2);

            }

        };
        var onSaveSuccess2 = function (result) {
            console.log('saving project...');
            // $scope.$emit('smartLpcApp:projectsUpdate', result);
            // $uibModalInstance.close(result);
            $ngConfirm({
                title: 'Success!',
                content: "Project : <strong>"+vm.projects.name+"</strong> has been created",
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
            $state.go('projects', {}, {reload: true}); // use for redirecting
            // ...
        };

        var onSaveError2 = function (result) {
            console.log("op : ",result);
            vm.isSaving = false;
        };

        vm.addStatus = function () {
            var status = prompt("Add New Option : ", "");
            console.log("status : "+status);
            if (status === null) {
                alert("No status Entered");
            }else{
                vm.newStatus = {
                    tableName: 'projects',
                    fieldName: 'status_id',
                    textValue: status,
                    id: null
                };

                Lookups.save(vm.newStatus, onSaveSuccess, onSaveError);


            }
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


    }
})();
