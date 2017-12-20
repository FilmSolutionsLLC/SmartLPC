(function() {
	'use strict';

	angular.module('smartLpcApp').controller('ProjectInfoRouter',
			ProjectInfoRouter);

	ProjectInfoRouter.$inject = [ '$translate', '$translatePartialLoader',
			'$http', 'Principal', '$scope', '$state' ];

	function ProjectInfoRouter($translate, $translatePartialLoader, $http,
			Principal, $scope, $state) {
		var vm = this;
		console.log("ProjectInfoRouter Controller");

		vm.reports = [];
		$http({
			method : 'GET',
			url : 'api/project-info/admin'
		}).then(function successCallback(response) {
			console.log("original length :" + response.data.length)
			vm.reports = response.data;
			console.log(JSON.stringify(vm.reports));

			if (vm.reports.length > 1) {
				console.log("multiple reports here :" + vm.reports.length);


                console.log("reports include : "+JSON.stringify(vm.reports));
                $state.go('multiple-workorder-reports',{user:vm.reports});

            } else {
				console.log("single report here : " + vm.reports.length)
				var reportType = vm.reports[0].report_id;
				console.log("reportType : " + reportType);
				if (angular.equals(reportType, 1)) {
					$state.go('projects');
				} else if (angular.equals(reportType, 4)) {
					$state.go('work-order-processing-log');
				} else if (angular.equals(reportType, 6)) {
					$state.go('work-order-audit');
				} else if (angular.equals(reportType, 7)) {
					$state.go('work-order-invoice');
				} else if (angular.equals(reportType, 9)) {
					$state.go('work-order-my-open');
				} else {
					$state.go('work-order');
				}
			}
		}, function errorCallback(response) {

		});

		vm.currentAccount = null;
		Principal.identity().then(
				function(account) {
					vm.currentAccount = account;

					console.log("Current User : "
							+ JSON.stringify(vm.currentAccount));
					if (angular.equals(vm.currentAccount.fullName,
							"Kyle Cummins")) {
						$state.go('projects');
					} else if (angular.equals(vm.currentAccount.fullName,
							"Rohan Mhatre")) {
						$state.go('work-order-audit');
					} else {
						$state.go('work-order');
					}
				});

	}

})();
