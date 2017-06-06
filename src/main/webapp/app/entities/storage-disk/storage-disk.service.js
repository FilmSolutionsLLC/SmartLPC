(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Storage_Disk', Storage_Disk);

    Storage_Disk.$inject = ['$resource', 'DateUtils'];

    function Storage_Disk ($resource, DateUtils) {
        var resourceUrl =  'api/storage-disks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastUpdated = DateUtils.convertLocalDateFromServer(data.lastUpdated);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.lastUpdated = DateUtils.convertLocalDateToServer(data.lastUpdated);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.lastUpdated = DateUtils.convertLocalDateToServer(data.lastUpdated);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
