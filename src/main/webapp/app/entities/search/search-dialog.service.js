/**
 * Created by macbookpro on 12/14/16.
 */
(function() {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Search', Search);

    Search.$inject = ['$resource'];

    function Search ($resource) {
        var resourceUrl =  'api/searchbar/search';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {

                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {

                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    console.log("SEARCH......");
                    console.log(angular.toJson(data).toString())
                    return angular.toJson(data);
                }
            }
        });
    }
})();
