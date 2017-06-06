(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('ProjectPurchaseOrdersSearch', ProjectPurchaseOrdersSearch);

    ProjectPurchaseOrdersSearch.$inject = ['$resource'];

    function ProjectPurchaseOrdersSearch($resource) {
        var resourceUrl =  'api/_search/project-purchase-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
