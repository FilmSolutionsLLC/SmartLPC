/**
 * Created by macbookpro on 12/27/16.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('RenameController', RenameController);
    RenameController.$inject = ['project', '$http', '$rootScope', 'Contacts', 'Lookups', 'Departments', 'User', 'ContactsSearch', 'AlertService', '$uibModal', '$scope', '$state', 'Projects','$uibModalInstance'];

	    function RenameController(project, $http, $rootScope, Contacts, Lookups,
			Departments, User, ContactsSearch, AlertService, $uibModal, $scope,
			$state, Projects, $uibModalInstance) {

		var vm = this;

		console.log(JSON.stringify(project));

		vm.proj = project;

		vm.close = function() {

			$uibModalInstance.dismiss('cancel');
		};
		$scope.title1 = "";
		$scope.title2 = "";

		vm.rename = function() {
			$http({
				method : 'GET',
				url : 'api/rename',
				params : {
					id : vm.proj.id,
					alfrescoTitle1 : $scope.title1,
					alfrescoTitle2 : $scope.title2
				}
			}).then(function successCallback(response) {
				vm.tagss = response.data;
				console.log("TAGS : ");
				console.log(JSON.stringify(vm.tagss));
				$scope.totalItems = vm.tagss.length;
			}, function errorCallback(response) {

			});
		}
	}
})();
