(function () {
    'use strict';
    angular
        .module('smartLpcApp')
        .factory('Contacts', Contacts);

    Contacts.$inject = ['$resource', 'DateUtils'];

    function Contacts($resource, DateUtils) {

        var resourceUrl = 'api/contacts/:id';
        
        return $resource(resourceUrl, {}, {

            'query': {
                method: 'GET', isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    console.log("Contact.Service : GET called for single contact");
                    data = angular.fromJson(data);
                    //data.createdDate = DateUtils.convertLocalDateFromServer(data.createdDate);
                    //data.updatedDate = DateUtils.convertLocalDateFromServer(data.updatedDate);
                    //console.log("IN SERVICE :" + JSON.stringify(data));
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {

                    console.log("Contact.Service : PUT called for single contact");
                    // data.contacts.createdDate = DateUtils.convertLocalDateToServer(data.contacts.createdDate);
                    //data.contacts.updatedDate = DateUtils.convertLocalDateToServer(data.contacts.updatedDate);
                    return angular.toJson(data);
                }
            },
            'save': {

                method: 'POST',
                transformRequest: function (data) {
                    console.log("Contact.Service : POST called for single contact");
                    //data.createdDate = DateUtils.convertLocalDateToServer(data.createdDate);
                    //data.updatedDate = DateUtils.convertLocalDateToServer(data.updatedDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
