(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('search', {
                parent: 'app',
                url: '/search',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/search/search-dialog.html',
                        controller: 'SearchDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md'
                    }).result.then(function () {
                        $state.go('contacts', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('search.display', {
                parent: 'app',
                url: '/searchResults?search&type',
                params: {
                    search: null,
                    type: null
                },
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/search/search.html',
                        controller: 'SearchController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {

                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('workOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
        ;

    }
})();
