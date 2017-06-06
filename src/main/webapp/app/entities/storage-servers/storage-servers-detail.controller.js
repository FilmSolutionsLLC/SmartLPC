(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('Storage_ServersDetailController', Storage_ServersDetailController);

    Storage_ServersDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Storage_Servers'];

    function Storage_ServersDetailController($scope, $rootScope, $stateParams, entity, Storage_Servers) {
        var vm = this;
        vm.storage_Servers = entity;
        vm.load = function (id) {
            Storage_Servers.get({id: id}, function(result) {
                vm.storage_Servers = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:storage_ServersUpdate', function(event, result) {
            vm.storage_Servers = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
