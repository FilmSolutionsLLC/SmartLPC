(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('ContactRelationships', ContactRelationships);

    ContactRelationships.$inject = ['$resource', 'DateUtils'];

    function ContactRelationships ($resource, DateUtils) {
        var resourceUrl =  'api/contact-relationships/:id';

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
