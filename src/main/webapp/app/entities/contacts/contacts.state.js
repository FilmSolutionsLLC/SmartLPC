(function() {
	'use strict';

	angular.module('smartLpcApp').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider' ];

	function stateConfig($stateProvider) {
		$stateProvider
				.state(
						'contacts',
						{
							parent : 'entity',
							url : '/contacts?page&sort&search',
							data : {
								authorities : [ 'ROLE_USER' ],
								pageTitle : 'smartLpcApp.contacts.home.title'
							},
							views : {
								'content@' : {
									templateUrl : 'app/entities/contacts/contacts.html',
									controller : 'ContactsController',
									controllerAs : 'vm'
								}
							},
							params : {
								page : {
									value : '1',
									squash : true
								},
								sort : {
									value : 'id,asc',
									squash : true
								},
								search : null
							},
							resolve : {
								pagingParams : [
										'$stateParams',
										'PaginationUtil',
										function($stateParams, PaginationUtil) {
											return {
												page : PaginationUtil
														.parsePage($stateParams.page),
												sort : $stateParams.sort,
												predicate : PaginationUtil
														.parsePredicate($stateParams.sort),
												ascending : PaginationUtil
														.parseAscending($stateParams.sort),
												search : $stateParams.search
											};
										} ],
								translatePartialLoader : [
										'$translate',
										'$translatePartialLoader',
										function($translate,
												$translatePartialLoader) {
											$translatePartialLoader
													.addPart('contacts');
											$translatePartialLoader
													.addPart('global');
											return $translate.refresh();
										} ]
							}
						})
				.state(
						'contacts-detail',
						{
							parent : 'entity',
							url : '/contacts/{id}',
							data : {
								authorities : [ 'ROLE_USER' ],
								pageTitle : 'smartLpcApp.contacts.detail.title'
							},
							views : {
								'content@' : {
									templateUrl : 'app/entities/contacts/contacts-detail.html',
									controller : 'ContactsDetailController',
									controllerAs : 'vm'
								}
							},
							resolve : {
								translatePartialLoader : [
										'$translate',
										'$translatePartialLoader',
										function($translate,
												$translatePartialLoader) {
											$translatePartialLoader
													.addPart('contacts');
											return $translate.refresh();
										} ],
								entity : [
										'$stateParams',
										'Contacts',
										function($stateParams, Contacts) {
											console
													.log("Contacts.State : contacts.detail : resolve.entity called");

											return Contacts.get({
												id : $stateParams.id
											}).$promise;
										} ]
							}
						})
				.state(
						'contacts.new',
						{
							parent : 'contacts',
							url : '/new',
							data : {
								authorities : [ 'ROLE_USER' ]
							},
							onEnter : [
									'$stateParams',
									'$state',
									'$uibModal',
									function($stateParams, $state, $uibModal) {
										$uibModal
												.open({
													templateUrl : 'app/entities/contacts/contacts-dialog.html',
													controller : 'ContactsDialogController',
													controllerAs : 'vm',
													backdrop : 'static',
													size : 'lg',
													resolve : {
														entity : function() {
															return {
																username : null,
																password : null,
																fullName : null,
																title : null,
																email : null,
																email2 : null,
																phoneOffice : null,
																phoneAlternate : null,
																phoneMobile : null,
																phoneFax : null,
																streetAddress : null,
																streetAddress2 : null,
																streetAddress3 : null,
																city : null,
																state : null,
																zipcode : null,
																country : null,
																website : null,
																notes : null,
																sourceId : null,
																createdDate : null,
																updatedDate : null,
																globalRestartColumns : null,
																globalRestartImagesPerPage : null,
																globalRestartImageSize : null,
																globalRestartTime : null,
																dashboard : null,
																internalAccessOnly : null,
																adhocExpiresIn : null,
																adhocLimitViews : null,
																adhocDownload : null,
																adhocWatermarkText : null,
																loginIp : null,
																loginAttempt : null,
																attemptBasedLogin : null,
																ipBasedLogin : null,
																id : null
															};
														}
													}
												}).result.then(function() {
											$state.go('contacts', null, {
												reload : true
											});
										}, function() {
											$state.go('contacts');
										});
									} ]
						})
				.state(
						'contacts.edit',
						{
							parent : 'contacts',
							url : '/{id}/edit',
							data : {
								authorities : [ 'ROLE_USER' ],
								pageTitle : 'smartLpcApp.contacts.detail.title'
							},
							views : {
								'content@' : {
									templateUrl : 'app/entities/contacts/contacts-update.html',
									controller : 'ContactsUpdateController',
									controllerAs : 'vm'
								}
							},
							resolve : {
								translatePartialLoader : [
										'$translate',
										'$translatePartialLoader',
										function($translate,
												$translatePartialLoader) {
											$translatePartialLoader
													.addPart('contacts');
											return $translate.refresh();
										} ],


								entity : [ '$stateParams', 'Contacts',
										function($stateParams, Contacts) {
											// contactID: ['$stateParams',
											// 'Contacts', function
											// ($stateParams, Contacts) {

											return Contacts.get({
												id : $stateParams.id
											}).$promise;
										} ]
							}
						})
            .state(
                'contacts.similar',
                {
                    parent : 'contacts',
                    url : '/{id}/similar',
                    data : {
                        authorities : [ 'ROLE_USER' ],
                        pageTitle : 'smartLpcApp.contacts.detail.title'
                    },
                    views : {
                        'content@' : {
                            templateUrl : 'app/entities/contacts/contacts-add-similar.html',
                            controller : 'ContactsSimilarController',
                            controllerAs : 'vm'
                        }
                    },
                    resolve : {
                        translatePartialLoader : [
                            '$translate',
                            '$translatePartialLoader',
                            function($translate,
                                     $translatePartialLoader) {
                                $translatePartialLoader
                                    .addPart('contacts');
                                return $translate.refresh();
                            } ],


                        entity : [ '$stateParams', 'Contacts',
                            function($stateParams, Contacts) {
                                // contactID: ['$stateParams',
                                // 'Contacts', function
                                // ($stateParams, Contacts) {

                                return Contacts.get({
                                    id : $stateParams.id
                                }).$promise;
                            } ]
                    }
                })
				.state(
						'contacts.delete',
						{
							parent : 'contacts',
							url : '/{id}/delete',
							data : {
								authorities : [ 'ROLE_USER' ]
							},
							onEnter : [
									'$stateParams',
									'$state',
									'$uibModal',
									function($stateParams, $state, $uibModal) {
										$uibModal
												.open({
													templateUrl : 'app/entities/contacts/contacts-delete-dialog.html',
													controller : 'ContactsDeleteController',
													controllerAs : 'vm',
													size : 'md',
													resolve : {
														entity : [
																'Contacts',
																function(
																		Contacts) {
																	return Contacts
																			.get({
																				id : $stateParams.id
																			});
																} ]
													}
												}).result.then(function() {
											$state.go('contacts', null, {
												reload : true
											});
										}, function() {
											$state.go('^');
										});
									} ]
						})

				// ROHAN CODE.....
				.state(
						'contacts.add',
						{
							// parent: 'contacts',
							url : '/add',
							data : {
								authorities : [ 'ROLE_USER' ],
								pageTitle : 'smartLpcApp.contacts.detail.title'
							},
							views : {
								'content@' : {
									templateUrl : 'app/entities/contacts/contacts-add.html',
									controller : 'ContactsAddController',
									controllerAs : 'vm',
									// backdrop: 'static',
									// size: 'lg',
									resolve : {
										entity : function() {
											return {
												// relatedContacts: null,
												username : null,
												password : null,
												fullName : null,
												title : '',
												email : '',
												email2 : '',
												phoneOffice : '',
												phoneAlternate : '',
												phoneMobile : '',
												phoneFax : '',
                                                streetAddress : '',
                                                streetAddress2 : '',
                                                streetAddress3 : '',
												city : '',
												state : '',
												zipcode : '',
												country : '',
												website : '',
												notes : '',
												sourceId : '',
                                                createdDate : null,
												updatedDate : null,
												dashboard : false,
                                                internalAccessOnly : false,
                                                adhocExpiresIn : null,
                                                adhocLimitViews : null,
                                                adhocDownload : null,
                                                adhocWatermarkText : null,
                                                loginIp : null,
                                                loginAttempt : 0,
                                                attemptBasedLogin : true,
                                                ipBasedLogin : false,
												resetpassword : null,
                                                companyContact : null,
                                                createdByAdmin : null,
                                                updatedByAdmin : null,
                                                globalRestartColumns : 0,
                                                globalRestartImagesPerPage : 0,
                                                globalRestartImageSize : '',
                                                globalRestartTime : null,
												id : null
											};
										}
									}
								}
							}
						})
				.state(
						'contacts.modalview',
						{
							parent : 'contacts',
							url : '/add/select?page&sort&search',
							data : {
								authorities : [ 'ROLE_USER' ]
							},
							onEnter : [
									'$stateParams',
									'$state',
									'$uibModal',
									function($stateParams, $state, $uibModal) {
										$uibModal
												.open({
													templateUrl : 'app/entities/contacts/contacts-modal.html',
													controller : 'ContactsSelectController',
													controllerAs : 'vm',
													backdrop : 'static',
													size : 'lg',
													params : {
														page : {
															value : '1',
															squash : true
														},
														sort : {
															value : 'id,asc',
															squash : true
														},
														search : null
													},
													resolve : {
														pagingParams : [
																'$stateParams',
																'PaginationUtil',
																function(
																		$stateParams,
																		PaginationUtil) {
																	return {
																		page : PaginationUtil
																				.parsePage($stateParams.page),
																		sort : $stateParams.sort,
																		predicate : PaginationUtil
																				.parsePredicate($stateParams.sort),
																		ascending : PaginationUtil
																				.parseAscending($stateParams.sort),
																		search : $stateParams.search
																	};
																} ],
														translatePartialLoader : [
																'$translate',
																'$translatePartialLoader',
																function(
																		$translate,
																		$translatePartialLoader) {
																	$translatePartialLoader
																			.addPart('contacts');
																	$translatePartialLoader
																			.addPart('global');
																	return $translate
																			.refresh();
																} ]
													}
												})
									} ]
						})
				.state(
						'contacts.select',
						{
							parent : 'contacts',
							url : '/add/select?page&sort&search',
							// url: '/select?page&sort&search',
							data : {
								authorities : [ 'ROLE_USER' ],
								pageTitle : 'smartLpcApp.contacts.home.title'
							},
							views : {
								'content@' : {
									templateUrl : 'app/entities/contacts/contacts-select.html',
									controller : 'ContactsSelectController',
									controllerAs : 'vm'
								}
							},
							params : {
								page : {
									value : '1',
									squash : true
								},
								sort : {
									value : 'id,asc',
									squash : true
								},
								search : null
							},
							resolve : {
								pagingParams : [
										'$stateParams',
										'PaginationUtil',
										function($stateParams, PaginationUtil) {
											return {
												page : PaginationUtil
														.parsePage($stateParams.page),
												sort : $stateParams.sort,
												predicate : PaginationUtil
														.parsePredicate($stateParams.sort),
												ascending : PaginationUtil
														.parseAscending($stateParams.sort),
												search : $stateParams.search
											};
										} ],
								translatePartialLoader : [
										'$translate',
										'$translatePartialLoader',
										function($translate,
												$translatePartialLoader) {
											$translatePartialLoader
													.addPart('contacts');
											$translatePartialLoader
													.addPart('global');
											return $translate.refresh();
										} ]
							}
						})
				.state(
						'contacts.project-permission',
						{
							parent : 'contacts',
							url : '/{id}/projectpermission',
							data : {
								authorities : [ 'ROLE_USER' ],
								pageTitle : 'smartLpcApp.contacts.detail.title'
							},
							views : {
								'content@' : {
									templateUrl : 'app/entities/contacts/project-permission.html',
									controller : 'ProjectPermission',
									controllerAs : 'vm'
								}
							},
							resolve : {
								translatePartialLoader : [
										'$translate',
										'$translatePartialLoader',
										function($translate,
												$translatePartialLoader) {
											$translatePartialLoader
													.addPart('contacts');
											return $translate.refresh();
										} ],
								entity : [
										'$stateParams',
										'Contacts',
										function($stateParams, Contacts) {
											// contactID: ['$stateParams',
											// 'Contacts', function
											// ($stateParams, Contacts) {
											console
													.log("Project Permission State Called for ID : "+ $stateParams.id);

											return Contacts.get({
												id : $stateParams.id
											}).$promise;
										} ]
							}
						})
				.state(
						'contacts.simple',
						{
							parent : 'contacts',
							url : '/add/simple',
							data : {
								authorities : [ 'ROLE_USER' ]
							},

							onEnter : [
									'$stateParams',
									'$state',
									'$uibModal',
									function($stateParams, $state, $uibModal) {
										$uibModal
												.open({
													templateUrl : 'app/entities/contacts/simpleModal.html',
													controller : 'SimpleController',
													controllerAs : 'vm',
													backdrop : 'static',
													size : 'md'
												}).result.then(function() {
											$state.go('contacts.add', null, {
												reload : true
											});xz
										}, function() {
											$state.go('^');
										});
									} ],
							params : {
								page : {
									value : '1',
									squash : true
								},
								sort : {
									value : 'id,asc',
									squash : true
								},
								search : null
							},
							resolve : {
								pagingParams : [
										'$stateParams',
										'PaginationUtil',
										function($stateParams, PaginationUtil) {
											return {
												page : PaginationUtil
														.parsePage($stateParams.page),
												sort : $stateParams.sort,
												predicate : PaginationUtil
														.parsePredicate($stateParams.sort),
												ascending : PaginationUtil
														.parseAscending($stateParams.sort),
												search : $stateParams.search
											};
										} ],
								translatePartialLoader : [
										'$translate',
										'$translatePartialLoader',
										function($translate,
												$translatePartialLoader) {
											$translatePartialLoader
													.addPart('contacts');
											$translatePartialLoader
													.addPart('global');
											return $translate.refresh();
										} ]
							}
						})
            .state('contacts.newSmall',
            {
                data : {
                    authorities : [ 'ROLE_USER' ]
                },
                views : {
                    'content@': {
                        templateUrl: 'app/entities/contacts/contacts-add.html',
                        controller: 'ContactsAddController',
                        controllerAs: 'vm',
                        // backdrop: 'static',
                        // size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    // relatedContacts: null,
                                    username : null,
                                    password : null,
                                    fullName : null,
                                    title : '',
                                    email : '',
                                    email2 : '',
                                    phoneOffice : '',
                                    phoneAlternate : '',
                                    phoneMobile : '',
                                    phoneFax : '',
                                    streetAddress : '',
                                    streetAddress2 : '',
                                    streetAddress3 : '',
                                    city : '',
                                    state : '',
                                    zipcode : '',
                                    country : '',
                                    website : '',
                                    notes : '',
                                    sourceId : '',
                                    createdDate : null,
                                    updatedDate : null,
                                    dashboard : false,
                                    internalAccessOnly : false,
                                    adhocExpiresIn : null,
                                    adhocLimitViews : null,
                                    adhocDownload : null,
                                    adhocWatermarkText : null,
                                    loginIp : null,
                                    loginAttempt : 0,
                                    attemptBasedLogin : true,
                                    ipBasedLogin : false,
                                    resetpassword : null,
                                    companyContact : null,
                                    createdByAdmin : null,
                                    updatedByAdmin : null,
                                    globalRestartColumns : 0,
                                    globalRestartImagesPerPage : 0,
                                    globalRestartImageSize : '',
                                    globalRestartTime : null,
                                    id : null
                                };
                            }
                        }
                    }
                }
            });

	}
})();
