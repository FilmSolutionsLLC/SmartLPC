(function () {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('WorkOrder', WorkOrder);

    WorkOrder.$inject = ['$resource', 'DateUtils'];

    function WorkOrder($resource, DateUtils) {
        var resourceUrl = 'api/work-orders/:id';

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
            },
            'update': {
                method: 'PUT',

                transformRequest: function (data) {
                  /*  data.requestDate = DateUtils.convertLocalDateToServer(data.workOrder.requestDate);
                    data.reminderDate1 = DateUtils.convertLocalDateToServer(data.reminderDate1);
                    data.reminderDate2 = DateUtils.convertLocalDateToServer(data.reminderDate2);
                    data.reminderDate3 = DateUtils.convertLocalDateToServer(data.reminderDate3);
                    data.processingDateRecieved = DateUtils.convertLocalDateToServer(data.processingDateRecieved);
                    data.processingDateShipped = DateUtils.convertLocalDateToServer(data.processingDateShipped);
                    data.dueToClientReminder = DateUtils.convertLocalDateToServer(data.dueToClientReminder);
                    data.dueToMounterReminder = DateUtils.convertLocalDateToServer(data.dueToMounterReminder);
                    data.recievedFromMounterReminder = DateUtils.convertLocalDateToServer(data.recievedFromMounterReminder);
                    data.completionDate = DateUtils.convertLocalDateToServer(data.completionDate);
                    data.processingProofShipped = DateUtils.convertLocalDateToServer(data.processingProofShipped);
*/
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',

                transformRequest: function (data) {

                    data.requestDate = DateUtils.convertLocalDateToServer(data.requestDate);
                    data.reminderDate1 = DateUtils.convertLocalDateToServer(data.reminderDate1);
                    data.reminderDate2 = DateUtils.convertLocalDateToServer(data.reminderDate2);
                    data.reminderDate3 = DateUtils.convertLocalDateToServer(data.reminderDate3);
                    data.processingDateRecieved = DateUtils.convertLocalDateToServer(data.processingDateRecieved);
                    data.processingDateShipped = DateUtils.convertLocalDateToServer(data.processingDateShipped);
                    data.dueToClientReminder = DateUtils.convertLocalDateToServer(data.dueToClientReminder);
                    data.dueToMounterReminder = DateUtils.convertLocalDateToServer(data.dueToMounterReminder);
                    data.recievedFromMounterReminder = DateUtils.convertLocalDateToServer(data.recievedFromMounterReminder);
                    data.completionDate = DateUtils.convertLocalDateToServer(data.completionDate);
                    data.processingProofShipped = DateUtils.convertLocalDateToServer(data.processingProofShipped);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
