(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('departments', {
            parent: 'entity',
            url: '/departments?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.departments.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/departments/departments.html',
                    controller: 'DepartmentsController',
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
                    $translatePartialLoader.addPart('departments');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('departments-detail', {
            parent: 'entity',
            url: '/departments/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.departments.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/departments/departments-detail.html',
                    controller: 'DepartmentsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('departments');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Departments', function($stateParams, Departments) {
                    return Departments.get({id : $stateParams.id});
                }]
            }
        })
        .state('departments.new', {
            parent: 'departments',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/departments/departments-dialog.html',
                    controller: 'DepartmentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                departmentName: null,
                                companyId: null,
                                createdDate: null,
                                updatedDate: null,
                                logo: null,
                                urlOverride: null,
                                selfProject: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('departments', null, { reload: true });
                }, function() {
                    $state.go('departments');
                });
            }]
        })
        .state('departments.edit', {
            parent: 'departments',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/departments/departments-dialog.html',
                    controller: 'DepartmentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Departments', function(Departments) {
                            return Departments.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('departments', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('departments.delete', {
            parent: 'departments',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/departments/departments-delete-dialog.html',
                    controller: 'DepartmentsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Departments', function(Departments) {
                            return Departments.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('departments', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
