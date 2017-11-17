(function () {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('ProjectsList', ProjectsList);

    ProjectsList.$inject = ['$resource', 'DateUtils'];

    function ProjectsList($resource) {
        var resourceUrl = 'api/pro/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true}
        });
    }
})();
