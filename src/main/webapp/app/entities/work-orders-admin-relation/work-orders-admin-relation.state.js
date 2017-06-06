(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('work-orders-admin-relation', {
            parent: 'entity',
            url: '/work-orders-admin-relation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.workOrdersAdminRelation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-orders-admin-relation/work-orders-admin-relations.html',
                    controller: 'WorkOrdersAdminRelationController',
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
                    $translatePartialLoader.addPart('workOrdersAdminRelation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('work-orders-admin-relation-detail', {
            parent: 'entity',
            url: '/work-orders-admin-relation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.workOrdersAdminRelation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-orders-admin-relation/work-orders-admin-relation-detail.html',
                    controller: 'WorkOrdersAdminRelationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('workOrdersAdminRelation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WorkOrdersAdminRelation', function($stateParams, WorkOrdersAdminRelation) {
                    return WorkOrdersAdminRelation.get({id : $stateParams.id});
                }]
            }
        })
        .state('work-orders-admin-relation.new', {
            parent: 'work-orders-admin-relation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-orders-admin-relation/work-orders-admin-relation-dialog.html',
                    controller: 'WorkOrdersAdminRelationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('work-orders-admin-relation', null, { reload: true });
                }, function() {
                    $state.go('work-orders-admin-relation');
                });
            }]
        })
        .state('work-orders-admin-relation.edit', {
            parent: 'work-orders-admin-relation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-orders-admin-relation/work-orders-admin-relation-dialog.html',
                    controller: 'WorkOrdersAdminRelationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkOrdersAdminRelation', function(WorkOrdersAdminRelation) {
                            return WorkOrdersAdminRelation.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-orders-admin-relation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('work-orders-admin-relation.delete', {
            parent: 'work-orders-admin-relation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-orders-admin-relation/work-orders-admin-relation-delete-dialog.html',
                    controller: 'WorkOrdersAdminRelationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WorkOrdersAdminRelation', function(WorkOrdersAdminRelation) {
                            return WorkOrdersAdminRelation.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-orders-admin-relation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
