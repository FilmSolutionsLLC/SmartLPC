(function () {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('WorkOrderPorcessing', WorkOrderPorcessing);

    WorkOrderPorcessing.$inject = ['$resource', 'DateUtils'];

    function WorkOrderPorcessing($resource, DateUtils) {
        var resourceUrl = 'api/processing/work-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.workOrder.requestDate = DateUtils.convertLocalDateFromServer(data.workOrder.requestDate);
                    data.workOrder.reminderDate1 = DateUtils.convertLocalDateFromServer(data.workOrder.reminderDate1);
                    data.workOrder.reminderDate2 = DateUtils.convertLocalDateFromServer(data.workOrder.reminderDate2);
                    data.workOrder.reminderDate3 = DateUtils.convertLocalDateFromServer(data.workOrder.reminderDate3);
                    data.workOrder.processingDateRecieved = DateUtils.convertLocalDateFromServer(data.workOrder.processingDateRecieved);
                    data.workOrder.processingDateShipped = DateUtils.convertLocalDateFromServer(data.workOrder.processingDateShipped);
                    data.workOrder.dueToClientReminder = DateUtils.convertLocalDateFromServer(data.workOrder.dueToClientReminder);
                    data.workOrder.dueToMounterReminder = DateUtils.convertLocalDateFromServer(data.workOrder.dueToMounterReminder);
                    data.recievedFromMounterReminder = DateUtils.convertLocalDateFromServer(data.recievedFromMounterReminder);
                    data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                    data.updatedDate = DateUtils.convertDateTimeFromServer(data.updatedDate);
                    data.workOrder.completionDate = DateUtils.convertLocalDateFromServer(data.workOrder.completionDate);
                    data.workOrder.processingProofShipped = DateUtils.convertLocalDateFromServer(data.workOrder.processingProofShipped);
                    return data;
                }
            }
        });
    }
})();
