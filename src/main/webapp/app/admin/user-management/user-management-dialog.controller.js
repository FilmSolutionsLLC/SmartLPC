(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('UserManagementDialogController', UserManagementDialogController);

    UserManagementDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'User', 'JhiLanguageService'];

    function UserManagementDialogController($stateParams, $uibModalInstance, entity, User, JhiLanguageService) {
        var vm = this;


        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_BASIC', 'ROLE_LAB', 'ROLE_SALE', 'ROLE_TAGGER'];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.user = entity;


        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function save() {
            vm.isSaving = true;
            if (vm.user.id !== null) {

                vm.user.fullName = vm.user.firstName.concat(" ").concat(vm.user.lastName);

                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                vm.user.fullName = vm.user.firstName.concat(" ").concat(vm.user.lastName);
                //console.log(vm.user.firstName.concat(" ").concat(vm.user.lastName));
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
    }
})();
