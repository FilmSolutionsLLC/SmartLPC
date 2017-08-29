(function() {
	'use strict';

	angular.module('smartLpcApp').controller('ProjectPermission',
			ProjectPermission);

	ProjectPermission.$inject = [ '$http', 'entity', 'ContactPrivileges','$rootScope', '$uibModal', '$scope', '$state', 'Contacts','ContactsSearch', 'AlertService' ];

	function ProjectPermission($http, entity, ContactPrivileges, $rootScope,$uibModal, $scope, $state, Contacts, ContactsSearch, AlertService) {
		var vm = this;
		vm.contactDTO = entity;
		vm.contacts = [];

		vm.relatedContacts = [];

		vm.contacts = vm.contactDTO.contacts;

		console.log("entity : "+JSON.stringify(entity));
		console.log("vm.contacts : "+JSON.stringify(vm.contacts));
		
		$rootScope.savedContact = [];
		vm.selectedProjects = [];

		$http({
			method : 'GET',
			url : 'api/contact/project/' + vm.contactDTO.contacts.id

		}).then(
				function successCallback(response) {
					vm.projects = response.data;
					console.log("Total Projects Recieved : "
							+ vm.projects.length);

					for (var i = 0; i < vm.projects.length; i++) {
						vm.selectedProjects.push(vm.projects[i]);

					}
					console.log("push worked for get:"
							+ vm.selectedProjects.length);
					console.log("vm.selectedProjects : "
							+ JSON.stringify(vm.selectedProjects));
				}, function errorCallback(response) {

				});
		$rootScope.savedContact.push(vm.contacts);
		vm.close = function() {
			$uibModalInstance.dismiss('cancel');
		};
		vm.downloadType = [ {
			'id' : 0,
			'value' : "NONE"
		}, {
			'id' : 1,
			'value' : "ALL"
		}, {
			'id' : 2,
			'value' : "Lock Approved"
		} ];
		vm.exclusives = [ {
			'id' : 0,
			'value' : "NONE"
		}, {
			'id' : 1,
			'value' : "BASIC"
		}, {
			'id' : 2,
			'value' : "MASTER"
		} ];

		vm.openModalProject = function() {

			var modalInstance = $uibModal.open({

				templateUrl : 'app/entities/contacts/projectsList.html',
				controller : 'ProjectListController',
				size : 'xl',
				scope : $scope,
				controllerAs : 'vm'
			});
		};

		$rootScope.selectedProjects = null;

		$rootScope.$watch(function() {
			return $rootScope.selectedProjects;
		}, function() {
			if ($rootScope.selectedProjects == null) {
				console.log("null or 0 rootscope selectedProjects");

			} else {
				console.log("rootscope selectedProjects contains projects :"
						+ $rootScope.selectedProjects.length);
				console.log("vm.selectedProjects contains projects  : "
						+ vm.selectedProjects.length);

				for (var i = 0; i < $rootScope.selectedProjects.length; i++) {
					vm.selectedProjects.push($rootScope.selectedProjects[i]);
				}
				console.log("push worked for rootscope:"
						+ vm.selectedProjects.length);
			}
		});

		vm.removeSelected = function(index) {

			vm.selectedProjects.splice(index, 1);

		};

		vm.save = function() {
			vm.isSaving = true;
			for (var i = 0; i < vm.selectedProjects.length; i++) {
				ContactPrivileges.save(vm.selectedProjects[i], onSaveSuccess,
						onSaveError);
			}
		};

		var onSaveSuccess = function(result) {
			// $scope.$emit('smartLpcApp:contactPrivilegesUpdate', result);
			$uibModalInstance.close(result);
			// console.log("saved in db: " + result.data.id);
			vm.isSaving = false;
			alert("Contact Privileges updated for Contact : "
					+ vm.selectedProjects.contact.fullName);
		};

		var onSaveError = function() {
			vm.isSaving = false;
		};
		/*
		 * vm.execss.push({ "exec": false, "downloadType": 0, "print": false,
		 * "email": false, "captioning": false, "talentManagement": false,
		 * "signoffManagement": false, "releaseExclude": false, "vendor": false,
		 * "lockApproveRestriction": false, "viewSensitive": false,
		 * "exclusives": false, "seesUntagged": false, "hasVideo": false,
		 * "disabled": false, "datgeditManagement": false, "priorityPix": false,
		 * "readOnly": false, "restartColumns": 2, "restartImageSize": 'Large',
		 * "restartImagesPerPage": 20, "showFinalizations": false, "watermark":
		 * false, "internal": false
		 * 
		 * });
		 */
		vm.load = function(id) {
			console.log("Contact Update Controller :  vm.load");
			Contacts.get({
				id : id
			}, function(result) {
				vm.contactDTO = result.data;
				// vm.contacts = vm.contactDTO.contacts;
				// vm.relatedContacts = vm.contactDTO.contactRelationships;

			});

		};
	}
})();
