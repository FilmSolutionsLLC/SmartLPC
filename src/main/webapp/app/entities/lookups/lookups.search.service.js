(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('LookupsSearch', LookupsSearch);

    LookupsSearch.$inject = ['$resource'];

    function LookupsSearch($resource) {
        var resourceUrl =  'api/_search/lookups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
