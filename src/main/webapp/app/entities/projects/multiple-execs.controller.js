/**
 * Created by macbookpro on 1/25/17.
 */
/**
 * Created by macbookpro on 12/27/16.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('MultiExecsController', MultiExecsController);
    MultiExecsController.$inject = ['sendID', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModalInstance', '$scope', '$state'];

    function MultiExecsController(sendID, $http, $rootScope, Contacts, Lookups, Departments, User, ContactsSearch, AlertService, $uibModalInstance, $scope, $state) {


        $rootScope.owner = null;
        var vm = this;


        $rootScope.multiExecs = [];

        vm.elementID = sendID;


        vm.loadAll = loadAll;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;

       // vm.url = "api/contacts?page=0";
        //vm.searchUrl = "api/_search/contacts?query=";
        console.log(" URL : " + vm.url);
        //vm.selectChecked = selectChecked;
        vm.addSelected = addSelected;
        vm.removeSelected = removeSelected;
        //vm.selectedContact = selectedContact;
        vm.selectContact = selectContact;

        console.log("data got form contact : " + vm.elementID);

        //vm.loadAll();



        vm.downloadTypeList = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "ALL"}, {'id': 2, 'value': "Lock Approved"}];
        vm.exclusivesList = [{'id': 0, 'value': "NONE"}, {'id': 1, 'value': "BASIC"}, {'id': 2, 'value': "MASTER"}];

        vm.downloadType = 0;
        vm.exclusives = 0;


        vm.page = 0;
        vm.url = "api/contacts?page=0&sort=fullName,asc";
        vm.searchUrl = "api/_search/contacts?query=";

        vm.selectedContact = selectedContact;
        vm.elementID = sendID;
        vm.order = "asc";
        vm.sortBy = "fullName"
        vm.loadAll();


        vm.setSortOrder = function (field) {
            console.log("Sorting by : " + field);
            if (field === vm.sortBy) {
                if (vm.order === 'asc') {
                    vm.order = 'desc';
                } else {
                    vm.order = "asc";
                }

            } else {
                vm.order;

            }
            vm.sortBy = field;


            vm.url = "api/contacts?page=0&sort=".concat(field).concat(",").concat(vm.order);
            console.log("URL : " + vm.url);
            vm.searchUrl = "api/_search/contacts?query=";
            vm.loadAll();

        };

        function loadAll() {

            vm.contactsDTO = [];
            if (vm.currentSearch) {

                $http({
                    method: 'GET',
                    url: vm.searchUrl
                }).then(function successCallback(response) {
                    vm.contactsDTO = response.data;
                    vm.totalItems = response.headers('X-Total-Count');
                }, function errorCallback(response) {

                });
            } else if(vm.filtered ){
                $http({
                    method: 'GET',
                    url: vm.url
                }).then(function successCallback(response) {
                    vm.contactsDTO = response.data;
                    vm.totalItems = vm.contactsDTO.length;
                }, function errorCallback(response) {

                });

            }else {
                $http({
                    method: 'GET',
                    url: vm.url
                }).then(function mySuccess(response) {
                    vm.contactsDTO = response.data;
                    vm.totalItems =  response.headers('X-Total-Count');
                    vm.queryCount = vm.totalItems;

                    console.log("Headers: "+JSON.stringify(response));


                    console.log("vm.totalItems: "+(vm.totalItems));
                    console.log("queryCount: "+(vm.queryCount));
                    console.log("vm.page: "+(vm.page));

                }, function myError(response) {

                });
            }

            function sort() {

                console.log("Inside this ");
                var result = [vm.predicate + ','
                + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {


                vm.links = ParseLinks.parse(headers('link'));

                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.contacts = data;
                vm.page = pagingParams.page;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function transition() {


            console.log("Page: ", vm.page-1);
            vm.url = "api/contacts?page=" + (vm.page-1) + ("&sort=") + (vm.sortBy) + (',') + (vm.order);
            console.log("transition url : " + vm.url);
            vm.loadAll();
        }

        function search(searchQuery) {

            if (!searchQuery) {
                return vm.clear();
            }
            vm.page = 0;
            vm.currentSearch = searchQuery;
            vm.searchUrl = "api/_search/contacts?query=" + vm.currentSearch;


            vm.transition();
        }

        function clear() {
            vm.page = 0;
            vm.currentSearch = null;
            vm.searchQuery = null;
            vm.transition();
        }

        $rootScope.relatedContacts = [];

        function selectedContact(contacts) {

            $scope.currentOBJ = {
                "elementID": vm.elementID,
                "data": contacts
            };

            $rootScope.relationships = $scope.currentOBJ;
            $uibModalInstance.dismiss('cancel');

        };


        vm.close = function () {

            $uibModalInstance.dismiss('cancel');
        };



        vm.selectedContacts = [];

        vm.execContact = [];

        function selectContact(contacts){
            if (vm.selectedContacts.indexOf(contacts) == -1) {
                vm.selectedContacts.push(contacts);
                console.log("Checking  :  " + vm.selectedCheckBox[contacts.id]);
                console.log("contact selected : " + contacts.id);
            } else {

                console.log("item already exists");
                vm.selectedContacts.splice(vm.selectedContacts.indexOf(contacts), 1);
            }
        };

        function removeSelected(index) {

            console.log("removing id : " + vm.selectedCheckBox[vm.selectedContacts[index].id]);
            vm.selectedCheckBox[vm.selectedContacts[index].id] = false;
            vm.selectedContacts.splice(index, 1);

        };

       function addSelected(){
            $rootScope.execsContactMultiple = [];
            console.log("total contacts selected  : " + vm.execContact.length);
            for (var i = 0; i < vm.selectedContacts.length; i++) {

                $rootScope.execsContactMultiple.push({
                    "contact": vm.selectedContacts[i],
                    "exec": true,
                    "downloadType": vm.downloadType,
                    "print": vm.print,
                    "email": vm.email,
                    "captioning": vm.captioning,
                    "talentManagement": vm.talentManagement,
                    "signoffManagement": vm.signoffManagement,
                    "releaseExclude": vm.releaseExclude,
                    "vendor": vm.vendor,
                    "lockApproveRestriction": vm.lockApproveRestriction,
                    "viewSensitive": vm.viewSensitive,
                    "exclusives": 0,
                    "seesUntagged": false,
                    "hasVideo": vm.hasVideo,
                    "disabled": vm.disabled,
                    "datgeditManagement": false,
                    "priorityPix": vm.priorityPix,
                    "readOnly": false,
                    "restartColumns": 2,
                    "restartImageSize": 'Large',
                    "restartImagesPerPage": 20,
                    "showFinalizations": false,
                    "watermark": false,
                    "internal": false,
                    "globalAlbum": false,
                    "loginCount": 0,
                    "defaultAlbum": null,
                    "critiqueIt": false,
                    "adhocLink": false,
                    "retouch": false,
                    "fileUpload": false,
                    "deleteAssets": false,
                    "watermarkInnerTransparency": 0.00,
                    "watermarkOuterTransparency": 0.00,
                    "restartRole": "EXEC"
                });
            }
            $uibModalInstance.dismiss('cancel');
        };

       vm.save = function () {

       };

       vm.filterQuery = '';

       vm.runFilter = function (type) {
           console.log("Search based on Project")
           vm.filtered  = true;
           vm.url = 'api/cp/projects/'+type+"/"+vm.filterQuery;
           vm.loadAll();
         /*;

           $http({
               method : 'GET',
               url : 'api/cp/projects/'+type+"/"+vm.filterQuery
           }).then(
               function successCallback(response) {

                   vm.contactsDTO = response.data;
                   console.log("Http GET called");

                   console.log("length : " + vm.totalItems);
               }, function errorCallback(response) {

               });*/
       };


        vm.addContact = function () {

            // var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/contacts/contacts-add-modal.html',
                controller: 'ContactsAddModalController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'xl',
                resolve: {
                    elementID: function () {
                        return vm.elementID;
                    },
                    entity: function () {
                        return {
                            // relatedContacts: null,
                            username : null,
                            password : null,
                            fullName : null,
                            title : '',
                            email : '',
                            email2 : '',
                            phoneOffice : '',
                            phoneAlternate : '',
                            phoneMobile : '',
                            phoneFax : '',
                            streetAddress : '',
                            streetAddress2 : '',
                            streetAddress3 : '',
                            city : '',
                            state : '',
                            zipcode : '',
                            country : '',
                            website : '',
                            notes : '',
                            sourceId : '',
                            createdDate : null,
                            updatedDate : null,
                            dashboard : false,
                            internalAccessOnly : false,
                            adhocExpiresIn : null,
                            adhocLimitViews : null,
                            adhocDownload : null,
                            adhocWatermarkText : null,
                            loginIp : null,
                            loginAttempt : 0,
                            attemptBasedLogin : true,
                            ipBasedLogin : false,
                            resetpassword : null,
                            companyContact : null,
                            createdByAdmin : null,
                            updatedByAdmin : null,
                            globalRestartColumns : 0,
                            globalRestartImagesPerPage : 0,
                            globalRestartImageSize : '',
                            globalRestartTime : null,
                            id : null
                        };
                    }/*,
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]*/
                }
                /*resolve: {
                    entity: ['ProjectRoles', function(ProjectRoles) {
                        return ProjectRoles.get({id : $stateParams.id}).$promise;
                    }]
                }*/
            });
        }
    }
})();
