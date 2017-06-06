'use strict';

describe('Controller Tests', function() {

    describe('WorkOrderAbcFile Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWorkOrderAbcFile, MockWorkOrder, MockLookups;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWorkOrderAbcFile = jasmine.createSpy('MockWorkOrderAbcFile');
            MockWorkOrder = jasmine.createSpy('MockWorkOrder');
            MockLookups = jasmine.createSpy('MockLookups');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'WorkOrderAbcFile': MockWorkOrderAbcFile,
                'WorkOrder': MockWorkOrder,
                'Lookups': MockLookups
            };
            createController = function() {
                $injector.get('$controller')("WorkOrderAbcFileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:workOrderAbcFileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
