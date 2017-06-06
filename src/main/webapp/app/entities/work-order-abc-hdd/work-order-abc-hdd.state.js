(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('work-order-abc-hdd', {
            parent: 'entity',
            url: '/work-order-abc-hdd?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.workOrderAbcHdd.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-order-abc-hdd/work-order-abc-hdds.html',
                    controller: 'WorkOrderAbcHddController',
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
                    $translatePartialLoader.addPart('workOrderAbcHdd');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('work-order-abc-hdd-detail', {
            parent: 'entity',
            url: '/work-order-abc-hdd/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.workOrderAbcHdd.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-order-abc-hdd/work-order-abc-hdd-detail.html',
                    controller: 'WorkOrderAbcHddDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('workOrderAbcHdd');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WorkOrderAbcHdd', function($stateParams, WorkOrderAbcHdd) {
                    return WorkOrderAbcHdd.get({id : $stateParams.id});
                }]
            }
        })
        .state('work-order-abc-hdd.new', {
            parent: 'work-order-abc-hdd',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-order-abc-hdd/work-order-abc-hdd-dialog.html',
                    controller: 'WorkOrderAbcHddDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                size: null,
                                drive_number: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('work-order-abc-hdd', null, { reload: true });
                }, function() {
                    $state.go('work-order-abc-hdd');
                });
            }]
        })
        .state('work-order-abc-hdd.edit', {
            parent: 'work-order-abc-hdd',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-order-abc-hdd/work-order-abc-hdd-dialog.html',
                    controller: 'WorkOrderAbcHddDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkOrderAbcHdd', function(WorkOrderAbcHdd) {
                            return WorkOrderAbcHdd.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-order-abc-hdd', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('work-order-abc-hdd.delete', {
            parent: 'work-order-abc-hdd',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-order-abc-hdd/work-order-abc-hdd-delete-dialog.html',
                    controller: 'WorkOrderAbcHddDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WorkOrderAbcHdd', function(WorkOrderAbcHdd) {
                            return WorkOrderAbcHdd.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-order-abc-hdd', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
