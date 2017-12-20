(function() {
	'use strict';

	angular.module('smartLpcApp').controller('ProjectsAddController',
			ProjectsAddController);

	ProjectsAddController.$inject = [ '$ngConfirm','$uibModal', '$rootScope', '$state',
			'$scope', '$stateParams', 'entity', 'Projects', 'Lookups',
			'Contacts', 'User', 'Departments', 'Storage_Disk', '$http' ];

	function ProjectsAddController($ngConfirm,$uibModal, $rootScope, $state, $scope,
			$stateParams, entity, Projects, Lookups, Contacts, User,
			Departments, Storage_Disk, $http) {


		console.log("Project Add Controller");

		$scope.addImages = function() {
			var f = document.getElementById('file').files[0], r = new FileReader();
			$scope.logos = f;

			r.onloadend = function(e) {
				var data = e.target.result;
				// send your binary data via $http or $resource or do anything
				// else with it
				console.log(data);

				var res = $http.post('api/logos', f);
				res.success(function(data, status, headers, config) {
					console.log("successs......");
					alert("Logo Saved");
				});
				res.error(function(data, status, headers, config) {
					alert("failed");
				});
			}
			r.readAsBinaryString(f);
			console.log("Images added : " + r.readAsBinaryString);
		}

		console.log("root scope : " + $rootScope.xyz);
		var vm = this;
		vm.projects = entity;
		vm.lookupss = Lookups.query();

		vm.users = User.query();
		vm.departmentss = Departments.query();

		vm.load = function(id) {
			Projects.get({
				id : id
			}, function(result) {
				vm.projects = result;
			});
		};


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


		vm.status = {};
		$http({
			method : 'GET',
			url : 'api/lookups/projects/status'
		}).then(function successCallback(response) {
			vm.status = response.data;
		}, function errorCallback(response) {

		});

		vm.projectType = {};
		$http({
			method : 'GET',
			url : 'api/lookups/projects/type'
		}).then(function successCallback(response) {
			vm.projectType = response.data;
		}, function errorCallback(response) {

		});

		vm.fileType = {};
		$http({
			method : 'GET',
			url : 'api/lookups/projects/filetype'
		}).then(function successCallback(response) {
			vm.fileType = response.data;
		}, function errorCallback(response) {

		});

		vm.labTask = {};
		$http({
			method : 'GET',
			url : 'api/lookups/get/project_lab_tasks/task_name_id'
		}).then(function successCallback(response) {
			vm.labTask = response.data;
		}, function errorCallback(response) {

		});


		vm.projectPurchaseOrderses = [];
		vm.projectRoles = [];
		vm.projectLabTaskses = [];
		vm.contactPrivileges = [];

		vm.talents = [];
		vm.execss = [];

		vm.projects.name = '';
		vm.projects.fullName = '';
		vm.projects.alfrescoTitle1 = '';
		vm.projects.alfrescoTitle2 = '';

		String.prototype.toCamelCase = function() {
			return this.replace(/^([A-Z])|\s(\w)/g, function(match, p1, p2,
					offset) {
				if (p2)
					return p2.toUpperCase();
				return p1.toLowerCase();
			});
		};

		String.prototype.capitalizeFirstLetter = function() {
			return this.charAt(0).toUpperCase() + this.slice(1);
		}

		vm.setProjectName = function() {

			var z = vm.projects.name.split(/[ ,]+/);

			var y = [];
			if (z.length > 1) {
				for (var i = 0; i < z.length; i++) {
					y.push(z[i].capitalizeFirstLetter());

				}

				var fullName = y.join(" ");
				var alfrescoTitle1 = y.join("_").concat("_Proj");
				var alfrescoTitle2 = y.join("_");

				vm.projects.fullName = fullName;
				vm.projects.alfrescoTitle1 = alfrescoTitle1;
				vm.projects.alfrescoTitle2 = alfrescoTitle2;

			} else {
				vm.projects.fullName = vm.projects.name.capitalizeFirstLetter();
				vm.projects.alfrescoTitle1 = vm.projects.name
						.capitalizeFirstLetter().concat("_Proj");
				vm.projects.alfrescoTitle2 = vm.projects.name
						.capitalizeFirstLetter();

			}

		};

		vm.save = function() {

			vm.projects.name = vm.projects.name.toUpperCase();

			for (var i = 0; i < vm.talents.length; i++) {
				vm.projectRoles.push(vm.talents[i]);
			}
			for (var i = 0; i < vm.execss.length; i++) {
				vm.contactPrivileges.push(vm.execss[i]);
			}
			// vm.projectRoles.concat(vm.talents);
            vm.projects.actorsWithRights   = vm.talents.length;


			vm.projectsDTO = {
				"projects" : vm.projects,
				"projectPurchaseOrderses" : vm.projectPurchaseOrderses,
				"projectRoles" : vm.projectRoles,
				"projectLabTaskses" : vm.projectLabTaskses,
				"contactPrivileges" : vm.contactPrivileges
			};
			vm.isSaving = true;
			if (vm.projects.id !== null) {
				Projects.update(vm.projects, onSaveSuccess, onSaveError);
			} else {

				console.log("==========================================");
				console.log("==========================================");
				console.log(JSON.stringify(vm.projectsDTO));
				Projects.save(vm.projectsDTO, onSaveSuccess, onSaveError);

			}
		};

        var onSaveSuccess = function(result) {
            console.log('saving project...');
            //$scope.$emit('smartLpcApp:projectsUpdate', result);
            // $uibModalInstance.close(result);
            $ngConfirm({
                title: 'Success!',
                content: "Project : <strong>"+vm.projects.fullName+"</strong> has been created",
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



        vm.clear = function() {
			// $uibModalInstance.dismiss('cancel');
		};

		vm.datePickerOpenStatus = {};
		vm.datePickerOpenStatus.startDate = false;
		vm.datePickerOpenStatus.endDate = false;
		vm.datePickerOpenStatus.createdDate = false;
		vm.datePickerOpenStatus.updatedDate = false;
		vm.datePickerOpenStatus.shootDate = false;
		vm.datePickerOpenStatus.tagDate = false;
		vm.datePickerOpenStatus.reminderDate = false;

		vm.openCalendar = function(date) {
			vm.datePickerOpenStatus[date] = true;
		};

		$scope.runShow = [];
		vm.runShowAdd = function() {
			console.log("added ..");
			var newItemNo = $scope.runShow.length + 1;
			$scope.runShow.push({
				'id' : 'runShow' + newItemNo
			});
		};

		vm.runShowRemove = function(index) {
			console.log("index : " + index);
			$scope.runShow.splice(index, 1);
		};

		$scope.related = [];
		vm.addlab = function() {
			console.log("added ..");
			var newItemNo = $scope.related.length + 1;
			$scope.related.push({
				'id' : 'related' + newItemNo
			});
		};

		vm.removelab = function(index) {
			console.log("index : " + index);
			$scope.related.splice(index, 1);
		};

            vm.alert = function() {
                console.log("esdadasdas");
                window.alert("Wake up !!!!");
            };

		$scope.talents = [];
		vm.addTalent = function() {
			var talent = $scope.talents.length + 1;
			$scope.talents.push({
				'id' : talent
			});
			console.log("-------");
			console.log(JSON.stringify($scope.talents));
		};

		vm.removeTalent = function(index) {
			console.log("removing talent : " + index);
			vm.talents.splice(index, 1);
			$scope.talents.splice(index, 1)
		};


		$scope.execs = [];

		vm.addExec = function() {
		    /*console.log("Adding exec");
			var exec = $scope.execs.length + 1;
			$scope.execs.push({
				'id' : 'execs' + exec,
                'exec': true
			});*/

            vm.execss.push({
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
                "restartRole": 'EXEC'
            });

        };

		vm.removeExec = function(index) {
			vm.execss.splice(index, 1);
			//$scope.execs.splice(index, 1);


		};

		vm.talents = [];

		vm.execss = [];

		vm.relatedContact = [];

		$rootScope
				.$watch(
						function() {
							return $rootScope.execsContactMultiple;
						},
						function() {
							if ($rootScope.execsContactMultiple == null) {

								console
										.log("null $rootScope.execsContactMultiple ");
							} else {
								console
										.log(" $rootScope.execsContactMultiple added to execss");

								for (var i = 0; i < $rootScope.execsContactMultiple.length; i++) {

									vm.execss
											.push($rootScope.execsContactMultiple[i]);
									console.log("added execss " + i);
									var exec = $scope.execs.length + 1;
									$scope.execs.push({
										'id' : 'execs' + exec
									});
								}

							}
						});

		vm.count = 0;

		$rootScope.$watch(function() {
			return $rootScope.relationships;
		}, function() {
			if ($rootScope.relationships == null) {
				console.log("null rootscope");

			} else {
				console.log("not null");

				vm.currrentOBJ = $rootScope.relationships;
				console.log("========> " + JSON.stringify(vm.currentOBJ));
				if (angular.equals(vm.currrentOBJ.elementID,
						'field_vm.projects.owner')) {
					console.log("found equal");

					vm.projects.owner = vm.currrentOBJ.data;
					console.log(JSON.stringify(vm.projects.owner));
					console.log(vm.projects.owner.fullName);
				}

				else if (angular.equals(vm.currrentOBJ.elementID,
						'field_vm.projects.mainContact')) {
					vm.projectRoles[0] = {
						"contact" : vm.currrentOBJ.data,
						"relationship_type" : "Main Contact",
                        "disabled" : true,
                        "soloKillPct": 50,
                        "groupKillPct": 50,
                        "tertiaryKillPct": 50.0,
                        "excSologroup": false,
					};

				} else if (angular.equals(vm.currrentOBJ.elementID,
						'field_vm.projects.unitPublicist')) {
					vm.projectRoles[1] = {
						"contact" : vm.currrentOBJ.data,
						"relationship_type" : "Unit Publicist",
                        "disabled" : true,
                        "soloKillPct": 50,
                        "groupKillPct": 50,
                        "tertiaryKillPct": 50.0,
                        "excSologroup": false,
					};

				} else if (angular.equals(vm.currrentOBJ.elementID,
						'field_vm.projects.unitPhotographer')) {
					vm.projectRoles[2] = {
						"contact" : vm.currrentOBJ.data,
						"relationship_type" : "Unit Photographer",
                        "disabled" : true,
                        "soloKillPct": 50,
                        "groupKillPct": 50,
                        "tertiaryKillPct": 50.0,
                        "excSologroup": false,
					};

				} else if (angular.equals(vm.currrentOBJ.elementID,
						'field_vm.projects.labInfo')) {
					vm.projectRoles[3] = {
						"contact" : vm.currrentOBJ.data,
						"relationship_type" : "Lab",
                        "disabled" : true,
                        "soloKillPct": 50,
                        "groupKillPct": 50,
                        "tertiaryKillPct": 50.0,
                        "excSologroup": false,
					};

				} else if (angular.equals(vm.currrentOBJ.elementID,
						'field_vm.projects.productCompany')) {
					vm.projects.productionCompanyContact = vm.currrentOBJ.data;

				} else if (angular.equals(vm.currrentOBJ.elementID,
						'field_vm.projects.parentInfo')) {
					vm.projects.parentCompanyContact = vm.currrentOBJ.data;
				} else if (angular.equals(vm.currrentOBJ.elementID,
						'field_vm.projects.talents')) {
					vm.talents.push({
						"contact" : vm.currrentOBJ.data,
						"relationship_type" : "PKO_Tag",
						"soloKillPct" : 50,
						"groupKillPct" : 25,
						"characterName" : "",
						"disabled" : true,
                        "excSologroup": false,
						"welcomeMessage" : ""
					});


                    vm.execss.push({
                        "contact" : vm.currrentOBJ.data,
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
					console.log(" get releated  : ", vm.currrentOBJ.data.id);
					$http({
						method : 'GET',
						url : 'api/contacts/related/' + vm.currrentOBJ.data.id
					}).then(function successCallback(response) {
						vm.relatedContact.push(response.data);
					}, function errorCallback(response) {

					});

					console.log("Related Contact Length : ",
							vm.relatedContact.length);
				} else if (angular.equals(vm.currrentOBJ.elementID,
						'field_vm.projects.execs')) {
				    vm.execss.pop();
					vm.execss.push({
						"contact" : vm.currrentOBJ.data,
						"exec" : true,
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
                        "restartRole": 'EXEC'
					});
				} else if (angular.equals(vm.currrentOBJ.elementID,
						'relatedContact')) {
					console.log("count : " + vm.count);
					// vm.relatedContacts.push(vm.currrentOBJ.data);
					// vm.relatedContacts[vm.count].contactB =
					// vm.currrentOBJ.data;
					vm.relatedContacts.push({
						"isPrimaryContact" : false,
						"contact_b" : vm.currrentOBJ.data
					});
					vm.count++;

					console.log("related Contacts size "
							+ vm.relatedContacts.length);
				} else {
					console.log("not equal..");
				}
			}
		});

		vm.openModal = function(elementID) {

			console.log("id of textbox : " + elementID);
			// var ctrl = angular.element(id).data('$ngModelController');

			var modalInstance = $uibModal.open({

				templateUrl : 'app/entities/contacts/simpleModal.html',
				controller : 'SimpleController',
				size : 'lg',
				scope : $scope,
				controllerAs : 'vm',
				backdrop : 'static',
				resolve : {
					sendID : function() {
						return elementID;
					},
					translatePartialLoader : [ '$translate',
							'$translatePartialLoader',
							function($translate, $translatePartialLoader) {
								$translatePartialLoader.addPart('contacts');
								$translatePartialLoader.addPart('projects');
								$translatePartialLoader.addPart('global');
								return $translate.refresh();
							} ]
				}
			})
		};

		vm.openMultipleExecs = function(elementID) {

			console.log("multiple Execs");
			// var ctrl = angular.element(id).data('$ngModelController');

			var modalInstance = $uibModal.open({

				templateUrl : 'app/entities/projects/multiple-execs.html',
				controller : 'MultiExecsController',
				size : 'xl',
				scope : $scope,
				controllerAs : 'vm',
				resolve : {
					sendID : function() {
						return elementID;
					},
					translatePartialLoader : [ '$translate',
							'$translatePartialLoader',
							function($translate, $translatePartialLoader) {
								$translatePartialLoader.addPart('contacts');
								$translatePartialLoader.addPart('projects');
								$translatePartialLoader.addPart('global');
								return $translate.refresh();
							} ]
				}
			})
		};



        vm.saveAndClose = function() {

            vm.projects.name = vm.projects.name.toUpperCase();


            for (var i = 0; i < vm.talents.length; i++) {
                vm.projectRoles.push(vm.talents[i]);
            }
            for (var i = 0; i < vm.execss.length; i++) {
                vm.contactPrivileges.push(vm.execss[i]);
            }
            // set actors with rights
            vm.projects.actorsWithRights   = vm.talents.length;

            vm.projectsDTO = {
                "projects" : vm.projects,
                "projectPurchaseOrderses" : vm.projectPurchaseOrderses,
                "projectRoles" : vm.projectRoles,
                "projectLabTaskses" : vm.projectLabTaskses,
                "contactPrivileges" : vm.contactPrivileges
            };


            vm.isSaving = true;
            if (vm.projects.id !== null) {
                Projects.update(vm.projects, onSaveSuccess2, onSaveError);
            } else {

                console.log("==========================================");
                console.log("==========================================");
                console.log(JSON.stringify(vm.projectsDTO));
                Projects.save(vm.projectsDTO, onSaveSuccess2, onSaveError);

            }
        };

        var onSaveSuccess2 = function(result) {
            console.log('saving project...');
            //$scope.$emit('smartLpcApp:projectsUpdate', result);
            // $uibModalInstance.close(result);
            $ngConfirm({
                title: 'Success!',
                content: "Project : <strong>"+vm.projects.fullName+"</strong> has been created",
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
            $state.go('projects', {}, {
                reload : true
            });// use for redirecting ...
        };


        vm.saveAndNext = function() {

            vm.projects.name = vm.projects.name.toUpperCase();


            for (var i = 0; i < vm.talents.length; i++) {
                vm.projectRoles.push(vm.talents[i]);
            }
            for (var i = 0; i < vm.execss.length; i++) {
                vm.contactPrivileges.push(vm.execss[i]);
            }
            // set actors with rights
            vm.projects.actorsWithRights   = vm.talents.length;

            vm.projectsDTO = {
                "projects" : vm.projects,
                "projectPurchaseOrderses" : vm.projectPurchaseOrderses,
                "projectRoles" : vm.projectRoles,
                "projectLabTaskses" : vm.projectLabTaskses,
                "contactPrivileges" : vm.contactPrivileges
            };


            vm.isSaving = true;
            if (vm.projects.id !== null) {
                Projects.update(vm.projects, onSaveSuccess2, onSaveError);
            } else {

                console.log("==========================================");
                console.log("==========================================");
                console.log(JSON.stringify(vm.projectsDTO));
                Projects.save(vm.projectsDTO, onSaveSuccess3, onSaveError);

            }
        };

        var onSaveSuccess3 = function(result) {
            console.log('saving project...');
            //$scope.$emit('smartLpcApp:projectsUpdate', result);
            // $uibModalInstance.close(result);
            $ngConfirm({
                title: 'Success!',
                content: "Project : <strong>"+vm.projects.fullName+"</strong> has been created",
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
            $state.go('projects', {}, {
                reload : true
            });// use for redirecting ...
        };



        var onSaveError = function() {
            $ngConfirm({
                title: 'Error!',
                content: "Project : <strong>"+vm.projects.fullName+"</strong> was not created",
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

                Lookups.save(vm.newStatus, onSaveSuccesss, onSaveError);


            }
        };
        var onSaveSuccesss = function (result) {
            vm.status.push(result);
            console.log("GOT NEW STATUS : "+JSON.stringify(result));
            alert("New Status Created")
        };

        var onSaveError = function () {

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
