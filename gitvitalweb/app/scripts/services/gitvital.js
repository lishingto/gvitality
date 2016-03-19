'use strict';

/**
 * @ngdoc service
 * @name gitvitalwebApp.gitvital
 * @description
 * # gitvital
 * Service in the gitvitalwebApp.
 */
angular.module('gitvitalwebApp')
    .service('gitvital', function ($resource, $http, $q) {



        return {
            isOnline: function () {
                return $http.get(gitvitalUrl + "admin").then(function (data) {
                    return true;
                }, function (error, status) {
                    return false;
                });
            },
            datacount: function () {
                return $http.get(gitvitalUrl + "stats/dataset/count").then(function (response) {
                    return response.data.count;
                });
            },
            dataset: $resource(gitvitalUrl + "stats/dataset/:perPage/:pageNo", {}, {
                query: {
                    method: "GET",
                    isArray: true
                }
            }),
            classify: $resource(gitvitalUrl + "classify/:owner/:repo", {}, {
                query: {
                    method: "GET"
                }
            }),
            classifyList: function (list) {
                return $http.post(gitvitalUrl + "classify", list);
            },
            statistics: $resource(gitvitalUrl + "stats/dataset/stats", {}, {
                query: {
                    method: "GET"
                }
            })

        };
    });