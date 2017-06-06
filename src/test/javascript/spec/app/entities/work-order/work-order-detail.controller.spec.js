'use strict';

describe('Controller Tests', function() {

    describe('WorkOrder Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWorkOrder, MockLookups, MockProjects, MockUser, MockContacts;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWorkOrder = jasmine.createSpy('MockWorkOrder');
            MockLookups = jasmine.createSpy('MockLookups');
            MockProjects = jasmine.createSpy('MockProjects');
            MockUser = jasmine.createSpy('MockUser');
            MockContacts = jasmine.createSpy('MockContacts');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'WorkOrder': MockWorkOrder,
                'Lookups': MockLookups,
                'Projects': MockProjects,
                'User': MockUser,
                'Contacts': MockContacts
            };
            createController = function() {
                $injector.get('$controller')("WorkOrderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:workOrderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
