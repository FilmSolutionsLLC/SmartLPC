(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('IngestSearch', IngestSearch);

    IngestSearch.$inject = ['$resource'];

    function IngestSearch($resource) {
        var resourceUrl =  'api/_search/ingestss/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
