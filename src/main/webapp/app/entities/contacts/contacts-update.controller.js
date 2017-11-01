(function() {
	'use strict';

	angular.module('smartLpcApp').controller('ContactsUpdateController',
			ContactsUpdateController);

	ContactsUpdateController.$inject = ['entity', '$state', '$http',
			'$uibModal', '$scope', '$rootScope', '$stateParams', 'Contacts',
			'Lookups', 'Departments', 'User' ];

	function ContactsUpdateController(entity, $state, $http, $uibModal, $scope,
			$rootScope, $stateParams, Contacts, Lookups, Departments, User) {
		console.log("Contact Update Controller");

		var vm = this;
		vm.contactDTO = entity;
		vm.contacts = [];

		vm.relatedContacts = [];

		vm.contacts = vm.contactDTO.contacts;
		vm.relatedContacts = vm.contactDTO.contactRelationships;


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
		// console.log("vm.contactDTO : " + JSON.stringify(vm.contactDTO))
		// console.log("print entity: ");

		// console.log(JSON.stringify(entity));
		var unsubscribe = $rootScope.$on('smartLpcApp:contactsUpdate',
				function(event, result) {
					vm.contacts = result;
				});
		$scope.$on('$destroy', unsubscribe);

		vm.lookupss = {};
		$http({
			method : 'GET',
			url : 'api/lookups/contacts/type'
		}).then(function successCallback(response) {
			vm.lookupss = response.data;

		}, function errorCallback(response) {

		});
		vm.departmentss = Departments.query();
		vm.users = User.query();

		vm.selectedContact = function(id) {
			vm.contacts.companyContact = id;
			console.log("Company selected : " + vm.contacts.companyContact);
		};

		vm.relatedContact = function(id) {
			Contacts.get({
				id : id
			}, function(result) {
				vm.contacts.relatedContacts.push(result);
			});
		};

		vm.companyContactInputBox = 'companyContactInputBox';

		vm.companyContact = [];

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
					}
				},
				translatePartialLoader : [ '$translate',
						'$translatePartialLoader',
						function($translate, $translatePartialLoader) {
							$translatePartialLoader.addPart('contacts');
							$translatePartialLoader.addPart('global');
							return $translate.refresh();
						} ]
			})
		};

		vm.count = 0;
		$rootScope
				.$watch(
						function() {
							return $rootScope.relationships;
						},
						function() {
							if ($rootScope.relationships == null) {
								console.log("null rootscope");

							} else {
								console.log("not null");

								vm.currrentOBJ = $rootScope.relationships;
								console.log("========> "
										+ JSON.stringify(vm.currentOBJ));
								if (angular.equals(vm.currrentOBJ.elementID,
										'field_vm.contacts.contactCompany')) {
									console.log("found equal");

									vm.contactDTO.contacts.companyContact = vm.currrentOBJ.data;
									// console.log(JSON.stringify(vm.contacts.companyContact));
									// console.log(vm.vm.contactDTO.contacts.companyContact.fullName);
								} else if (angular.equals(
										vm.currrentOBJ.elementID,
										'relatedContact')) {
									console.log("count : " + vm.count);
									// vm.relatedContacts.push(vm.currrentOBJ.data);
									// vm.relatedContacts[vm.count].contactB =
									// vm.currrentOBJ.data;
									vm.contactDTO.contactRelationships.pop();
									vm.contactDTO.contactRelationships.push({
										"isPrimaryContact" : false,
										"contact_b" : vm.currrentOBJ.data,
                                        "relationshipType" : vm.currrentOBJ.data.typeValue
									});
									vm.count++;

									console.log("Data : "+JSON.stringify(vm.contactDTO.contactRelationships));

									console

											.log("related Contacts size "
													+ vm.contactDTO.contactRelationships.length);
								} else {
									console.log("not equal..");
								}
							}
						});

		vm.save = function() {
			console.log("calling SAVE function....");
			vm.isSaving = true;
			console.log("updating contact");
			/*
			 * vm.contactDTOs = {
			 *
			 * "contacts": vm.contacts, "contactRelationships":
			 * vm.relatedContacts };
			 */

			// vm.contactDTO.contactRelationships = vm.relatedContacts;
			Contacts.update(vm.contactDTO, onSaveSuccess, onSaveError);
			console.log("saving contactsDTO :" + JSON.stringify(vm.contactDTO));

		}

		var onSaveSuccess = function(result) {
			vm.isSaving = false;
			//$scope.$emit('smartLpcApp:contactsUpdate', result);
			// $uibModalInstance.close(result);
            $ngConfirm({
                title: 'Success!',
                content: "Contact : <strong>"+vm.contacts.fullName+"</strong> has been updated",
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
			$state.go('contacts', {}, {
				reload : true
			});// use for redirecting ...

		};

		var onSaveError = function() {
			vm.isSaving = false;
		};

		$scope.related = [];
		console
				.log("vm.relatedContacts : "
						+ JSON.stringify(vm.relatedContact));
		/*
		 * for (var i = 0; i < vm.contactDTO.contactRelationships.length; i++) {
		 *
		 * $scope.related.push({'id': 'related' + i}); console.log("related
		 * pushed . ." + $scope.related.length); }
		 */

		vm.addRelated = function() {
			console.log("adding related contacts");
			// var newItemNo = $scope.related.length + 1;
			// console.log("related contacts added " + newItemNo);
			// $scope.related.push({'id': 'related' + newItemNo});
			vm.contactDTO.contactRelationships.push({
				"isPrimaryContact" : false,
				"contact_b" : null,
                "relationshipType": null
			});

		};

		vm.removeRelated = function(index) {
			console.log("related contacts removed .." + index);
			vm.contactDTO.contactRelationships.splice(index, 1);
			// vm.relatedContacts.splice(index, 1);
			// $scope.related.splice(index, 1);
		};

		vm.addPrivilege = function(id) {
			console.log("Contact id to get projects : " + id);
			var modalInstance = $uibModal.open({

				templateUrl : 'app/entities/contacts/project-permission.html',
				controller : 'ProjectPermission',
				controllerAs : 'vm',
				size : 'md',
				scope : $scope,


				resolve : {
					id : function() {
						return id;
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
			});
		};

	}
})();
