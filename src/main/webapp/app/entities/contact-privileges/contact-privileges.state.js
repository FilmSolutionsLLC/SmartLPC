(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contact-privileges', {
            parent: 'entity',
            url: '/contact-privileges?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.contactPrivileges.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contact-privileges/contact-privileges.html',
                    controller: 'ContactPrivilegesController',
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
                    $translatePartialLoader.addPart('contactPrivileges');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contact-privileges-detail', {
            parent: 'entity',
            url: '/contact-privileges/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.contactPrivileges.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contact-privileges/contact-privileges-detail.html',
                    controller: 'ContactPrivilegesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contactPrivileges');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ContactPrivileges', function($stateParams, ContactPrivileges) {
                    return ContactPrivileges.get({id : $stateParams.id});
                }]
            }
        })
        .state('contact-privileges.new', {
            parent: 'contact-privileges',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-privileges/contact-privileges-dialog.html',
                    controller: 'ContactPrivilegesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                title: null,
                                description: null,
                                author: null,
                                exec: 1,
                                captioning: 0,
                                downloadType: 0,
                                email: 0,
                                print: 0,
                                lockApproveRestriction: 0,
                                priorityPix: 0,
                                releaseExclude: 0,
                                viewSensitive: 0,
                                watermark: 0,
                                watermarkInnerTransparency: null,
                                watermarkOuterTransparency: null,
                                internal: 0,
                                vendor: 0,
                                restartRole: null,
                                restartImage: null,
                                restartPage: 0,
                                lastLoginDt: null,
                                lastLogoutDt: null,
                                disabled: 0,
                                welcomeMessage: null,
                                isABCViewer: null,
                                talentManagement: 0,
                                signoffManagement: 0,
                                datgeditManagement: 0,
                                createdDate: null,
                                updatedDate: null,
                                expireDate: null,
                                restartFilter: null,
                                restartColumns: 2,
                                restartImagesPerPage: 20,
                                restartImageSize: "Large",
                                restartTime: null,
                                showFinalizations: 0,
                                readOnly: 0,
                                hasVideo: 0,
                                globalAlbum: false,
                                seesUntagged: 0,
                                loginCount: 0,
                                exclusives: 0,
                                defaultAlbum: null,
                                critiqueIt: false,
                                adhocLink: false,
                                retouch: false,
                                fileUpload: false,
                                deleteAssets: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contact-privileges', null, { reload: true });
                }, function() {
                    $state.go('contact-privileges');
                });
            }]
        })
        .state('contact-privileges.edit', {
            parent: 'contact-privileges',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-privileges/contact-privileges-dialog.html',
                    controller: 'ContactPrivilegesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContactPrivileges', function(ContactPrivileges) {
                            return ContactPrivileges.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact-privileges', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact-privileges.delete', {
            parent: 'contact-privileges',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-privileges/contact-privileges-delete-dialog.html',
                    controller: 'ContactPrivilegesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ContactPrivileges', function(ContactPrivileges) {
                            return ContactPrivileges.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact-privileges', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
