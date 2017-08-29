(function() {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$location', '$state', 'Auth', 'Principal', 'ENV', 'LoginService'];

    function NavbarController ($location, $state, Auth, Principal, ENV, LoginService) {
        var vm = this;

        vm.navCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.inProduction = ENV === 'prod';
        vm.login = login;
        vm.logout = logout;
        vm.$state = $state;


        function login () {
        	
            LoginService.open();
            
        }

        function logout () {
            Auth.logout();
            $state.go('home');
        }
        
        console.log("Navbar Controller")
//        vm.currentAccount = null;
//		Principal.identity().then(function(account) {
//			vm.currentAccount = account;
//
//			console.log("Current User : " + JSON.stringify(vm.currentAccount));
//		});
//		
//		
//		
//		$rootScope.$watch(function() {
//			return $rootScope.currentUserGlobal;
//		}, function() {
//			if ($rootScope.relationships == null) {
//				console.log("null rootscope");
//
//			} else {
//				console.log("not null");
//
//			}
//		});
    }
})();
