(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('IngestDialogController', IngestDialogController);

    IngestDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ingest', 'User', 'Lookups', 'Storage_Servers', '$http'];

    function IngestDialogController($scope, $stateParams, $uibModalInstance, entity, Ingest, User, Lookups, Storage_Servers, $http) {
        var vm = this;
        vm.ingest = entity;
        vm.users = User.query();
        vm.lookupss = {};
        $http({
            method: 'GET',
            url: '/api/lookups/projects/serverTasks'
        }).then(function successCallback(response) {
            vm.lookupss = response.data;
        }, function errorCallback(response) {

        });
        vm.storage_serverss = Storage_Servers.query();
        vm.load = function (id) {
            Ingest.get({id: id}, function (result) {
                vm.ingest = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:ingestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.ingest.id !== null) {
                Ingest.update(vm.ingest, onSaveSuccess, onSaveError);
            } else {
                Ingest.save(vm.ingest, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.ingestStartTime = false;
        vm.datePickerOpenStatus.ingestCompletedTime = false;

        vm.openCalendar = function (date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
