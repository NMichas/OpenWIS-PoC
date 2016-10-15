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
	function SearchController($scope, $http, $location) {
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
		function getServer() {
			if ($location.port()) {
				return $location.protocol() + '://'+ $location.host() +':'+  $location.port();
			} else {
				return $location.protocol() + '://'+ $location.host();
			}
		}

		function search(bar) {
			console.debug("Searching for :" + vm.term);
			return $http.get(getServer() + "/api/openwis/search?t=" +  vm.term)
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
