(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('WorkOrderAbcHddSearch', WorkOrderAbcHddSearch);

    WorkOrderAbcHddSearch.$inject = ['$resource'];

    function WorkOrderAbcHddSearch($resource) {
        var resourceUrl =  'api/_search/work-order-abc-hdds/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
