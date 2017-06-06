(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderAbcHddDialogController', WorkOrderAbcHddDialogController);

    WorkOrderAbcHddDialogController.$inject = ['$http','$scope', '$stateParams', '$uibModalInstance', 'entity', 'WorkOrderAbcHdd', 'WorkOrder', 'Lookups'];

    function WorkOrderAbcHddDialogController ($http,$scope, $stateParams, $uibModalInstance, entity, WorkOrderAbcHdd, WorkOrder, Lookups) {
        var vm = this;
        vm.workOrderAbcHdd = entity;
        vm.workorders = WorkOrder.query();
        vm.lookupss = Lookups.query();


        vm.abcHdd = {};
        $http({
            method: 'GET',
            url: '/api/lookups/get/work_orders/abc_hdd_to'
        }).then(function successCallback(response) {
            vm.abcHdd = response.data;

        }, function errorCallback(response) {
        });


        vm.load = function(id) {
            WorkOrderAbcHdd.get({id : id}, function(result) {
                vm.workOrderAbcHdd = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:workOrderAbcHddUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.workOrderAbcHdd.id !== null) {
                WorkOrderAbcHdd.update(vm.workOrderAbcHdd, onSaveSuccess, onSaveError);
            } else {
                WorkOrderAbcHdd.save(vm.workOrderAbcHdd, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
