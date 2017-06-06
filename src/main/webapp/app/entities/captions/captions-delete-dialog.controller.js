(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('CaptionsDeleteController',CaptionsDeleteController);

    CaptionsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Captions'];

    function CaptionsDeleteController($uibModalInstance, entity, Captions) {
        var vm = this;
        vm.captions = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Captions.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
