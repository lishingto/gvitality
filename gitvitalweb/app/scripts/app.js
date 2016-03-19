'use strict';

/**
 * @ngdoc overview
 * @name gitvitalwebApp
 * @description
 * # gitvitalwebApp
 *
 * Main module of the application.
 */
angular
    .module('gitvitalwebApp', [
    'ngAnimate',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ngTable',
    'ui.router',
    'angularSpinner',
    'angularMoment'
  ])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('main', {
                url: '/',
                templateUrl: 'views/main.html',
                controller: 'MainCtrl',
                controllerAs: 'main'
            })
            .state('data', {
                url: '/data',
                templateUrl: 'views/data.html',
                controller: 'DataCtrl',
                controllerAs: 'data'
            }).state('view', {
                url: '/view/:repoName',
                templateUrl: 'views/view.html',
                controller: 'ViewCtrl',
                controllerAs: 'view'
            }).state('classify', {
                url: '/classify',
                params: {list: []},
                templateUrl: 'views/classify.html',
                controller: 'ClassifyCtrl',
                controllerAs: 'classify'
            }).state('about', {
                url: '/about',
                templateUrl: 'views/about.html'
            });
        $urlRouterProvider.when('', '/');
    });