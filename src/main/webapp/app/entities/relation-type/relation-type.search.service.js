(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('RelationTypeSearch', RelationTypeSearch);

    RelationTypeSearch.$inject = ['$resource'];

    function RelationTypeSearch($resource) {
        var resourceUrl =  'api/_search/relation-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
