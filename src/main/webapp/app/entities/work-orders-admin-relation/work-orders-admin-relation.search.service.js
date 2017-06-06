(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('WorkOrdersAdminRelationSearch', WorkOrdersAdminRelationSearch);

    WorkOrdersAdminRelationSearch.$inject = ['$resource'];

    function WorkOrdersAdminRelationSearch($resource) {
        var resourceUrl =  'api/_search/work-orders-admin-relations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
