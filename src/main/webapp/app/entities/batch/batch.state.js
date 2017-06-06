(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('batch', {
            parent: 'entity',
            url: '/batch?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.batch.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/batch/batches.html',
                    controller: 'BatchController',
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
                    $translatePartialLoader.addPart('batch');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('batch-detail', {
            parent: 'entity',
            url: '/batch/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.batch.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/batch/batch-detail.html',
                    controller: 'BatchDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('batch');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Batch', function($stateParams, Batch) {
                    return Batch.get({id : $stateParams.id});
                }]
            }
        })
        .state('batch.new', {
            parent: 'batch',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/batch/batch-dialog.html',
                    controller: 'BatchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                createdTime: null,
                                updatedTime: null,
                                coverImageId: null,
                                isAsset: null,
                                isHidden: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('batch', null, { reload: true });
                }, function() {
                    $state.go('batch');
                });
            }]
        })
        .state('batch.edit', {
            parent: 'batch',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/batch/batch-dialog.html',
                    controller: 'BatchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Batch', function(Batch) {
                            return Batch.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('batch', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('batch.delete', {
            parent: 'batch',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/batch/batch-delete-dialog.html',
                    controller: 'BatchDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Batch', function(Batch) {
                            return Batch.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('batch', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
