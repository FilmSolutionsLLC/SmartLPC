(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderAbcFileDialogController', WorkOrderAbcFileDialogController);

    WorkOrderAbcFileDialogController.$inject = ['$http','$scope', '$stateParams', '$uibModalInstance', 'entity', 'WorkOrderAbcFile', 'WorkOrder', 'Lookups'];

    function WorkOrderAbcFileDialogController ($http,$scope, $stateParams, $uibModalInstance, entity, WorkOrderAbcFile, WorkOrder, Lookups) {
        var vm = this;
        vm.workOrderAbcFile = entity;
        vm.workorders = WorkOrder.query();
        vm.lookupss = Lookups.query();


        vm.workOrderAbcFileType = {};
        $http({
            method: 'GET',
            url: '/api/lookups/get/work_orders/abc_file_type'
        }).then(function successCallback(response) {
            vm.workOrderAbcFileType = response.data;

        }, function errorCallback(response) {
        });


        vm.load = function(id) {
            WorkOrderAbcFile.get({id : id}, function(result) {
                vm.workOrderAbcFile = result;
            });
        };


        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:workOrderAbcFileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.workOrderAbcFile.id !== null) {
                WorkOrderAbcFile.update(vm.workOrderAbcFile, onSaveSuccess, onSaveError);
            } else {
                WorkOrderAbcFile.save(vm.workOrderAbcFile, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
