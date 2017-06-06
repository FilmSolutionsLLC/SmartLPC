/**
 * Created by macbookpro on 2/2/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('StopIngestsController', StopIngestsController);

    StopIngestsController.$inject = ['$uibModalInstance', 'sendIngest', '$uibModal', 'DateUtils', '$http', '$scope', '$state', 'Ingest', 'IngestSearch', 'paginationConstants'];

    function StopIngestsController($uibModalInstance, sendIngest, $uibModal, DateUtils, $http, $scope, $state, Ingest, IngestSearch, paginationConstants) {

        var vm = this;


        vm.currentIngest = sendIngest;
        console.log("========= >  SOPTING INGEST");
        console.log(JSON.stringify(vm.currentIngest));

        vm.kill = function (ingest) {

            $http.put("/api/filesystem/ingests",ingest).
            success(function(data, status, headers, config) {
                // this callback will be called asynchronously
                // when the response is available
                console.log(data);
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });

           /* $http({
                method: 'PUT',
                url: '/api/filesystem/ingests'
            }).then(function success(ingest, response, headers) {


            }, function error(response) {

            });*/
            $uibModalInstance.dismiss('cancel');
            $state.go("ingest.current");
        };
        vm.close = function () {

            $uibModalInstance.dismiss('cancel');
        };
    }
})();
