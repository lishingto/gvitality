'use strict';

/**
 * @ngdoc directive
 * @name gitvitalwebApp.directive:boolLabel
 * @description
 * # boolLabel
 */
angular.module('gitvitalwebApp')
  .directive('boolLabel', function () {
    return {
      restrict: 'E',
      compile: function postLink(element, attrs) {
          var condition = attrs.condition;
          var html = '<span><span class="label" ng-class="'+condition+'?\'label-success\':\'label-danger\'"><i class="glyphicon" ng-class="'+condition+'?\'glyphicon-ok\':\'glyphicon-remove\'"></i> {{'
          +condition+'?"Good":"Bad"}}</span></span>';
          element.replaceWith(html);
      }
    };
  });
