(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('WorkOrderSearch', WorkOrderSearch);

    WorkOrderSearch.$inject = ['$resource'];

    function WorkOrderSearch($resource) {
        var resourceUrl =  'api/_search/work-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
