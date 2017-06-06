(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Lookups', Lookups);

    Lookups.$inject = ['$resource'];

    function Lookups ($resource) {
        var resourceUrl =  'api/lookups/:id';

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
