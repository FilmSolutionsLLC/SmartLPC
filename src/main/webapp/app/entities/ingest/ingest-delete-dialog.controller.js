(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('IngestDeleteController',IngestDeleteController);

    IngestDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ingest'];

    function IngestDeleteController($uibModalInstance, entity, Ingest) {
        var vm = this;
        vm.ingest = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Ingest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
