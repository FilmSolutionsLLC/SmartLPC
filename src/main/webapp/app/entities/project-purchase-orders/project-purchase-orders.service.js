(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('ProjectPurchaseOrders', ProjectPurchaseOrders);

    ProjectPurchaseOrders.$inject = ['$resource', 'DateUtils'];

    function ProjectPurchaseOrders ($resource, DateUtils) {
        var resourceUrl =  'api/project-purchase-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created_date = DateUtils.convertDateTimeFromServer(data.created_date);
                    data.updated_date = DateUtils.convertDateTimeFromServer(data.updated_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
