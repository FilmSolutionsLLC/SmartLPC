/**
 * Created by macbookpro on 12/27/16.
 */
(function() {
    'use strict';

    angular.module('smartLpcApp').controller('TopLevelAlbumsController',
        TopLevelAlbumsController);
    TopLevelAlbumsController.$inject = [ 'projectID','Principal', '$http',
        '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User',
        'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope',
        '$state' ];

    function TopLevelAlbumsController(projectID,Principal,  $http, $rootScope,
                                       Contacts, Lookups, Departments, User, ContactsSearch, AlertService,
                                       $uibModalInstance, $scope, $state) {

        var vm = this;

        vm.projectID = projectID;
        vm.close = function() {

            $uibModalInstance.dismiss('cancel');
        };

        vm.albums = [];
        $http({
            method: 'GET',
            url: 'api/talents',
            params: {
                id: projectID,
                type: 'albums'

            }
        }).then(function successCallback(response) {
            vm.albums = response.data;

            console.log(JSON.stringify(vm.albums));
            $scope.totalItems = vm.albums.length;
            console.log("Albums Length : " + $scope.totalItems);

        }, function errorCallback(response) {

        });


        $http({
            method: 'GET',
            url: 'api/talents',
            params: {
                id: projectID,
                type: 'privileges'

            }
        }).then(function successCallback(response) {
            vm.privileges = response.data;
            console.log("Privileges : ");
            console.log(JSON.stringify(vm.privileges));
            $scope.totalItems = vm.privileges.length;
        }, function errorCallback(response) {

        });

    }
})();
