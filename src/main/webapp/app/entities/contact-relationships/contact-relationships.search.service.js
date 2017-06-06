(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('ContactRelationshipsSearch', ContactRelationshipsSearch);

    ContactRelationshipsSearch.$inject = ['$resource'];

    function ContactRelationshipsSearch($resource) {
        var resourceUrl =  'api/_search/contact-relationships/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
