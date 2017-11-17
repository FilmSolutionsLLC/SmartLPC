/**
 * Created by macbookpro on 1/12/17.
 */
/**
 * Created by macbookpro on 1/12/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectTemplateController', ProjectTemplateController);


    ProjectTemplateController.$inject = ['$timeout', '$http', 'AlertService', '$uibModalInstance', '$uibModal', '$rootScope', '$state', '$scope', '$stateParams', 'Projects', 'Contacts'];

    function ProjectTemplateController($timeout, $http, AlertService, $uibModalInstance, $uibModal, $rootScope, $state, $scope, $stateParams, Projects, Contacts) {
        var vm = this;
        vm.$state = $state;

        vm.selected = undefined;
        vm.projects = [];
        vm.getTemplate = getTemplate;

        /* Projects.query({
         size: 100000
         }, onSuccess, onError);

         function onSuccess(data, headers) {
         vm.projects = data;

         }

         function onError(error) {
         AlertService.error(error.data.message);
         }
         */
        $http({
            method: 'GET',
            url: 'api/template/projects'
        }).then(function (response) {
            console.log("total projects : " + response.data.length);
            vm.temp = response.data;
            console.log("first project " + JSON.stringify(vm.temp[0][1]));
            console.log("first id " + vm.temp[0][0]);
            for (var i = 0; i < vm.temp.length; i++) {
                $scope.tempObj = {"id": vm.temp[i][0], "name": vm.temp[i][1]}
                vm.projects.push($scope.tempObj);
            }
        });

        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };


        $scope.$watch('vm.selected', function () {
            if (angular.equals(vm.selected, undefined)) {
                console.log("not changed..");
            } else if (angular.equals(vm.selected, null)) {
                console.log("changed to NULL")
            } else {
                console.log("has changed " + JSON.stringify(vm.selected));
                // get ID and change state to project.add with all these values of this particular project
                //  alert("PROJECT SELECTED : " + JSON.stringify(vm.selected));
                console.log("id : " + vm.selected.id);
                vm.id = vm.selected.id;
                $rootScope.isTemplate = true;
                $timeout(function () {
                    $state.go('projects.template-add', {id: vm.id}, {reload: true});
                }, 0);

                //$state.go('projects.edit', {id: vm.selected.id}, {reload: true});
                //$state.go('contacts');

                //  console.log("state : " + ($state.go('projects.edit', {'id': vm.selected.id})));
                $uibModalInstance.dismiss('cancel');
                //getTemplate(vm.selected.id);
            }
        });
        function getTemplate(id) {
            console.log("getTemplate executed");
            $state.go('contacts', {}, {reload: true});// use for redirecting ...
        };
    }
})();
