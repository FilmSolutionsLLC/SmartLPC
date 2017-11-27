/**
 * Created by macbookpro on 6/20/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('IndividualALbumController', IndividualALbumController);
    IndividualALbumController.$inject = ['projectID','$uibModal','data','$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state', 'Projects', 'ContactPrivileges'];

    function IndividualALbumController(projectID,$uibModal, data,$http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state, Projects, ContactPrivileges) {
        var vm = this;

        vm.data = data;
        console.log("Data: "+JSON.stringify(data));


        vm.close = function () {

            $uibModalInstance.dismiss('cancel');
        };


        vm.edit = function (tags) {
            console.log(tags.id);
            console.log(tags.type);
            console.log(tags.value);

            if (tags.id === null) {
                console.log("inserting data here");
                tags.id = projectID;
                $http({
                    method: 'POST',
                    url: 'api/insert/album',
                    data: tags
                }).then(function successCallback(response) {
                    alert("New Album Created");
                    $uibModalInstance.dismiss('cancel');
                }, function errorCallback(response) {
                    $uibModalInstance.dismiss('cancel');

                });
            }
            else {
                $http({
                    method: 'POST',
                    url: 'api/update/album',
                    data: tags
                }).then(function successCallback(response) {
                    alert("Album name changed to : "+tags.value);
                    $uibModalInstance.dismiss('cancel');
                }, function errorCallback(response) {
                    $uibModalInstance.dismiss('cancel');
                });
            }
        };

        vm.remove = function (tags) {
            if (confirm("Are you sure you want to REMOVE album ? \n" + tags.type + " - " + tags.value) == true) {
                console.log("u pressed okay")
                $http({
                    method: 'GET',
                    url: 'api/remove',
                    params: {
                        id: tags.id,
                        type: 'albums'

                    }
                }).then(function successCallback(response) {
                    var index = vm.albums.indexOf(tags);
                    console.log("index spliced  : " + index);
                    if (index != -1)
                        vm.albums.splice(index, 1);
                }, function errorCallback(response) {

                });


            } else {
                console.log("u pressed cancel");
            }
        };
    }
})();
