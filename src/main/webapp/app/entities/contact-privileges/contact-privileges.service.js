(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('ContactPrivileges', ContactPrivileges);

    ContactPrivileges.$inject = ['$resource', 'DateUtils'];

    function ContactPrivileges ($resource, DateUtils) {
        var resourceUrl =  'api/contact-privileges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastLoginDt = DateUtils.convertDateTimeFromServer(data.lastLoginDt);
                    data.lastLogoutDt = DateUtils.convertDateTimeFromServer(data.lastLogoutDt);
                    data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                    data.updatedDate = DateUtils.convertDateTimeFromServer(data.updatedDate);
                    data.expireDate = DateUtils.convertDateTimeFromServer(data.expireDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
