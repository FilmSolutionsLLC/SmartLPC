(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('captions', {
            parent: 'entity',
            url: '/captions?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.captions.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/captions/captions.html',
                    controller: 'CaptionsController',
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
                    $translatePartialLoader.addPart('captions');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('captions-detail', {
            parent: 'entity',
            url: '/captions/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.captions.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/captions/captions-detail.html',
                    controller: 'CaptionsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('captions');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Captions', function($stateParams, Captions) {
                    return Captions.get({id : $stateParams.id});
                }]
            }
        })
        .state('captions.new', {
            parent: 'captions',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/captions/captions-dialog.html',
                    controller: 'CaptionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                captionText: null,
                                captionDttm: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('captions', null, { reload: true });
                }, function() {
                    $state.go('captions');
                });
            }]
        })
        .state('captions.edit', {
            parent: 'captions',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/captions/captions-dialog.html',
                    controller: 'CaptionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Captions', function(Captions) {
                            return Captions.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('captions', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('captions.delete', {
            parent: 'captions',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/captions/captions-delete-dialog.html',
                    controller: 'CaptionsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Captions', function(Captions) {
                            return Captions.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('captions', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
