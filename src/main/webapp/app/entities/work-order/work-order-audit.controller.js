/**
 * Created by macbookpro on 2/16/17.
 */
(function() {
	'use strict';

	angular.module('smartLpcApp').controller('WorkOrderAuditController',
			WorkOrderAuditController);

	WorkOrderAuditController.$inject = ['$http', '$scope',
			'$state' ];

	function WorkOrderAuditController( $http, $scope, $state) {

		var vm = this;


		vm.workOrders = [];
		$http({
			method : 'GET',
			url : 'api/reports/work-orders',
			params : {
				reportType : 'to_audit'
			}
		}).then(function(response) {
			vm.workOrders = response.data;
			console.log("total open workOrders : " + vm.workOrders.length);
			console.log(JSON.stringify(vm.workOrders));
			$scope.totalItems = vm.workOrders.length;

		});

		$scope.viewby = 10;

		$scope.currentPage = 1;
		$scope.itemsPerPage = $scope.viewby;
		$scope.maxSize = 5; // Number of pager buttons to show

		console.log("total items : " + $scope.totalItems);
		$scope.setPage = function(pageNo) {
			$scope.currentPage = pageNo;
		};

		$scope.pageChanged = function() {
			console.log('Page changed to: ' + $scope.currentPage);
		};

		$scope.setItemsPerPage = function(num) {
			$scope.itemsPerPage = num;
			$scope.currentPage = 1; // reset to first paghe
		};

		/*$http({
			method : 'GET',
			url : 'api/grouped/work-orders'
		}).then(function(response) {
			vm.groupedWorkOrders = response.data;
			// console.log("GroupWorkOrders : " +
			// JSON.stringify(vm.groupedWorkOrders));
		});*/

		/*$http({
			url : "api/_search/filters/work-orders",
			method : "GET",
			params : {
				query : 'PO 7654',
				filter : 'description'
			}
		}).then(function(response) {
			vm.wo = response.data;
			// console.log("WorkOrders : " + JSON.stringify(vm.wo));
		});*/

	}
})();
