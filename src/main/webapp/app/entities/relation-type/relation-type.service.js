(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('RelationType', RelationType);

    RelationType.$inject = ['$resource'];

    function RelationType ($resource) {
        var resourceUrl =  'api/relation-types/:id';

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
