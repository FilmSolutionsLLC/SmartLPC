'use strict';

describe('Controller Tests', function() {

    describe('ProjectPurchaseOrders Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProjectPurchaseOrders, MockProjects, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProjectPurchaseOrders = jasmine.createSpy('MockProjectPurchaseOrders');
            MockProjects = jasmine.createSpy('MockProjects');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ProjectPurchaseOrders': MockProjectPurchaseOrders,
                'Projects': MockProjects,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ProjectPurchaseOrdersDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:projectPurchaseOrdersUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
