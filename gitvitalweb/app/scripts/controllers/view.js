'use strict';

/**
 * @ngdoc function
 * @name gitvitalwebApp.controller:ViewCtrl
 * @description
 * # ViewCtrl
 * Controller of the gitvitalwebApp
 */
angular.module('gitvitalwebApp')
    .controller('ViewCtrl', function ($scope, $stateParams, gitvital, moment) {
        var view = this;

        view.loading = true;
        view.repoName = $stateParams.repoName;

        var nameAry = view.repoName.split("/");
        var owner = nameAry[0];
        var repoName = nameAry[1];
        var format = "DD MMM YYYY HH:mm";

        view.updatedDays = 0;
        view.isUpdated = false;

        view.threshold = {
            SizeKb: 60,
            readmeSize: 11700,
            stargazers: 5,
            forks_count: 1,
            open_issues_count: 1,
            DescLen: 128,
            numOfContributors: 3
        };

        view.dataset = gitvital.classify.query({
            owner: owner,
            repo: repoName
        }, function (data) {
            view.classify = data.classify[0];
            view.repo = data.repo[0];

            view.repo.created_at = moment(view.repo.created_at).format(format);

            view.repo.pushed_at = moment(view.repo.pushed_at).format(format);

            view.updatedDays = moment(view.repo.updated_at).fromNow();
            view.isUpdated = moment().diff(moment(view.repo.updated_at), 'days')<daysThreshold;
            view.repo.updated_at = moment(view.repo.updated_at).format(format);


            view.isActive = view.classify.isActive === "true";

            view.debug = JSON.stringify(data);
            view.loading = false;
        });

    });