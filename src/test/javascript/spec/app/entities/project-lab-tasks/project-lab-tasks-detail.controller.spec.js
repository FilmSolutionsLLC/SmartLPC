'use strict';

describe('Controller Tests', function() {

    describe('ProjectLabTasks Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProjectLabTasks, MockProjects, MockLookups, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProjectLabTasks = jasmine.createSpy('MockProjectLabTasks');
            MockProjects = jasmine.createSpy('MockProjects');
            MockLookups = jasmine.createSpy('MockLookups');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ProjectLabTasks': MockProjectLabTasks,
                'Projects': MockProjects,
                'Lookups': MockLookups,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ProjectLabTasksDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:projectLabTasksUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
