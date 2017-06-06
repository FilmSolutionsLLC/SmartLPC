(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('storage-disk', {
            parent: 'entity',
            url: '/storage-disk?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.storage_Disk.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/storage-disk/storage-disks.html',
                    controller: 'Storage_DiskController',
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
                    $translatePartialLoader.addPart('storage_Disk');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('storage-disk-detail', {
            parent: 'entity',
            url: '/storage-disk/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.storage_Disk.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/storage-disk/storage-disk-detail.html',
                    controller: 'Storage_DiskDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('storage_Disk');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Storage_Disk', function($stateParams, Storage_Disk) {
                    return Storage_Disk.get({id : $stateParams.id});
                }]
            }
        })
        .state('storage-disk.new', {
            parent: 'storage-disk',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-disk/storage-disk-dialog.html',
                    controller: 'Storage_DiskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                size: null,
                                used: null,
                                available: null,
                                usePercent: null,
                                lastUpdated: null,
                                reserved: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('storage-disk', null, { reload: true });
                }, function() {
                    $state.go('storage-disk');
                });
            }]
        })
        .state('storage-disk.edit', {
            parent: 'storage-disk',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-disk/storage-disk-dialog.html',
                    controller: 'Storage_DiskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Storage_Disk', function(Storage_Disk) {
                            return Storage_Disk.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('storage-disk', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('storage-disk.delete', {
            parent: 'storage-disk',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-disk/storage-disk-delete-dialog.html',
                    controller: 'Storage_DiskDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Storage_Disk', function(Storage_Disk) {
                            return Storage_Disk.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('storage-disk', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
