(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('ProjectPermission', ProjectPermission);

    ProjectPermission.$inject = ['ContactPrivileges', '$rootScope', '$uibModal', '$scope', '$state', 'Contacts', 'ContactsSearch', 'AlertService'];

    function ProjectPermission(ContactPrivileges, $rootScope, $uibModal, $scope, $state, Contacts, ContactsSearch, AlertService) {
        var vm = this;

        vm.close = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.openModalProject = function () {

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/contacts/projectsList.html',
                controller: 'ProjectListController',
                size: 'xl',
                scope: $scope,
                controllerAs: 'vm'
            });
        };

        $rootScope.selectedProjects = null;
        vm.selectedProjects = [];
        $rootScope.$watch(function () {
            return $rootScope.selectedProjects;
        }, function () {
            if ($rootScope.selectedProjects == null) {
                console.log("null rootscope selectedProjects");

            } else {
                console.log("selectedProjects  contains few projects");
                vm.selectedProjects = $rootScope.selectedProjects;
                console.log(JSON.stringify(vm.selectedProjects));
            }
        });

        vm.removeSelected = function (index) {

            vm.selectedProjects.splice(index, 1);

        };

        vm.save = function () {
            vm.isSaving = true;
            for (var i = 0; i < vm.selectedProjects.length; i++) {
                ContactPrivileges.save(vm.selectedProjects[i], onSaveSuccess, onSaveError);
            }
        };

        var onSaveSuccess = function (result) {
            //$scope.$emit('smartLpcApp:contactPrivilegesUpdate', result);
            $uibModalInstance.close(result);
            // console.log("saved in db: " + result.data.id);
            vm.isSaving = false;
            alert("Contact Privileges updated for Contact : " + vm.selectedProjects.contact.fullName );
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };
        /* vm.execss.push({
         "exec": false,
         "downloadType": 0,
         "print": false,
         "email": false,
         "captioning": false,
         "talentManagement": false,
         "signoffManagement": false,
         "releaseExclude": false,
         "vendor": false,
         "lockApproveRestriction": false,
         "viewSensitive": false,
         "exclusives": false,
         "seesUntagged": false,
         "hasVideo": false,
         "disabled": false,
         "datgeditManagement": false,
         "priorityPix": false,
         "readOnly": false,
         "restartColumns": 2,
         "restartImageSize": 'Large',
         "restartImagesPerPage": 20,
         "showFinalizations": false,
         "watermark": false,
         "internal": false

         });*/
    }
})();
