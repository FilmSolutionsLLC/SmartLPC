(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('LookupsDetailController', LookupsDetailController);

    LookupsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Lookups'];

    function LookupsDetailController($scope, $rootScope, $stateParams, entity, Lookups) {
        var vm = this;
        vm.lookups = entity;
        vm.load = function (id) {
            Lookups.get({id: id}, function (result) {
                console.log("vm.load called..");
                vm.lookups = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:lookupsUpdate', function (event, result) {
            vm.lookups = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
