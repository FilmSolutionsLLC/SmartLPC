'use strict';

describe('Controller Tests', function() {

    describe('Batch Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBatch, MockProjects, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBatch = jasmine.createSpy('MockBatch');
            MockProjects = jasmine.createSpy('MockProjects');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Batch': MockBatch,
                'Projects': MockProjects,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("BatchDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:batchUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
