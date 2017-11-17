/**
 * Created by macbookpro on 12/27/16.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('RenameController', RenameController);
    RenameController.$inject = ['project', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModal', '$scope', '$state', 'Projects', '$uibModalInstance'];

    function RenameController(project, $http, $rootScope, Contacts, Lookups,
                              Departments, User, ContactsSearch, AlertService, $uibModal, $scope,
                              $state, Projects, $uibModalInstance) {

        var vm = this;

        console.log(JSON.stringify(project));

        vm.proj = project;

        vm.close = function () {

            $uibModalInstance.dismiss('cancel');
        };
        $scope.title1 = "";
        $scope.title2 = "";

        vm.rename = function () {
            $http({
                method: 'GET',
                url: 'api/rename',
                params: {
                    id: vm.proj.id,
                    alfrescoTitle1: $scope.title1,
                    alfrescoTitle2: $scope.title2
                }
            }).then(function successCallback(response) {
                alert("Project was Updated with AlfrescoTitle1: "+ $scope.title1 + " and AlfrescoTitle2: "+ $scope.title2);
                $state.go("projects-detail",{id:vm.proj.id},{reload:true});
            }, function errorCallback(response) {

            });
        };

        $scope.flag = false;
        vm.validate = function () {
            $scope.temp1 = $scope.title1.split(" ");
            $scope.temp2 = $scope.title2.split(" ");
            console.log("String split1 : "+$scope.temp1.length);
            console.log("String split2 : "+$scope.temp2.length);
            if ($scope.temp1.length > 1 || $scope.temp2.length > 1) {
                $scope.flag = true;
            } else {
                $scope.flag = false;
            }
        };
    }
})();
