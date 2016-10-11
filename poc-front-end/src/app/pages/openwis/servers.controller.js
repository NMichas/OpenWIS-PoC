/**
 * @ngdoc controller
 * @name servers.controller.js
 * @description ServersController controller
 */
(function() {
	"use strict";

	angular // eslint-disable-line no-undef
		.module("BlurAdmin.pages.openwis")
		.controller("ServersController", ServersController);

	/** @ngInject */
	function ServersController($scope, toastr, $http, x2js) {
		var vm = this;

		/***********************************************************************
		 * Local variables.
		 **********************************************************************/
		//var showAdd;

		/***********************************************************************
		 * Exported variables.
		 **********************************************************************/
		vm.showAdd = false;
		vm.newServer = {};
		vm.serverIdentity;
		vm.serverSets;
		vm.servers;

		/***********************************************************************
		 * Exported functions.
		 **********************************************************************/
		vm.harvest = harvest;
		vm.addServer = addServer;
		vm.identifyServer = identifyServer;

		// Activating the controller.
		activate();

		/***********************************************************************
		 * Controller activation.
		 **********************************************************************/
		function activate() {
			getServers();
		}

		/***********************************************************************
		 * $scope destroy.
		 **********************************************************************/
		$scope.$on("$destroy", function() {
		});

		/***********************************************************************
		 * Functions.
		 **********************************************************************/
		function harvest() {
            toastr.success("Harvesting started.");
            console.log("HARVEST");
		}

		function addServer() {
			$http.post("http://localhost:8181/api/openwis/add-server",
				vm.newServer).then(function (res) {
					toastr.success("Server " + vm.newServer.servername + " added successfully.");
					vm.newServer = {};
					vm.serverIdentity = {};
					vm.showAdd = false;
					return getServers();
				}, function (res) {
					toastr.error("Could not add server " + vm.newServer.servername + ".");
				}
			);
		}

		function identifyServer() {
			if (!vm.newServer.url) {
				return;
			} else {
				$http.get("http://localhost:8181/api/openwis/server-identify?url=" + encodeURI(vm.newServer.url))
					.then(function (res) {
						vm.serverIdentity = x2js.xml_str2json(res.data);
						console.log("IDENTITY=", vm.serverIdentity);
					}, function (res) {
						toastr.error("Could not identify server " + vm.newServer.servername + ".");
					}
				);
				$http.get("http://localhost:8181/api/openwis/server-list-sets?url=" + encodeURI(vm.newServer.url))
					.then(function (res) {
							vm.serverSets = x2js.xml_str2json(res.data);
							console.log("SETS=", vm.serverSets);
						}, function (res) {
							toastr.error("Could not get sets for server " + vm.newServer.servername + ".");
						}
					);
			}
		}

		function getServers() {
			return $http.get("http://localhost:8181/api/openwis/servers-list")
				.then(function (res) {
						vm.servers = res.data;
					}, function (res) {
						toastr.error("Could not get servers list.");
					}
				);
		}

		function harvest(serverID) {
			$http.get("http://localhost:8181/api/openwis/harvest?id=" + serverID)
				.then(function (res) {

						toastr.success("Harvesting started.");
					}, function (res) {
					console.log(res);
						toastr.error("Could not start harvesting.");
					}
				);
		}
	}
})();
