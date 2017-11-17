(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$rootScope','$scope', 'Principal', 'LoginService'];

    function HomeController ($rootScope,$scope, Principal, LoginService) {
        console.log("Home Controller");
    	var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {

            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;

                vm.currentAccount = null;
        		Principal.identity().then(function(account) {
        			vm.currentAccount = account;

        			$rootScope.currentUserGlobal = vm.currentAccount.fullName;

        		});


            });
        }

    }
})();
