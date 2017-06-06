(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('ProjectRoles', ProjectRoles);

    ProjectRoles.$inject = ['$resource', 'DateUtils'];

    function ProjectRoles ($resource, DateUtils) {
        var resourceUrl =  'api/project-roles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.miniFullDt = DateUtils.convertLocalDateFromServer(data.miniFullDt);
                    data.fullFinalDt = DateUtils.convertLocalDateFromServer(data.fullFinalDt);
                    data.startDate = DateUtils.convertLocalDateFromServer(data.startDate);
                    data.expireDate = DateUtils.convertLocalDateFromServer(data.expireDate);
                    data.createdDate = DateUtils.convertLocalDateFromServer(data.createdDate);
                    data.updatedDate = DateUtils.convertLocalDateFromServer(data.updatedDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.miniFullDt = DateUtils.convertLocalDateToServer(data.miniFullDt);
                    data.fullFinalDt = DateUtils.convertLocalDateToServer(data.fullFinalDt);
                    data.startDate = DateUtils.convertLocalDateToServer(data.startDate);
                    data.expireDate = DateUtils.convertLocalDateToServer(data.expireDate);
                    data.createdDate = DateUtils.convertLocalDateToServer(data.createdDate);
                    data.updatedDate = DateUtils.convertLocalDateToServer(data.updatedDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.miniFullDt = DateUtils.convertLocalDateToServer(data.miniFullDt);
                    data.fullFinalDt = DateUtils.convertLocalDateToServer(data.fullFinalDt);
                    data.startDate = DateUtils.convertLocalDateToServer(data.startDate);
                    data.expireDate = DateUtils.convertLocalDateToServer(data.expireDate);
                    data.createdDate = DateUtils.convertLocalDateToServer(data.createdDate);
                    data.updatedDate = DateUtils.convertLocalDateToServer(data.updatedDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
