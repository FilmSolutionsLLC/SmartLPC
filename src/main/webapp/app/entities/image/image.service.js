(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Image', Image);

    Image.$inject = ['$resource', 'DateUtils'];

    function Image ($resource, DateUtils) {
        var resourceUrl =  'api/images/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {

                method: 'GET',
                transformResponse: function (data) {
                    console.log("ImageService : get");
                    data = angular.fromJson(data);
                    data.releaseTime = DateUtils.convertDateTimeFromServer(data.releaseTime);
                    data.ingestTime = DateUtils.convertDateTimeFromServer(data.ingestTime);
                    data.quickpickSelectedTime = DateUtils.convertDateTimeFromServer(data.quickpickSelectedTime);
                    data.createdTime = DateUtils.convertDateTimeFromServer(data.createdTime);
                    data.updatedTime = DateUtils.convertDateTimeFromServer(data.updatedTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
