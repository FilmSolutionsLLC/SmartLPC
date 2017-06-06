(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('Storage_ServersDeleteController',Storage_ServersDeleteController);

    Storage_ServersDeleteController.$inject = ['$uibModalInstance', 'entity', 'Storage_Servers'];

    function Storage_ServersDeleteController($uibModalInstance, entity, Storage_Servers) {
        var vm = this;
        vm.storage_Servers = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Storage_Servers.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
