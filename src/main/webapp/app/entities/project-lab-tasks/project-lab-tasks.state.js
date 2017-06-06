(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-lab-tasks', {
            parent: 'entity',
            url: '/project-lab-tasks?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.projectLabTasks.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-lab-tasks/project-lab-tasks.html',
                    controller: 'ProjectLabTasksController',
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
                    $translatePartialLoader.addPart('projectLabTasks');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-lab-tasks-detail', {
            parent: 'entity',
            url: '/project-lab-tasks/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.projectLabTasks.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-lab-tasks/project-lab-tasks-detail.html',
                    controller: 'ProjectLabTasksDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectLabTasks');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectLabTasks', function($stateParams, ProjectLabTasks) {
                    return ProjectLabTasks.get({id : $stateParams.id});
                }]
            }
        })
        .state('project-lab-tasks.new', {
            parent: 'project-lab-tasks',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-lab-tasks/project-lab-tasks-dialog.html',
                    controller: 'ProjectLabTasksDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createdDate: null,
                                updatedDate: null,
                                qb_rid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-lab-tasks', null, { reload: true });
                }, function() {
                    $state.go('project-lab-tasks');
                });
            }]
        })
        .state('project-lab-tasks.edit', {
            parent: 'project-lab-tasks',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-lab-tasks/project-lab-tasks-dialog.html',
                    controller: 'ProjectLabTasksDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectLabTasks', function(ProjectLabTasks) {
                            return ProjectLabTasks.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-lab-tasks', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-lab-tasks.delete', {
            parent: 'project-lab-tasks',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-lab-tasks/project-lab-tasks-delete-dialog.html',
                    controller: 'ProjectLabTasksDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectLabTasks', function(ProjectLabTasks) {
                            return ProjectLabTasks.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-lab-tasks', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
