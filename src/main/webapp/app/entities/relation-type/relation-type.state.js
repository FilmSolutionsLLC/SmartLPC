(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('relation-type', {
            parent: 'entity',
            url: '/relation-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.relationType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/relation-type/relation-types.html',
                    controller: 'RelationTypeController',
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
                    $translatePartialLoader.addPart('relationType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('relation-type-detail', {
            parent: 'entity',
            url: '/relation-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.relationType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/relation-type/relation-type-detail.html',
                    controller: 'RelationTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('relationType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RelationType', function($stateParams, RelationType) {
                    return RelationType.get({id : $stateParams.id});
                }]
            }
        })
        .state('relation-type.new', {
            parent: 'relation-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/relation-type/relation-type-dialog.html',
                    controller: 'RelationTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                relation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('relation-type', null, { reload: true });
                }, function() {
                    $state.go('relation-type');
                });
            }]
        })
        .state('relation-type.edit', {
            parent: 'relation-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/relation-type/relation-type-dialog.html',
                    controller: 'RelationTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RelationType', function(RelationType) {
                            return RelationType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('relation-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('relation-type.delete', {
            parent: 'relation-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/relation-type/relation-type-delete-dialog.html',
                    controller: 'RelationTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RelationType', function(RelationType) {
                            return RelationType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('relation-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
