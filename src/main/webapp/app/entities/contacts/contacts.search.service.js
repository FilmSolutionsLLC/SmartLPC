(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('ContactsSearch', ContactsSearch);

    ContactsSearch.$inject = ['$resource'];

    function ContactsSearch($resource) {
        console.log("ContactsSearch");
        var resourceUrl =  'api/_search/contacts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
