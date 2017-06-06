(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ContactRelationshipsDetailController', ContactRelationshipsDetailController);

    ContactRelationshipsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ContactRelationships', 'User', 'Contacts'];

    function ContactRelationshipsDetailController($scope, $rootScope, $stateParams, entity, ContactRelationships, User, Contacts) {
        var vm = this;
        vm.contactRelationships = entity;
        vm.load = function (id) {
            ContactRelationships.get({id: id}, function(result) {
                vm.contactRelationships = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:contactRelationshipsUpdate', function(event, result) {
            vm.contactRelationships = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
