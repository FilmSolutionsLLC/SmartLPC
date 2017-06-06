(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('WorkOrdersAdminRelation', WorkOrdersAdminRelation);

    WorkOrdersAdminRelation.$inject = ['$resource'];

    function WorkOrdersAdminRelation ($resource) {
        var resourceUrl =  'api/work-orders-admin-relations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
