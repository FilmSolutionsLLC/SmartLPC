(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('WorkOrderAbcHdd', WorkOrderAbcHdd);

    WorkOrderAbcHdd.$inject = ['$resource'];

    function WorkOrderAbcHdd ($resource) {
        var resourceUrl =  'api/work-order-abc-hdds/:id';

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
