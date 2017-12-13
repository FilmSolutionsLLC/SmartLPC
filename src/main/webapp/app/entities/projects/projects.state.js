(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('projects', {
                parent: 'entity',
                url: '/projects?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projects.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/projects/projects.html',
                        controller: 'ProjectsController',
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
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('projects-detail', {
                parent: 'entity',
                url: '/projects/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projects.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/projects/projects-detail.html',
                        controller: 'ProjectsDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('projects');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Projects', function ($stateParams, Projects) {
                        console.log("sending entity via params..");
                        return Projects.get({id: $stateParams.id}).$promise;
                    }]
                }
            })
            .state('projects.new', {
                parent: 'projects',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/projects/projects-dialog.html',
                        controller: 'ProjectsDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    fullName: null,
                                    runOfShowFlag: null,
                                    template: null,
                                    labFlag: null,
                                    alfrescoTitle1: null,
                                    alfrescoTitle2: null,
                                    startDate: null,
                                    endDate: null,
                                    actorsWithRights: null,
                                    daysShooting: null,
                                    weeksShooting: null,
                                    notes: null,
                                    sensitiveViewing: false,
                                    productionCompanyNotes: null,
                                    productionCompanyShippingNumber: null,
                                    processingDeliveries: null,
                                    processingSpecialInstructions: null,
                                    processingWatermark: null,
                                    processingCopyright: null,
                                    labProofNotes: null,
                                    labLastProofImageNumber: null,
                                    labLastProofPageNumber: null,
                                    labImageNumberSchema: null,
                                    labFolderBatchSchema: null,
                                    photoLabInfo: null,
                                    projectUnitPhotoNotes: null,
                                    projectInfoNotes: null,
                                    createdDate: null,
                                    updatedDate: null,
                                    legacyDirector: null,
                                    legacyExecutiveProducer: null,
                                    legacyExecutiveProducer2: null,
                                    legacyExecutiveProducer3: null,
                                    legacyExecutiveProducer4: null,
                                    legacyProducer: null,
                                    legacyProducer2: null,
                                    legacyProducer3: null,
                                    legacyProducer4: null,
                                    legacyAdditionalTalent: null,
                                    themeId: true,
                                    sptPhotoSubtype: null,
                                    photoCredit: null,
                                    shootDate: null,
                                    shootDateOverride: null,
                                    unitPhotographerOverride: null,
                                    useSetup: null,
                                    useExif: null,
                                    tagDate: null,
                                    tagreportIndex: null,
                                    loginMessage: null,
                                    loginMessageActive: null,
                                    topLevelAlbums: null,
                                    enableTertiary: null,
                                    invoiceCreated: null,
                                    price: null,
                                    foxTitle: null,
                                    isAsset: null,
                                    fullRejection: null,
                                    disabled: false,
                                    reminderDate: null,
                                    photoCreditOverride: null,
                                    legacyNameNotes: null,
                                    currentTagger: null,
                                    currentTagger1: null,
                                    currentTagger2: null,
                                    imageLocation: null,
                                    imageLocationRemote: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('projects', null, {reload: true});
                    }, function () {
                        $state.go('projects');
                    });
                }]
            })
            /* .state('projects.edit', {
             parent: 'projects',
             url: '/{id}/edit',
             data: {
             authorities: ['ROLE_USER']
             },
             onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
             $uibModal.open({
             templateUrl: 'app/entities/projects/projects-dialog.html',
             controller: 'ProjectsDialogController',
             controllerAs: 'vm',
             backdrop: 'static',
             size: 'lg',
             resolve: {
             entity: ['Projects', function (Projects) {
             return Projects.get({id: $stateParams.id});
             }]
             }
             }).result.then(function () {
             $state.go('projects', null, {reload: true});
             }, function () {
             $state.go('^');
             });
             }]
             })*/
            .state('projects.edit', {
                parent: 'projects',
                url: '/{id}/edit?page',
                params: {
                    page: {
                        value: '1'
                    }
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projects.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/projects/projects-update.html',
                        controller: 'ProjectsUpdateController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('projects');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Projects', function ($stateParams, Projects) {
                        return Projects.get({id: $stateParams.id}).$promise;
                    }]
                }
            })
            .state('projects.template-add', {
                parent: 'projects',
                url: '/{id}/template?page',
                params: {
                    page: {
                        value: '1'
                    }
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projects.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/projects/projects-template-add.html',
                        controller: 'ProjectsTemplateAddController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('projects');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Projects', function ($stateParams, Projects) {
                        return Projects.get({id: $stateParams.id}).$promise;
                    }]
                }
            })
            .state('projects.delete', {
                parent: 'projects',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/projects/projects-delete-dialog.html',
                        controller: 'ProjectsDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Projects', function (Projects) {
                                return Projects.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('projects', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('projects.add', {
                parent: 'projects',
                url: '/add',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projects.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/projects/projects-add.html',
                        controller: 'ProjectsAddController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    fullName: null,
                                    runOfShowFlag: null,
                                    template: null,
                                    labFlag: null,
                                    alfrescoTitle1: null,
                                    alfrescoTitle2: null,
                                    startDate: null,
                                    endDate: null,
                                    actorsWithRights: 0,
                                    daysShooting: 0,
                                    weeksShooting: 0,
                                    notes: '',
                                    sensitiveViewing: false,
                                    productionCompanyNotes: '',
                                    productionCompanyShippingNumber: '',
                                    processingDeliveries: '',
                                    processingSpecialInstructions: '',
                                    processingWatermark: '',
                                    processingCopyright: '',
                                    labProofNotes: '',
                                    labLastProofImageNumber: '',
                                    labLastProofPageNumber: '',
                                    labImageNumberSchema: '',
                                    labFolderBatchSchema: '',
                                    photoLabInfo: '',
                                    projectUnitPhotoNotes: '',
                                    projectInfoNotes: '',
                                    createdDate: null,
                                    updatedDate: null,
                                    legacyDirector: '',
                                    legacyExecutiveProducer: '',
                                    legacyExecutiveProducer2: '',
                                    legacyExecutiveProducer3: '',
                                    legacyExecutiveProducer4: '',
                                    legacyProducer: '',
                                    legacyProducer2: '',
                                    legacyProducer3: '',
                                    legacyProducer4: '',
                                    legacyAdditionalTalent: '',
                                    legacyNameNotes: '',
                                    themeId: true,
                                    sptPhotoSubtype: null,
                                    photoCredit: '',
                                    shootDate: null,
                                    shootDateOverride: false,
                                    unitPhotographerOverride: false,
                                    useSetup: false,
                                    useExif: false,
                                    tagDate: null,
                                    tagreportIndex: null,
                                    loginMessage: '',
                                    loginMessageActive: false,
                                    topLevelAlbums: false,
                                    enableTertiary: false,
                                    invoiceCreated: 388,
                                    price: '',
                                    foxTitle: '',
                                    isAsset: null,
                                    fullRejection: 1,
                                    disabled: false,
                                    reminderDate: null,
                                    photoCreditOverride: false,
                                    imageLocation: null,
                                    imageLocationRemote: "brick",
                                    projectType: "PKO_MOVIE",
                                    id: null
                                };
                            }

                        }
                    }
                }
            })
            .state('projects.template', {
                parent: 'projects',
                url: '/template',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/projects/projects-template.html',
                        controller: 'ProjectTemplateController',
                        controllerAs: 'vm',
                        //backdrop: 'static',
                        size: 'md'
                    }).result.then(function (id) {
                        $state.go('projects.edit', null, {"id": id, reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }],
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('projects.multiple', {
                parent: 'entity',
                url: '/projects/multiple?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projects.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/projects/projects-multiple.html',
                        controller: 'MultiProjectController',
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
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('projects-test', {
                parent: 'entity',
                url: '/pro?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projects.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/projects/pro.html',
                        controller: 'ProController',
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
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('projectroles.edit', {
                parent: 'entity',
                url: '/edit/pr/{id}',
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
                        //$state.go('project-roles', null, { reload: true });
                        //window.history.back();
                    }, function() {
                        //$state.go('^');
                    });
                }]
            })
            .state('projectroles.new', {
                parent: 'project-roles',
                url: '/new/edit/pr',
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
                                    disabled: false,
                                    characterName: null,
                                    startDate: null,
                                    daysWorking: null,
                                    excSologroup: false,
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
            });
    }

})();
