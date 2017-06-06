(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-roles', {
            parent: 'entity',
            url: '/project-roles?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.projectRoles.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-roles/project-roles.html',
                    controller: 'ProjectRolesController',
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
                    $translatePartialLoader.addPart('projectRoles');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-roles-detail', {
            parent: 'entity',
            url: '/project-roles/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.projectRoles.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-roles/project-roles-detail.html',
                    controller: 'ProjectRolesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectRoles');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectRoles', function($stateParams, ProjectRoles) {
                    return ProjectRoles.get({id : $stateParams.id});
                }]
            }
        })
        .state('project-roles.new', {
            parent: 'project-roles',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-roles/project-roles-dialog.html',
                    controller: 'ProjectRolesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                soloKillPct: null,
                                groupKillPct: null,
                                miniFullDt: null,
                                fullFinalDt: null,
                                disabled: null,
                                characterName: null,
                                startDate: null,
                                daysWorking: null,
                                excSologroup: null,
                                notes: null,
                                tagName: null,
                                hotkeyValue: null,
                                expireDate: null,
                                tertiaryKillPct: null,
                                createdDate: null,
                                updatedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-roles', null, { reload: true });
                }, function() {
                    $state.go('project-roles');
                });
            }]
        })
        .state('project-roles.edit', {
            parent: 'project-roles',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-roles/project-roles-dialog.html',
                    controller: 'ProjectRolesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectRoles', function(ProjectRoles) {
                            return ProjectRoles.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-roles', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-roles.delete', {
            parent: 'project-roles',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-roles/project-roles-delete-dialog.html',
                    controller: 'ProjectRolesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectRoles', function(ProjectRoles) {
                            return ProjectRoles.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-roles', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
