(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('ContactPrivilegesSearch', ContactPrivilegesSearch);

    ContactPrivilegesSearch.$inject = ['$resource'];

    function ContactPrivilegesSearch($resource) {
        var resourceUrl =  'api/_search/contact-privileges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
