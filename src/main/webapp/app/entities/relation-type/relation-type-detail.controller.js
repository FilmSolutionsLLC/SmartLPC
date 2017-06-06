(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('RelationTypeDetailController', RelationTypeDetailController);

    RelationTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RelationType'];

    function RelationTypeDetailController($scope, $rootScope, $stateParams, entity, RelationType) {
        var vm = this;
        vm.relationType = entity;
        vm.load = function (id) {
            RelationType.get({id: id}, function(result) {
                vm.relationType = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:relationTypeUpdate', function(event, result) {
            vm.relationType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
