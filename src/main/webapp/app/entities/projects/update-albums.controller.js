/**
 * Created by macbookpro on 12/27/16.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('UpdateAlbumsController', UpdateAlbumsController);
    UpdateAlbumsController.$inject = ['id', 'projectID', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state', 'Projects'];

    function UpdateAlbumsController(id, projectID, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state, Projects) {


        var vm = this;

        $http({
            method: 'GET',
            url: 'api/talents',
            params: {
                id: id,
                type: 'albums'

            }
        }).then(function successCallback(response) {
            vm.albums = response.data;

            console.log(JSON.stringify(vm.albums));
            $scope.totalItems = vm.albums.length;
            console.log("Albums Length : " + $scope.totalItems);
        }, function errorCallback(response) {

        });


        vm.close = function () {

            $uibModalInstance.dismiss('cancel');
        };


        $scope.viewby = 10;
        //$scope.totalItems = null;
        $scope.currentPage = 1;
        $scope.itemsPerPage = $scope.viewby;
        $scope.maxSize = 5; //Number of pager buttons to show


        $scope.setPage = function (pageNo) {
            $scope.currentPage = pageNo;
        };

        $scope.pageChanged = function () {
            console.log('Page changed to: ' + $scope.currentPage);
        };

        $scope.setItemsPerPage = function (num) {

            $scope.itemsPerPage = num;
            $scope.currentPage = 1; //reset to first paghe
        };

        vm.add = function () {
            vm.tempRecord = {id: null, type: '', value: ''};
            vm.albums.unshift(vm.tempRecord);
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

                }, function errorCallback(response) {

                });
            }
            else {
                $http({
                    method: 'POST',
                    url: 'api/update/album',
                    data: tags
                }).then(function successCallback(response) {
                	alert("Album name changed to : "+tags.value);
                }, function errorCallback(response) {

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
