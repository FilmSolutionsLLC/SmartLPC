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
                                exec: null,
                                captioning: null,
                                downloadType: null,
                                email: null,
                                print: null,
                                lockApproveRestriction: null,
                                priorityPix: null,
                                releaseExclude: null,
                                viewSensitive: null,
                                watermark: null,
                                watermarkInnerTransparency: null,
                                watermarkOuterTransparency: null,
                                internal: null,
                                vendor: null,
                                restartRole: null,
                                restartImage: null,
                                restartPage: null,
                                lastLoginDt: null,
                                lastLogoutDt: null,
                                disabled: null,
                                welcomeMessage: null,
                                isABCViewer: null,
                                talentManagement: null,
                                signoffManagement: null,
                                datgeditManagement: null,
                                createdDate: null,
                                updatedDate: null,
                                expireDate: null,
                                restartFilter: null,
                                restartColumns: null,
                                restartImagesPerPage: null,
                                restartImageSize: null,
                                restartTime: null,
                                showFinalizations: null,
                                readOnly: null,
                                hasVideo: null,
                                globalAlbum: null,
                                seesUntagged: null,
                                loginCount: null,
                                exclusives: null,
                                defaultAlbum: null,
                                critiqueIt: null,
                                adhocLink: null,
                                retouch: null,
                                fileUpload: null,
                                deleteAssets: null,
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
