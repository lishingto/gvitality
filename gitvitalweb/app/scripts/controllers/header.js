'use strict';

/**
 * @ngdoc function
 * @name gitvitalwebApp.controller:HeaderCtrl
 * @description
 * # HeaderCtrl
 * Controller of the gitvitalwebApp
 */
angular.module('gitvitalwebApp')
    .controller('HeaderCtrl', function ($scope, gitvital) {

        var header = this;
        header.isOnline = false;


        gitvital.isOnline().then(function (isOnline) {
            header.isOnline = isOnline;
        });

        header.topMenu = [{
            label: "Search Github",
            state: "main",
    }, {
            label: "Training Data",
            state: "data",
    },{
        label: "About",
        state: "about"
    }];

    });