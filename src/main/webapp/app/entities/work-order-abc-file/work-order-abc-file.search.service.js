(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('WorkOrderAbcFileSearch', WorkOrderAbcFileSearch);

    WorkOrderAbcFileSearch.$inject = ['$resource'];

    function WorkOrderAbcFileSearch($resource) {
        var resourceUrl =  'api/_search/work-order-abc-files/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
