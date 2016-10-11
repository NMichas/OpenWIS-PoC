/**
 * @ngdoc controller
 * @name browse.controller.js
 * @description BrowseController controller
 */
(function() {
	"use strict";

	angular // eslint-disable-line no-undef
		.module("BlurAdmin.pages.openwis")
		.controller("BrowseController", BrowseController);

	/** @ngInject */
	function BrowseController($scope, $http) {
		var vm = this;

		/***********************************************************************
		 * Local variables.
		 **********************************************************************/
		var currentPage = 0;
		var pageSize = 6;

		/***********************************************************************
		 * Exported variables.
		 **********************************************************************/
		vm.results = [];
		vm.total;
		vm.isFetching = false;

		/***********************************************************************
		 * Exported functions.
		 **********************************************************************/
		vm.fetchNext = fetchNext;

		// Activating the controller.
		activate();

		/***********************************************************************
		 * Controller activation.
		 **********************************************************************/
		function activate() {
		}

		/***********************************************************************
		 * $scope destroy.
		 **********************************************************************/
		$scope.$on("$destroy", function() {
		});

		/***********************************************************************
		 * Functions.
		 **********************************************************************/
		function fetchNext(bar) {
			vm.isFetching = true;
			currentPage++;
			return $http.get("http://localhost:8181/api/openwis/browse?p=" +  currentPage + "&s=" + pageSize)
				.then(function (res) {
						vm.total = res.data.hits.total;
						vm.results = vm.results.concat(res.data.hits.hits);
						vm.isFetching = false;
					}, function (res) {
						toastr.error("Could not browse records.");
						vm.isFetching = false;
					}
				);
		}
	}
})();
