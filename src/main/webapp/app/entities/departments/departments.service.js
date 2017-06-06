(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Departments', Departments);

    Departments.$inject = ['$resource', 'DateUtils'];

    function Departments ($resource, DateUtils) {
        var resourceUrl =  'api/departments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdDate = DateUtils.convertLocalDateFromServer(data.createdDate);
                    data.updatedDate = DateUtils.convertLocalDateFromServer(data.updatedDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.createdDate = DateUtils.convertLocalDateToServer(data.createdDate);
                    data.updatedDate = DateUtils.convertLocalDateToServer(data.updatedDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.createdDate = DateUtils.convertLocalDateToServer(data.createdDate);
                    data.updatedDate = DateUtils.convertLocalDateToServer(data.updatedDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
