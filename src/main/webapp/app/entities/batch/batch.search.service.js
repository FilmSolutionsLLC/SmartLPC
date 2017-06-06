(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('BatchSearch', BatchSearch);

    BatchSearch.$inject = ['$resource'];

    function BatchSearch($resource) {
        var resourceUrl =  'api/_search/batches/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
