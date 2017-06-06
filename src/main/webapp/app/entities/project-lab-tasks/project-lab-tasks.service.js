(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('ProjectLabTasks', ProjectLabTasks);

    ProjectLabTasks.$inject = ['$resource', 'DateUtils'];

    function ProjectLabTasks ($resource, DateUtils) {
        var resourceUrl =  'api/project-lab-tasks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                    data.updatedDate = DateUtils.convertDateTimeFromServer(data.updatedDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
