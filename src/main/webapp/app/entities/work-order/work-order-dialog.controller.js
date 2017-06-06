(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderDialogController', WorkOrderDialogController);

    WorkOrderDialogController.$inject = ['$scope', '$stateParams', 'entity', 'WorkOrder', 'Lookups', 'Projects', 'User', 'Contacts','$uibModalInstance'];

    function WorkOrderDialogController ($scope, $stateParams, entity, WorkOrder, Lookups, Projects, User, Contacts,$uibModalInstance) {
        var vm = this;
        vm.workOrder = entity;
        vm.lookupss = Lookups.query();
        vm.projectss = Projects.query();
        vm.users = User.query();
        vm.contactss = Contacts.query();
        vm.load = function(id) {
            WorkOrder.get({id : id}, function(result) {
                vm.workOrder = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('smartLpcApp:workOrderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.workOrder.id !== null) {
                WorkOrder.update(vm.workOrder, onSaveSuccess, onSaveError);
            } else {
                WorkOrder.save(vm.workOrder, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.requestDate = false;
        vm.datePickerOpenStatus.reminderDate1 = false;
        vm.datePickerOpenStatus.reminderDate2 = false;
        vm.datePickerOpenStatus.reminderDate3 = false;
        vm.datePickerOpenStatus.processingDateRecieved = false;
        vm.datePickerOpenStatus.processingDateShipped = false;
        vm.datePickerOpenStatus.dueToClientReminder = false;
        vm.datePickerOpenStatus.dueToMounterReminder = false;
        vm.datePickerOpenStatus.recievedFromMounterReminder = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;
        vm.datePickerOpenStatus.completionDate = false;
        vm.datePickerOpenStatus.processingProofShipped = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
