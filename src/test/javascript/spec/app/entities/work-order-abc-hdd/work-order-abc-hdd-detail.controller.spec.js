'use strict';

describe('Controller Tests', function() {

    describe('WorkOrderAbcHdd Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWorkOrderAbcHdd, MockWorkOrder, MockLookups;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWorkOrderAbcHdd = jasmine.createSpy('MockWorkOrderAbcHdd');
            MockWorkOrder = jasmine.createSpy('MockWorkOrder');
            MockLookups = jasmine.createSpy('MockLookups');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'WorkOrderAbcHdd': MockWorkOrderAbcHdd,
                'WorkOrder': MockWorkOrder,
                'Lookups': MockLookups
            };
            createController = function() {
                $injector.get('$controller')("WorkOrderAbcHddDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:workOrderAbcHddUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
