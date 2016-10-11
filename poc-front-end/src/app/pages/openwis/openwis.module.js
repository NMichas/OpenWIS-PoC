/**
 * @author k.danovsky
 * created on 12.01.2016
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.openwis', [
      'cb.x2js', 'infinite-scroll'
  ])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('openwis-servers', {
          url: '/openwis-servers',
          templateUrl : 'app/pages/openwis/servers.html',
          controller: 'ServersController',
          controllerAs: 'serversCtrl' ,
          title: 'Servers',
          sidebarMeta: {
            icon: 'ion-soup-can-outline',
            order: 200,
          }
        })
        .state('openwis-browse', {
          url: '/openwis-browse',
          templateUrl : 'app/pages/openwis/browse.html',
          controller: 'BrowseController',
          controllerAs: 'browseCtrl' ,
          title: 'Browse',
          sidebarMeta: {
            icon: 'ion-earth',
            order: 200,
          }
        })
        .state('openwis-search', {
          url: '/openwis-search',
          templateUrl : 'app/pages/openwis/search.html',
          controller: 'SearchController',
          controllerAs: 'searchCtrl' ,
          title: 'Search',
          sidebarMeta: {
            icon: 'ion-search',
            order: 200,
          }
        })
  }

})();
