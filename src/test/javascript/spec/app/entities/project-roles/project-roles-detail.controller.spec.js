'use strict';

describe('Controller Tests', function() {

    describe('ProjectRoles Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProjectRoles, MockProjects, MockContacts, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProjectRoles = jasmine.createSpy('MockProjectRoles');
            MockProjects = jasmine.createSpy('MockProjects');
            MockContacts = jasmine.createSpy('MockContacts');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ProjectRoles': MockProjectRoles,
                'Projects': MockProjects,
                'Contacts': MockContacts,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ProjectRolesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:projectRolesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
