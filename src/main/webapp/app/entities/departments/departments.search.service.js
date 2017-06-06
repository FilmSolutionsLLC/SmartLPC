(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('DepartmentsSearch', DepartmentsSearch);

    DepartmentsSearch.$inject = ['$resource'];

    function DepartmentsSearch($resource) {
        var resourceUrl =  'api/_search/departments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
