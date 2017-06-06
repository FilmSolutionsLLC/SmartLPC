(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('Storage_ServersSearch', Storage_ServersSearch);

    Storage_ServersSearch.$inject = ['$resource'];

    function Storage_ServersSearch($resource) {
        var resourceUrl =  'api/_search/storage-servers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
