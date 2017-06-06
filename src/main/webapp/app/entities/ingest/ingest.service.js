(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Ingest', Ingest);

    Ingest.$inject = ['$resource', 'DateUtils'];

    function Ingest ($resource, DateUtils) {
        var resourceUrl =  'api/ingestss/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.ingestStartTime = DateUtils.convertDateTimeFromServer(data.ingestStartTime);
                    data.ingestCompletedTime = DateUtils.convertDateTimeFromServer(data.ingestCompletedTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
