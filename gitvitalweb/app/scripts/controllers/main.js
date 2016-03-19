'use strict';

/**
 * @ngdoc function
 * @name gitvitalwebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gitvitalwebApp
 */
angular.module('gitvitalwebApp')
    .controller('MainCtrl', function ($state, NgTableParams, github, gitvital, moment) {
        var main = this;
        main.loading = true;

        main.search = "apache";

        main.submit = function () {
            main.loading = true;
            main.tableParams.reload();
        }

        main.fromNow = function (dateStr) {
            return moment(dateStr).fromNow();
        }

        main.classifyList = [];

        main.add = function (repo) {
            if (main.classifyList.indexOf(repo) === -1) {
                main.classifyList.push(repo);
            }
        }

        main.remove = function (repo) {
            var index = main.classifyList.indexOf(repo);
            main.classifyList.splice(index, 1);
        }

        main.sendList = function () {
            if (main.classifyList.length > 0) {
                $state.go('classify', {
                    list: main.classifyList
                });
            }
        }

        main.tableParams = new NgTableParams({
            count: 25

        }, {
            getData: function (params) {
                return github.query({
                    page: params.page(),
                    per_page: params.count(),

                    q: main.search
                }, function (data, headersGetter) {
                    params.total(data.total_count);
                    return data;
                }).$promise.then(function (data) {
                    main.loading = false;
                    return data.items;
                });
            }
        });
    });