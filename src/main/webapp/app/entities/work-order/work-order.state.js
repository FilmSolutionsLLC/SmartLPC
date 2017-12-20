(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {


        $stateProvider
            .state('work-order', {
                parent: 'entity',
                url: '/work-order?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.workOrder.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-orders.html',
                        controller: 'WorkOrderController',
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
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('work-order-detail', {
                parent: 'entity',
                url: '/work-order/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.workOrder.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-detail.html',
                        controller: 'WorkOrderDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'WorkOrder', function ($stateParams, WorkOrder) {
                        return WorkOrder.get({id: $stateParams.id}).$promise;

                    }]
                }
            })
            .state('work-order.new', {
                parent: 'work-order',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/work-order/work-order-dialog.html',
                        controller: 'WorkOrderDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    isPrint: null,
                                    isProof: null,
                                    isAbc: null,
                                    isTracking: null,
                                    requestDate: null,
                                    requestDescription: null,
                                    auditedFlag: null,
                                    poRecord: null,
                                    invoiced: null,
                                    invoiceNumber: null,
                                    isAltCredit: null,
                                    overwrite: null,
                                    printSet: null,
                                    printQuantity: null,
                                    printDaysFrom: null,
                                    printDaysTo: null,
                                    printPagesFrom: null,
                                    printPagesTo: null,
                                    reminderDate1: null,
                                    reminderMsg1: null,
                                    reminderDate2: null,
                                    reminderMsg2: null,
                                    reminderDate3: null,
                                    reminderMsg3: null,
                                    processingDateRecieved: null,
                                    processingHDDid: null,
                                    processingDateShipped: null,
                                    processingNote: null,
                                    processingLocation: null,
                                    processingImageRange: null,
                                    processingImageQty: null,
                                    dueToClientReminder: null,
                                    dueToMounterReminder: null,
                                    recievedFromMounterReminder: null,
                                    abcInstruction: null,
                                    abcRawDvd: null,
                                    abcTalentCount: null,
                                    kickBack: null,
                                    createdDate: null,
                                    updatedDate: null,
                                    completionDate: null,
                                    durationOfService: null,
                                    processingProofShipped: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('work-order', null, {reload: true});
                    }, function () {
                        $state.go('work-order');
                    });
                }]
            })
            .state('work-order.add', {
                parent: 'work-order',
                url: '/add',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-add.html',
                        controller: 'WorkOrderAddController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        resolve: {
                            entity: function () {
                                return {
                                    isPrint: null,
                                    isProof: null,
                                    isAbc: null,
                                    isTracking: null,
                                    requestDate: null,
                                    requestDescription: null,
                                    auditedFlag: null,
                                    poRecord: null,
                                    invoiced: null,
                                    invoiceNumber: null,
                                    isAltCredit: null,
                                    overwrite: null,
                                    printSet: null,
                                    printQuantity: null,
                                    printDaysFrom: null,
                                    printDaysTo: null,
                                    printPagesFrom: null,
                                    printPagesTo: null,
                                    reminderDate1: null,
                                    reminderMsg1: null,
                                    reminderDate2: null,
                                    reminderMsg2: null,
                                    reminderDate3: null,
                                    reminderMsg3: null,
                                    processingDateRecieved: null,
                                    processingHDDid: null,
                                    processingDateShipped: null,
                                    processingNote: null,
                                    processingLocation: null,
                                    processingImageRange: null,
                                    processingImageQty: null,
                                    dueToClientReminder: null,
                                    dueToMounterReminder: null,
                                    recievedFromMounterReminder: null,
                                    abcInstruction: null,
                                    abcRawDvd: null,
                                    abcTalentCount: null,
                                    kickBack: null,
                                    createdDate: null,
                                    updatedDate: null,
                                    completionDate: null,
                                    durationOfService: null,
                                    processingProofShipped: null,
                                    id: null,
                                    verifiedBy: null,
                                    archivedBy: null,
                                    processedBy: null,
                                    ingestBy: null,
                                    printBy: null,
                                    uploadBy: null,
                                    project: null,
                                    type: null

                                };
                            }
                        }
                    }
                }
            })
            /*
			 * .state('work-order.edit', { parent: 'work-order', url:
			 * '/{id}/edit', data: { authorities: ['ROLE_USER'] }, onEnter:
			 * ['$stateParams', '$state', '$uibModal', function ($stateParams,
			 * $state, $uibModal) { $uibModal.open({ templateUrl:
			 * 'app/entities/work-order/work-order-dialog.html', controller:
			 * 'WorkOrderDialogController', controllerAs: 'vm', backdrop:
			 * 'static', size: 'lg', resolve: { entity: ['WorkOrder', function
			 * (WorkOrder) { return WorkOrder.get({id: $stateParams.id}); }] }
			 * }).result.then(function () { $state.go('work-order', null,
			 * {reload: true}); }, function () { $state.go('^'); }); }] })
			 */
            .state('work-order.edit', {
                parent: 'work-order',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.workOrder.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-update.html',
                        controller: 'WorkOrderUpdateController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'WorkOrder', function ($stateParams, WorkOrder) {
                        return WorkOrder.get({id: $stateParams.id}).$promise;

                    }]
                }
            })
            .state('work-order.delete', {
                parent: 'work-order',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/work-order/work-order-delete-dialog.html',
                        controller: 'WorkOrderDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['WorkOrder', function (WorkOrder) {
                                return WorkOrder.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('work-order', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('projectinfo', {
                parent: 'entity',
                url: '/projectinfo',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projectinfo.home.title'
                },
                /* views: {
                     'content@': {
                         templateUrl: 'app/entities/work-order/work-order-audit.html',
                         controller: 'WorkOrderAuditController',
                         controllerAs: 'vm'
                     }
                 },
                 resolve: {
                     translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                         $translatePartialLoader.addPart('workOrder');
                         $translatePartialLoader.addPart('global');
                         return $translate.refresh();
                     }]
                 }*/
                views: {
                    'content@': {
                        controller: 'ProjectInfoRouter',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }

            })
            .state('work-order-audit', {
                parent: 'entity',
                url: '/work-order-audit',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projectinfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-audit.html',
                        controller: 'WorkOrderAuditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('work-order-open', {
                parent: 'entity',
                url: '/work-order-open',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projectinfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-open.html',
                        controller: 'WorkOrderOpenController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('work-order-grouped', {
                parent: 'entity',
                url: '/work-order-grouped',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.work-order.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-grouped.html',
                        controller: 'WorkOrderGroupedController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('work-order-included-comp', {
                parent: 'entity',
                url: '/work-order-included-comp',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projectinfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-included-comp.html',
                        controller: 'WorkOrderIncludedCompController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('work-order-invoice', {
                parent: 'entity',
                url: '/work-order-invoice',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projectinfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-invoice.html',
                        controller: 'WorkOrderInvoiceController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('work-order-my-open', {
                parent: 'entity',
                url: '/work-order-my-open',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projectinfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-my-open.html',
                        controller: 'WorkOrderMyOpenController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })


            .state('work-order-processing-log', {
                parent: 'entity',
                url: '/work-order-processing-log',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projectinfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-processing-log.html',
                        controller: 'WorkOrderProcessingController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })

            .state('multiple-workorder-reports', {
                parent: 'entity',
                url: '/multiple-workorder-reports',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.projectinfo.home.title'
                },
                params: {
                    user: null
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/multiple-workorder-reports.html',
                        controller: 'MultipleWorkOrderReports',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    user: ['$stateParams',function ($stateParams) {
                        console.log("---> "+JSON.stringify($stateParams.user))
                       return $stateParams.user;
                    }]
                }
            })

        /*.state('work-order-invoice', {
            parent: 'entity',
            url: '/work-order-invoice?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.work-order.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-order/work-order-invoice.html',
                    controller: 'WorkOrderInvoiceController',
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
                    $translatePartialLoader.addPart('workOrder');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })*/
        /*    .state('work-order-included-comp', {
                parent: 'entity',
                url: '/work-order-included-comp-log?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'smartLpcApp.work-order.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/work-order/work-order-included-comp.html',
                        controller: 'WorkOrderInvoicedCompController',
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
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            */


        /* .state('work-order-my-open', {
             parent: 'entity',
             url: '/work-order-my-open?page&sort&search',
             data: {
                 authorities: ['ROLE_USER'],
                 pageTitle: 'smartLpcApp.work-order.home.title'
             },
             views: {
                 'content@': {
                     templateUrl: 'app/entities/work-order/work-order-my-open.html',
                     controller: 'WorkOrderMyOpenController',
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
                     $translatePartialLoader.addPart('workOrder');
                     $translatePartialLoader.addPart('global');
                     return $translate.refresh();
                 }]
             }
         })*/
        /*.state('work-order-processing', {
            parent: 'entity',
            url: '/processing/work-order?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.workOrder.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-order/work-order-processing-log.html',
                    controller: 'WorkOrderProcessingController',
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
                    $translatePartialLoader.addPart('workOrder');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });*/

    }

})();
