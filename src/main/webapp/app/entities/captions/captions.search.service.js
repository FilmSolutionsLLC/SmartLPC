(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .factory('CaptionsSearch', CaptionsSearch);

    CaptionsSearch.$inject = ['$resource'];

    function CaptionsSearch($resource) {
        var resourceUrl =  'api/_search/captions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
