(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('lookups', {
                parent: 'entity',
                url: '/lookups?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.lookups.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/lookups/lookups.html',
                        controller: 'LookupsController',
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
                        $translatePartialLoader.addPart('lookups');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('lookups-detail', {
                parent: 'entity',
                url: '/lookups/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.lookups.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/lookups/lookups-detail.html',
                        controller: 'LookupsDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookups');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Lookups', function ($stateParams, Lookups) {
                        console.log("sending entity via scope..");
                        return Lookups.get({id: $stateParams.id});
                    }]
                }
            })
            .state('lookups.new', {
                parent: 'lookups',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/lookups/lookups-dialog.html',
                        controller: 'LookupsDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    tableName: null,
                                    fieldName: null,
                                    textValue: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('lookups', null, {reload: true});
                    }, function () {
                        $state.go('lookups');
                    });
                }]
            })
            .state('lookups.edit', {
                parent: 'lookups',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/lookups/lookups-dialog.html',
                        controller: 'LookupsDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {

                            entity: ['Lookups', function (Lookups) {
                                return Lookups.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('lookups', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('lookups.delete', {
                parent: 'lookups',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/lookups/lookups-delete-dialog.html',
                        controller: 'LookupsDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Lookups', function (Lookups) {
                                return Lookups.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('lookups', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
