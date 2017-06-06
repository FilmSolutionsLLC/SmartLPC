(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('ProjectsSearch', ProjectsSearch);

    ProjectsSearch.$inject = ['$resource'];

    function ProjectsSearch($resource) {
        var resourceUrl =  'api/_search/projects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
