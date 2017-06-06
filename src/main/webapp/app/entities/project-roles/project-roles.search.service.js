(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('ProjectRolesSearch', ProjectRolesSearch);

    ProjectRolesSearch.$inject = ['$resource'];

    function ProjectRolesSearch($resource) {
        var resourceUrl =  'api/_search/project-roles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
