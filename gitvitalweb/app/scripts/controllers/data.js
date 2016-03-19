'use strict';

/**
 * @ngdoc function
 * @name gitvitalwebApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the gitvitalwebApp
 */
angular.module('gitvitalwebApp')
    .controller('DataCtrl', function (gitvital, NgTableParams) {

        var data = this;

        var dataCount = 0;

        gitvital.datacount().then(function (count) {
            dataCount = count;
        });
    
        data.loading = true;

        data.columns = ['gitName', 'SizeKb', 'readmeSize', 'isFork', 'stargazers', 'watchers',
                        'language', 'has_issues', 'has_downloads', 'has_wiki', 'has_pages',
                        'forks_count', 'open_issues_count', 'isDefaultMaster', 'score', 'DescLen',
                        'numOfContributors', 'totalContributions', 'avgContributions', 'daysCreated', 'daysUpdated', 'daysPushed', 'isActive'];
    
        data.statistics = gitvital.statistics.query({}).$promise.then(function(resp){
            data.avgSizeKb = resp.averages.avgSizeKb;
            data.avgStargazers = resp.averages.avgStargazers;
            data.avgForks = resp.averages.avgForks;
            data.avgReadmeSize = resp.averages.avgReadmeSize;
            data.avgDescLen = resp.averages.avgDescLen;
            data.avgOpenIssues = resp.averages.avgOpenIssues;
        });

        data.tableParams = new NgTableParams({
            count: 10
        }, {
            getData: function (params) {
                // ajax request to api
                return gitvital.dataset.query({
                    perPage: params.count(),
                    pageNo: params.page()
                }, function (data) {
                    params.total(dataCount);
                    return data;
                }).$promise.then(function (dataset) {
                    data.loading = false;
                    return dataset;
                });
            }
        });
    });