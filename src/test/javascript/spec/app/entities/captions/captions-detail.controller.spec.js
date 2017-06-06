'use strict';

describe('Controller Tests', function() {

    describe('Captions Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCaptions, MockProjects;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCaptions = jasmine.createSpy('MockCaptions');
            MockProjects = jasmine.createSpy('MockProjects');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Captions': MockCaptions,
                'Projects': MockProjects
            };
            createController = function() {
                $injector.get('$controller')("CaptionsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:captionsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
