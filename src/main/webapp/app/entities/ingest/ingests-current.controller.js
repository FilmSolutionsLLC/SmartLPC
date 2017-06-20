/**
 * Created by macbookpro on 2/2/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('IngestsCurrentController', IngestsCurrentController);

    IngestsCurrentController.$inject = ['$timeout', '$uibModal', 'DateUtils', '$http', '$scope', '$state', 'Ingest', 'IngestSearch', 'paginationConstants'];

    function IngestsCurrentController($timeout, $uibModal, DateUtils, $http, $scope, $state, Ingest, IngestSearch, paginationConstants) {

        // get selection
        $('.colors input[type=radio]').on('change', function () {
            console.log(this.value);
        });

        var vm = this;
        vm.runningIngests = [];
        $scope.reload = function () {
            $http({
                method: 'GET',
                url: '/api/running/ingest'
            }).then(function successCallback(response) {
                response.data.ingestStartTime = DateUtils.convertDateTimeFromServer(response.data.ingestStartTime);
                vm.runningIngests = response.data;
                console.log(JSON.stringify(vm.runningIngests));

            }, function errorCallback(response) {

            });
            $timeout(function () {
                $scope.reload();
                console.log("reloaded");
            }, 5000)
        };
        $scope.reload();

        vm.progress = function (currentIngest) {
            console.log(JSON.stringify(currentIngest));


            var modalInstance = $uibModal.open({


                templateUrl: 'app/entities/ingest/progress-ingest.html',
                controller: 'ProgressIngestsController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',

                resolve: {
                    sendIngestID: function () {
                        return currentIngest;
                    }
                }
            })


        };
        vm.pauseResume = "pause";
        vm.pause = function (currentIngest) {

            console.log(JSON.stringify(currentIngest));
            $http({
                method: 'PUT',
                url: 'api/filesystem/pause',
                data: currentIngest
            }).then(function successCallback(response) {

                $scope.reload();
            }, function errorCallback(response) {

            });
        };
        vm.resume = function (currentIngest) {

            console.log(JSON.stringify(currentIngest));
            $http({
                method: 'PUT',
                url: 'api/filesystem/resume',
                data: currentIngest
            }).then(function successCallback(response) {

                $scope.reload();
            }, function errorCallback(response) {

            });
        };


        vm.stop = function (currentIngest) {
            console.log(JSON.stringify(currentIngest));
            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/ingest/stop-ingest.html',
                controller: 'StopIngestsController',
                size: 'md',
                scope: $scope,
                controllerAs: 'vm',
                resolve: {
                    sendIngest: function () {
                        return currentIngest;
                    }
                }
            })
        };
    }
})();
