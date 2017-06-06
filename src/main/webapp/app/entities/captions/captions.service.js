(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Captions', Captions);

    Captions.$inject = ['$resource', 'DateUtils'];

    function Captions ($resource, DateUtils) {
        var resourceUrl =  'api/captions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.captionDttm = DateUtils.convertDateTimeFromServer(data.captionDttm);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
