'use strict';

describe('Directive: boolLabel', function () {

  // load the directive's module
  beforeEach(module('gitvitalwebApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<bool-label></bool-label>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the boolLabel directive');
  }));
});
