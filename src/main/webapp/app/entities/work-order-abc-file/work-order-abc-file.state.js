(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('work-order-abc-file', {
            parent: 'entity',
            url: '/work-order-abc-file?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.workOrderAbcFile.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-order-abc-file/work-order-abc-files.html',
                    controller: 'WorkOrderAbcFileController',
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
                    $translatePartialLoader.addPart('workOrderAbcFile');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('work-order-abc-file-detail', {
            parent: 'entity',
            url: '/work-order-abc-file/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.workOrderAbcFile.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-order-abc-file/work-order-abc-file-detail.html',
                    controller: 'WorkOrderAbcFileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('workOrderAbcFile');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WorkOrderAbcFile', function($stateParams, WorkOrderAbcFile) {
                    return WorkOrderAbcFile.get({id : $stateParams.id});
                }]
            }
        })
        .state('work-order-abc-file.new', {
            parent: 'work-order-abc-file',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-order-abc-file/work-order-abc-file-dialog.html',
                    controller: 'WorkOrderAbcFileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                file_count: null,
                                file_size: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('work-order-abc-file', null, { reload: true });
                }, function() {
                    $state.go('work-order-abc-file');
                });
            }]
        })
        .state('work-order-abc-file.edit', {
            parent: 'work-order-abc-file',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-order-abc-file/work-order-abc-file-dialog.html',
                    controller: 'WorkOrderAbcFileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkOrderAbcFile', function(WorkOrderAbcFile) {
                            return WorkOrderAbcFile.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-order-abc-file', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('work-order-abc-file.delete', {
            parent: 'work-order-abc-file',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-order-abc-file/work-order-abc-file-delete-dialog.html',
                    controller: 'WorkOrderAbcFileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WorkOrderAbcFile', function(WorkOrderAbcFile) {
                            return WorkOrderAbcFile.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-order-abc-file', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
