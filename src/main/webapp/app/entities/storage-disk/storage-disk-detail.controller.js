(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('Storage_DiskDetailController', Storage_DiskDetailController);

    Storage_DiskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Storage_Disk', 'Storage_Servers'];

    function Storage_DiskDetailController($scope, $rootScope, $stateParams, entity, Storage_Disk, Storage_Servers) {
        var vm = this;
        vm.storage_Disk = entity;
        vm.load = function (id) {
            Storage_Disk.get({id: id}, function(result) {
                vm.storage_Disk = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:storage_DiskUpdate', function(event, result) {
            vm.storage_Disk = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
