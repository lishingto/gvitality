'use strict';

/**
 * @ngdoc service
 * @name gitvitalwebApp.github
 * @description
 * # github
 * Service in the gitvitalwebApp.
 */
angular.module('gitvitalwebApp')
    .service('github', function ($resource) {
    
        return $resource(githubUrl+"search/repositories", {
            q: "java"
        }, {
            query: {
                method: "GET"
            }
        });
    });