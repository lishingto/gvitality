'use strict';

/**
 * @ngdoc function
 * @name gitvitalwebApp.controller:ClassifyCtrl
 * @description
 * # ClassifyCtrl
 * Controller of the gitvitalwebApp
 */
angular.module('gitvitalwebApp')
    .controller('ClassifyCtrl', function ($stateParams, gitvital, moment) {
        var classify = this;

        classify.loading = true;
        classify.inList = $stateParams.list;
        classify.nameList = [];
    
        classify.procList = [];
    
        classify.moment = moment;

        for (var i = 0; i < classify.inList.length; i++) {
            classify.nameList[i] = classify.inList[i].full_name;
        }
    
        gitvital.classifyList(classify.nameList).then(function(response){
            classify.loading = false;
            classify.procList = response.data.classified;
            for(var r in classify.procList){
                var repo = classify.procList[r];
                var updated = moment(repo.repo.updated_at);
                repo.updatedDays = updated.fromNow();
                repo.isUpdated = moment().diff(updated, 'days') < daysThreshold;
            }
        });

    });