(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('IngestDetailController', IngestDetailController);

    IngestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Ingest', 'User', 'Lookups', 'Storage_Servers'];

    function IngestDetailController($scope, $rootScope, $stateParams, entity, Ingest, User, Lookups, Storage_Servers) {
        var vm = this;
        vm.ingest = entity;
        vm.load = function (id) {
            Ingest.get({id: id}, function(result) {
                vm.ingest = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:ingestUpdate', function(event, result) {
            vm.ingest = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
