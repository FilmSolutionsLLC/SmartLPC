(function() {
	'use strict';

	angular.module('smartLpcApp').controller('ProjectsDetailController',
			ProjectsDetailController);

	ProjectsDetailController.$inject = ['$uibModal', '$scope', '$rootScope',
			'$stateParams', 'entity', 'Projects', 'Lookups', 'Contacts',
			'User', 'Departments', 'Storage_Disk' ];

	function ProjectsDetailController($uibModal,$scope, $rootScope, $stateParams, entity,
			Projects, Lookups, Contacts, User, Departments, Storage_Disk) {
		var vm = this;
		vm.projectsDTO = entity;

		$scope.isGeneric = function(tags) {
			return tags.contact.fullName !== 'generic pkotag';
		}
		/*
		 * console.log("projectsDTO : " + JSON.stringify(vm.projectsDTO));
		 * vm.tags = []; vm.projectRoles = vm.projectsDTO.projectRoles;
		 * vm.contactPrivileges = vm.projectsDTO.contactPrivileges;
		 * vm.purchaseOrders = vm.projectsDTO.projectPurchaseOrderses; vm.labs =
		 * vm.projectsDTO.projectLabTaskses; console.log("lab : " +
		 * JSON.stringify(vm.labs)); console.log("po : " +
		 * JSON.stringify(vm.purchaseOrders))
		 * 
		 * if (vm.projectRoles) { for (var i = 0; i < vm.projectRoles.length;
		 * i++) { console.log("iterated "); if
		 * (angular.equals(vm.projectRoles[i].relationship_type, "Main
		 * Contact")) { console.log("main contact added"); vm.mainC =
		 * vm.projectRoles[i]; } else if
		 * (angular.equals(vm.projectRoles[i].relationship_type, "Unit
		 * Publicist")) { console.log("u pub added"); vm.uPub =
		 * vm.projectRoles[i]; } if
		 * (angular.equals(vm.projectRoles[i].relationship_type, "Unit
		 * Photographer")) { console.log("u photo added"); vm.uPhoto =
		 * vm.projectRoles[i]; } if
		 * (angular.equals(vm.projectRoles[i].relationship_type, "Lab")) {
		 * console.log("labs added"); vm.lab = vm.projectRoles[i]; } if
		 * (angular.equals(vm.projectRoles[i].relationship_type, "PKO_Tag")) {
		 * console.log("tags added"); vm.tags.push(vm.projectRoles[i]);
		 * console.log("-----> " + JSON.stringify(vm.tag)); } } }
		 * console.log(JSON.stringify(vm.projectsDTO)); vm.load = function (id) {
		 * Projects.get({id: id}, function (result) { vm.projectsDTO = result;
		 * 
		 * vm.projectRoles = vm.projectsDTO.projectRoles; vm.contactPrivileges =
		 * vm.projectsDTO.contactPrivileges; vm.purchaseOrders =
		 * vm.projectsDTO.projectPurchaseOrderses; vm.labs =
		 * vm.projectsDTO.projectLabTaskses; console.log("lab : " +
		 * JSON.stringify(vm.labs)); console.log("po : " +
		 * JSON.stringify(vm.purchaseOrders)) if (vm.projectRoles) { for (var i =
		 * 0; i < vm.projectRoles.length; i++) { console.log("iterated "); if
		 * (angular.equals(vm.projectRoles[i].relationship_type, "Main
		 * Contact")) { console.log("main contact added"); vm.mainC =
		 * vm.projectRoles[i]; } else if
		 * (angular.equals(vm.projectRoles[i].relationship_type, "Unit
		 * Publicist")) { console.log("u pub added"); vm.uPub =
		 * vm.projectRoles[i]; } if
		 * (angular.equals(vm.projectRoles[i].relationship_type, "Unit
		 * Photographer")) { console.log("u photo added"); vm.uPhoto =
		 * vm.projectRoles[i]; } if
		 * (angular.equals(vm.projectRoles[i].relationship_type, "Lab")) {
		 * console.log("labs added"); vm.lab = vm.projectRoles[i]; } if
		 * (angular.equals(vm.projectRoles[i].relationship_type, "PKO_Tag")) {
		 * console.log("tags added"); vm.tags = vm.projectRoles[i];
		 * console.log("-----> " + JSON.stringify(vm.tag)); } } }
		 * 
		 * }); };
		 */
		var unsubscribe = $rootScope.$on('smartLpcApp:projectsUpdate',
				function(event, result) {
					vm.projectsDTO = result;
				});
		$scope.$on('$destroy', unsubscribe);

		vm.sendIndividualMail = function(talent) {

			var modalInstance = $uibModal.open({

				templateUrl : 'app/entities/projects/mails.html',
				controller : 'MailsIndividualController',
				size : 'lg',
				scope : $scope,
				controllerAs : 'vm',
				backdrop : 'static',
				resolve : {

					talent : function() {

						return talent;
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
		
		  // send multiple email
        vm.sendMail = function(talents) {
        	
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
		
	}
})();
