(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('Storage_DiskSearch', Storage_DiskSearch);

    Storage_DiskSearch.$inject = ['$resource'];

    function Storage_DiskSearch($resource) {
        var resourceUrl =  'api/_search/storage-disks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
