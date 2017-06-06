(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('ProjectLabTasksSearch', ProjectLabTasksSearch);

    ProjectLabTasksSearch.$inject = ['$resource'];

    function ProjectLabTasksSearch($resource) {
        var resourceUrl =  'api/_search/project-lab-tasks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
