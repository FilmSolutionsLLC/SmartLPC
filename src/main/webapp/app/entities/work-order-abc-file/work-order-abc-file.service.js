(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('WorkOrderAbcFile', WorkOrderAbcFile);

    WorkOrderAbcFile.$inject = ['$resource'];

    function WorkOrderAbcFile ($resource) {
        var resourceUrl =  'api/work-order-abc-files/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
