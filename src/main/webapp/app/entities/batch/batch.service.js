(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Batch', Batch);

    Batch.$inject = ['$resource', 'DateUtils'];

    function Batch ($resource, DateUtils) {
        var resourceUrl =  'api/batches/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdTime = DateUtils.convertLocalDateFromServer(data.createdTime);
                    data.updatedTime = DateUtils.convertLocalDateFromServer(data.updatedTime);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.createdTime = DateUtils.convertLocalDateToServer(data.createdTime);
                    data.updatedTime = DateUtils.convertLocalDateToServer(data.updatedTime);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.createdTime = DateUtils.convertLocalDateToServer(data.createdTime);
                    data.updatedTime = DateUtils.convertLocalDateToServer(data.updatedTime);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
