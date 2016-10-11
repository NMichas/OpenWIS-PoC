/**
 * @ngdoc controller
 * @name search.controller.js
 * @description SearchController controller
 */
(function() {
	"use strict";

	angular // eslint-disable-line no-undef
		.module("BlurAdmin.pages.openwis")
		.controller("SearchController", SearchController);

	/** @ngInject */
	function SearchController($scope, $http) {
		var vm = this;

		/***********************************************************************
		 * Local variables.
		 **********************************************************************/
		//var bar;

		/***********************************************************************
		 * Exported variables.
		 **********************************************************************/
		vm.results = [];
		vm.term = "";
		vm.total = 0;

		/***********************************************************************
		 * Exported functions.
		 **********************************************************************/
		vm.search = search;

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
		function search(bar) {
			return $http.get("http://localhost:8181/api/openwis/search?t=" +  vm.term)
				.then(function (res) {
						vm.total = res.data.hits.total;
						vm.results = res.data.hits.hits.splice(1);
					}, function (res) {
						toastr.error("Could not search records.");
					}
				);
		}
	}
})();
