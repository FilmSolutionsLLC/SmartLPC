(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ImageDeleteController',ImageDeleteController);

    ImageDeleteController.$inject = ['$uibModalInstance', 'entity', 'Image'];

    function ImageDeleteController($uibModalInstance, entity, Image) {
        var vm = this;
        vm.image = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Image.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
