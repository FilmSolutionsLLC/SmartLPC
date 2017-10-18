(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('CaptionsDetailController', CaptionsDetailController);

    CaptionsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Captions', 'Projects'];

    function CaptionsDetailController($scope, $rootScope, $stateParams, entity, Captions, Projects) {
        var vm = this;
        vm.captions = entity;
        console.log("Page called")

        console.log("Printing entity : "+JSON.stringify(entity));
        vm.load = function (id) {
            Captions.get({id: id}, function(result) {
                vm.captions = result;
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:captionsUpdate', function(event, result) {
            vm.captions = result;
        });
        $scope.$on('$destroy', unsubscribe);


    }
})();
