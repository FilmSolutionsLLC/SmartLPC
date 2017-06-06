(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Storage_Servers', Storage_Servers);

    Storage_Servers.$inject = ['$resource'];

    function Storage_Servers ($resource) {
        var resourceUrl =  'api/storage-servers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
