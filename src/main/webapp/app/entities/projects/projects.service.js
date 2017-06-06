(function () {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Projects', Projects);

    Projects.$inject = ['$resource', 'DateUtils'];

    function Projects($resource, DateUtils) {
        var resourceUrl = 'api/projects/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.projects.startDate = DateUtils.convertLocalDateFromServer(data.projects.startDate);
                    data.projects.endDate = DateUtils.convertLocalDateFromServer(data.projects.endDate);
                    data.projects.createdDate = DateUtils.convertLocalDateFromServer(data.projects.createdDate);
                    data.projects.updatedDate = DateUtils.convertLocalDateFromServer(data.projects.updatedDate);
                    data.projects.shootDate = DateUtils.convertLocalDateFromServer(data.projects.shootDate);
                    data.projects.tagDate = DateUtils.convertLocalDateFromServer(data.projects.tagDate);
                    data.projects.reminderDate = DateUtils.convertLocalDateFromServer(data.projects.reminderDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                  /*  data.startDate = DateUtils.convertLocalDateToServer(data.startDate);
                    data.endDate = DateUtils.convertLocalDateToServer(data.endDate);
                    data.createdDate = DateUtils.convertLocalDateToServer(data.createdDate);
                    data.updatedDate = DateUtils.convertLocalDateToServer(data.updatedDate);
                    data.shootDate = DateUtils.convertLocalDateToServer(data.shootDate);
                    data.tagDate = DateUtils.convertLocalDateToServer(data.tagDate);
                    data.reminderDate = DateUtils.convertLocalDateToServer(data.reminderDate);*/
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    /*data.startDate = DateUtils.convertLocalDateToServer(data.startDate);
                     data.endDate = DateUtils.convertLocalDateToServer(data.endDate);
                     data.createdDate = DateUtils.convertLocalDateToServer(data.createdDate);
                     data.updatedDate = DateUtils.convertLocalDateToServer(data.updatedDate);
                     data.shootDate = DateUtils.convertLocalDateToServer(data.shootDate);
                     data.tagDate = DateUtils.convertLocalDateToServer(data.tagDate);
                     data.reminderDate = DateUtils.convertLocalDateToServer(data.reminderDate);*/
                    return angular.toJson(data);
                }
            }
        });
    }
})();
