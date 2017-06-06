(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('ingest', {
                parent: 'entity',
                url: '/ingest?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.ingest.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/ingest/ingests.html',
                        controller: 'IngestController',
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
                        $translatePartialLoader.addPart('ingest');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ingest-detail', {
                parent: 'entity',
                url: '/ingest/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.ingest.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/ingest/ingest-detail.html',
                        controller: 'IngestDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ingest');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Ingest', function ($stateParams, Ingest) {
                        return Ingest.get({id: $stateParams.id});
                    }]
                }
            })
            .state('ingest.new', {
                parent: 'ingest',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/ingest/ingest-dialog.html',
                        controller: 'IngestDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    ingestStartTime: null,
                                    ingestCompletedTime: null,
                                    totalImages: null,
                                    totalDone: null,
                                    completed: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('ingest', null, {reload: true});
                    }, function () {
                        $state.go('ingest');
                    });
                }]
            })
            .state('ingest.edit', {
                parent: 'ingest',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/ingest/ingest-dialog.html',
                        controller: 'IngestDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Ingest', function (Ingest) {
                                return Ingest.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('ingest', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('ingest.delete', {
                parent: 'ingest',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/ingest/ingest-delete-dialog.html',
                        controller: 'IngestDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Ingest', function (Ingest) {
                                return Ingest.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('ingest', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('ingest.current', {
                parent: 'entity',
                url: '/ingest/current',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.ingest.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/ingest/ingests-current.html',
                        controller: 'IngestsCurrentController',
                        controllerAs: 'vm'
                    }
                },

                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ingest');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ingest.toIngest', {
                parent: 'ingest',
                url: '/fs/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/ingest/ingest-filesys-new.html',
                        controller: 'IngestFileSysController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                    entity: function () {
                        return {
                            ingestID: null,
                            project: null,
                            exists: null,
                            status: null,
                            totalFiles: null

                        };
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ingest');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    }

})();
