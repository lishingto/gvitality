'use strict';

describe('Service: gitvital', function () {

  // load the service's module
  beforeEach(module('gitvitalwebApp'));

  // instantiate service
  var gitvital;
  beforeEach(inject(function (_gitvital_) {
    gitvital = _gitvital_;
  }));

  it('should do something', function () {
    expect(!!gitvital).toBe(true);
  });

});
