(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('Storage_DiskDeleteController',Storage_DiskDeleteController);

    Storage_DiskDeleteController.$inject = ['$uibModalInstance', 'entity', 'Storage_Disk'];

    function Storage_DiskDeleteController($uibModalInstance, entity, Storage_Disk) {
        var vm = this;
        vm.storage_Disk = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Storage_Disk.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
