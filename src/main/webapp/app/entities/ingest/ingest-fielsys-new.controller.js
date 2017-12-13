(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('IngestFileSysController', IngestFileSysController);

    IngestFileSysController.$inject = ['entity', '$http', '$scope', '$state', 'Ingest', 'IngestSearch', 'AlertService'];

    function IngestFileSysController(entity, $http, $scope, $state, Ingest, IngestSearch, AlertService) {
        var vm = this;

        vm.ingestActions = ["Run Ingest Only", "Convert from Zooms", "Convert from SuperZooms", "Convert from Masters"];
        vm.priority = ["LOW", "NORMAL", "HIGH"];


        vm.newIngests = [];
        $http({
            method: 'GET',
            url: 'api/filesystem/ingest'
        }).then(function successCallback(response) {
            console.log("Gettting all ingests")
            vm.newIngests = response.data;
            console.log(JSON.stringify(vm.newIngests));
            $scope.totalItems = vm.newIngests.length;
        }, function errorCallback(response) {

        });

        $http({
            method: 'GET',
            url: 'api/logos/ingest'
        }).then(function successCallback(response) {
            console.log("Getting all logos");
            vm.logos = response.data;
            vm.logos.splice(0, 0, "NONE");
            console.log(JSON.stringify(vm.logos));

        }, function errorCallback(response) {

        });

        $scope.viewby = 10;

        $scope.currentPage = 1;
        $scope.itemsPerPage = $scope.viewby;
        $scope.maxSize = 5; //Number of pager buttons to show

        console.log("total items : " + $scope.totalItems);
        $scope.setPage = function (pageNo) {
            $scope.currentPage = pageNo;
        };
        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.pageChanged = function () {
            console.log('Page changed to: ' + $scope.currentPage);
        };

        $scope.setItemsPerPage = function (num) {
            $scope.itemsPerPage = num;
            $scope.currentPage = 1; //reset to first paghe
        };


        vm.ingest = entity;
        vm.save = function (ingests) {
            vm.ingest.exists = true;
            console.log("starting ingest : " + JSON.stringify(ingests));

            if (ingests.priority == "HIGH") {
                console.log("priority has been set high");
                if (confirm("Are you sure you want to set HIGH priority?") == true) {
                    console.log("u pressed okay")
                    // call  /filesystem/highpriority
                    $http({
                        method: 'GET',
                        url: 'api/filesystem/highpriority'
                    }).then(function successCallback(response) {
                        vm.flag = angular.fromJson(response.data);
                        console.log(vm.flag);
                        if (angular.equals(vm.flag, 'true')) {
                            console.log("we got true : " + vm.flag)
                        } else {
                            console.log("we got false : " + vm.flag)
                            // show warning...nothing free // tell them to pause on of the ingest
                            alert("No Server free for Ingest." +
                                "Pause or Kill one of the Ingest(or just wait)")
                        }
                    }, function errorCallback(response) {

                    });

                } else {
                    console.log("u pressed cancel");
                }
            }
            $http({
                method: 'POST',
                url: 'api/filesystem/ingest',
                data: ingests
            }).then(function successCallback(response) {
                console.log("POST successful");
                $state.go("ingest.current");
            }, function errorCallback(response) {
                console.log("POST unsuccessful ");
            });
        };


    }
})();
