/**
 * Created by macbookpro on 2/2/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProgressIngestsController', ProgressIngestsController);

    ProgressIngestsController.$inject = ['$uibModalInstance', 'sendIngestID', '$uibModal', 'DateUtils', '$http', '$scope', '$state', 'Ingest', 'IngestSearch', 'paginationConstants'];

    function ProgressIngestsController($uibModalInstance, sendIngestID, $uibModal, DateUtils, $http, $scope, $state, Ingest, IngestSearch, paginationConstants) {

        var vm = this;

        //   vm.currentIngest = sendIngestID;

        $http({
            method: 'GET',
            url: '/api/filesystem/progress/' + sendIngestID
        }).then(function successCallback(response) {
            vm.currentIngest = response.data;
        }, function errorCallback(response) {

        });
        vm.close = function () {
            $uibModalInstance.dismiss('cancel');
        };

        console.log("========= >  CURRENT INGEST");
        console.log(JSON.stringify(vm.currentIngest));

    }
})();
