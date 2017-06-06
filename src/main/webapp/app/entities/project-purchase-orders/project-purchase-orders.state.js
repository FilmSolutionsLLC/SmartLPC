(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-purchase-orders', {
            parent: 'entity',
            url: '/project-purchase-orders?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.projectPurchaseOrders.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-purchase-orders/project-purchase-orders.html',
                    controller: 'ProjectPurchaseOrdersController',
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
                    $translatePartialLoader.addPart('projectPurchaseOrders');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-purchase-orders-detail', {
            parent: 'entity',
            url: '/project-purchase-orders/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.projectPurchaseOrders.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-purchase-orders/project-purchase-orders-detail.html',
                    controller: 'ProjectPurchaseOrdersDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectPurchaseOrders');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectPurchaseOrders', function($stateParams, ProjectPurchaseOrders) {
                    return ProjectPurchaseOrders.get({id : $stateParams.id});
                }]
            }
        })
        .state('project-purchase-orders.new', {
            parent: 'project-purchase-orders',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-purchase-orders/project-purchase-orders-dialog.html',
                    controller: 'ProjectPurchaseOrdersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                po_number: null,
                                po_notes: null,
                                qb_rid: null,
                                created_date: null,
                                updated_date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-purchase-orders', null, { reload: true });
                }, function() {
                    $state.go('project-purchase-orders');
                });
            }]
        })
        .state('project-purchase-orders.edit', {
            parent: 'project-purchase-orders',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-purchase-orders/project-purchase-orders-dialog.html',
                    controller: 'ProjectPurchaseOrdersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectPurchaseOrders', function(ProjectPurchaseOrders) {
                            return ProjectPurchaseOrders.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-purchase-orders', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-purchase-orders.delete', {
            parent: 'project-purchase-orders',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-purchase-orders/project-purchase-orders-delete-dialog.html',
                    controller: 'ProjectPurchaseOrdersDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectPurchaseOrders', function(ProjectPurchaseOrders) {
                            return ProjectPurchaseOrders.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-purchase-orders', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
