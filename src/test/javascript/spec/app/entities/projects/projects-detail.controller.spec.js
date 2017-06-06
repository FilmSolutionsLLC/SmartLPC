'use strict';

describe('Controller Tests', function() {

    describe('Projects Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProjects, MockLookups, MockContacts, MockUser, MockDepartments, MockStorage_Disk;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProjects = jasmine.createSpy('MockProjects');
            MockLookups = jasmine.createSpy('MockLookups');
            MockContacts = jasmine.createSpy('MockContacts');
            MockUser = jasmine.createSpy('MockUser');
            MockDepartments = jasmine.createSpy('MockDepartments');
            MockStorage_Disk = jasmine.createSpy('MockStorage_Disk');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Projects': MockProjects,
                'Lookups': MockLookups,
                'Contacts': MockContacts,
                'User': MockUser,
                'Departments': MockDepartments,
                'Storage_Disk': MockStorage_Disk
            };
            createController = function() {
                $injector.get('$controller')("ProjectsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:projectsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
