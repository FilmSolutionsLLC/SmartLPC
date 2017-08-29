(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('chat', {
            parent: 'app',
            url: '/chat',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartLpcApp.captions.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chat/chat.html',
                    controller: 'ChatController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
               
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                   
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }
})();
