(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('RelationTypeDeleteController',RelationTypeDeleteController);

    RelationTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'RelationType'];

    function RelationTypeDeleteController($uibModalInstance, entity, RelationType) {
        var vm = this;
        vm.relationType = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            RelationType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
