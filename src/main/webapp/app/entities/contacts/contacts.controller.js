(function() {
	'use strict';

	angular.module('smartLpcApp').controller('ContactsController',
			ContactsController);

	ContactsController.$inject = [ '$rootScope', '$scope', '$state',
			'Contacts', 'ContactsSearch', 'ParseLinks', 'AlertService',
			'pagingParams', 'paginationConstants' ];

	function ContactsController($rootScope, $scope, $state, Contacts,
			ContactsSearch, ParseLinks, AlertService, pagingParams,
			paginationConstants) {
		console.log("ContactsController - Smart LPC");
		var vm = this;
		vm.loadAll = loadAll;
		vm.loadPage = loadPage;
		vm.predicate = pagingParams.predicate;
		vm.reverse = pagingParams.ascending;
		vm.transition = transition;
		vm.clear = clear;
		vm.search = search;
		vm.searchQuery = pagingParams.search;
		vm.currentSearch = pagingParams.search;
		vm.loadAll();
		console.log("No of items per page : "
				+ paginationConstants.itemsPerPage);
		function loadAll() {
			if (pagingParams.search) {
				console.log("contacts SEARCH called with query"
						+ pagingParams.search);
				console.log("pagingParams.page - 1 = " + pagingParams.page - 1)
				
				ContactsSearch.query({
					query : pagingParams.search,
					page : pagingParams.page - 1,
					size : paginationConstants.itemsPerPage,
					sort : sort()
				}, onSuccess, onError);
			} else {
				Contacts.query({
					page : pagingParams.page - 1,
					size : paginationConstants.itemsPerPage,
					sort : sort()
				}, onSuccess, onError);
			}
			function sort() {

				var result = [ vm.predicate + ','
						+ (vm.reverse ? 'asc' : 'desc') ];
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

		function loadPage(page) {
			console.log("page load called..");
			vm.page = page;
			vm.transition();
		}

		function transition() {
			console.log("changing page now");
			$state.transitionTo($state.$current, {
				page : vm.page,
				sort : vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
				search : vm.currentSearch
			});
		}

		function search(searchQuery) {
			console.log("search query called..");
			if (!searchQuery) {
				return vm.clear();
			}
			vm.links = null;
			vm.page = 1;
			vm.predicate = '_score';
			vm.reverse = false;
			vm.currentSearch = searchQuery;
			vm.transition();
		}

		function clear() {
			vm.links = null;
			vm.page = 1;
			vm.predicate = 'id';
			vm.reverse = true;
			vm.currentSearch = null;
			vm.transition();
		}

		/*
		 * var options = []; $( '.dropdown-menu a' ).on( 'click', function(
		 * event ) {
		 * 
		 * var $target = $( event.currentTarget ), val = $target.attr(
		 * 'data-value' ), $inp = $target.find( 'input' ), idx;
		 * 
		 * if ( ( idx = options.indexOf( val ) ) > -1 ) { options.splice( idx, 1 );
		 * setTimeout( function() { $inp.prop( 'checked', false ) }, 0); } else {
		 * options.push( val ); setTimeout( function() { $inp.prop( 'checked',
		 * true ) }, 0); } $( event.target ).blur();
		 * 
		 * console.log( options ); return false; });
		 */

		/*
		 * if (!('Notification' in window)) { console.log('Web Notification not
		 * supported'); return; }
		 * 
		 * Notification.requestPermission(function(permission) { var
		 * notification = new Notification("Title", { body : 'HTML5 Web
		 * Notification API', icon :
		 * 'http://filmsolutions.com/wp-content/uploads/1Logo.png', dir : 'auto'
		 * }); setTimeout(function() { notification.close(); }, 30000); });
		 */

	}
})();
