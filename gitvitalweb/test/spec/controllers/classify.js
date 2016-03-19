'use strict';

describe('Controller: ClassifyCtrl', function () {

  // load the controller's module
  beforeEach(module('gitvitalwebApp'));

  var ClassifyCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ClassifyCtrl = $controller('ClassifyCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ClassifyCtrl.awesomeThings.length).toBe(3);
  });
});
