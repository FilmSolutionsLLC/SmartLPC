(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contact-relationships', {
            parent: 'entity',
            url: '/contact-relationships?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.contactRelationships.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contact-relationships/contact-relationships.html',
                    controller: 'ContactRelationshipsController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contactRelationships');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contact-relationships-detail', {
            parent: 'entity',
            url: '/contact-relationships/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.contactRelationships.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contact-relationships/contact-relationships-detail.html',
                    controller: 'ContactRelationshipsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contactRelationships');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ContactRelationships', function($stateParams, ContactRelationships) {
                    return ContactRelationships.get({id : $stateParams.id});
                }]
            }
        })
        .state('contact-relationships.new', {
            parent: 'contact-relationships',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-relationships/contact-relationships-dialog.html',
                    controller: 'ContactRelationshipsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isPrimaryContact: null,
                                createdDate: null,
                                updatedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contact-relationships', null, { reload: true });
                }, function() {
                    $state.go('contact-relationships');
                });
            }]
        })
        .state('contact-relationships.edit', {
            parent: 'contact-relationships',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-relationships/contact-relationships-dialog.html',
                    controller: 'ContactRelationshipsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContactRelationships', function(ContactRelationships) {
                            return ContactRelationships.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact-relationships', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact-relationships.delete', {
            parent: 'contact-relationships',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-relationships/contact-relationships-delete-dialog.html',
                    controller: 'ContactRelationshipsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ContactRelationships', function(ContactRelationships) {
                            return ContactRelationships.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact-relationships', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
