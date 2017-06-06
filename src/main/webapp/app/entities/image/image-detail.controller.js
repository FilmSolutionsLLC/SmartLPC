(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ImageDetailController', ImageDetailController);

    ImageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Image', 'Batch', 'User'];

    function ImageDetailController($scope, $rootScope, $stateParams, entity, Image, Batch, User) {
        console.log("ImageDetailController");
        var vm = this;
        vm.image = entity;
        vm.load = function (id) {
            Image.get({id: id}, function(result) {
                vm.image = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:imageUpdate', function(event, result) {
            vm.image = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
