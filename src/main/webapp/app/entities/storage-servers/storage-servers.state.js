(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('storage-servers', {
            parent: 'entity',
            url: '/storage-servers?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.storage_Servers.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/storage-servers/storage-servers.html',
                    controller: 'Storage_ServersController',
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
                    $translatePartialLoader.addPart('storage_Servers');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('storage-servers-detail', {
            parent: 'entity',
            url: '/storage-servers/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.storage_Servers.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/storage-servers/storage-servers-detail.html',
                    controller: 'Storage_ServersDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('storage_Servers');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Storage_Servers', function($stateParams, Storage_Servers) {
                    return Storage_Servers.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('storage-servers.new', {
            parent: 'storage-servers',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-servers/storage-servers-dialog.html',
                    controller: 'Storage_ServersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                ipAddress: null,
                                remote: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('storage-servers', null, { reload: true });
                }, function() {
                    $state.go('storage-servers');
                });
            }]
        })
        .state('storage-servers.edit', {
            parent: 'storage-servers',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-servers/storage-servers-dialog.html',
                    controller: 'Storage_ServersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Storage_Servers', function(Storage_Servers) {
                            return Storage_Servers.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('storage-servers', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('storage-servers.delete', {
            parent: 'storage-servers',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-servers/storage-servers-delete-dialog.html',
                    controller: 'Storage_ServersDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Storage_Servers', function(Storage_Servers) {
                            return Storage_Servers.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('storage-servers', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
