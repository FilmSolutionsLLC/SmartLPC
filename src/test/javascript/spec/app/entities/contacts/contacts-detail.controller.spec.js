'use strict';

describe('Controller Tests', function() {

    describe('Contacts Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockContacts, MockLookups, MockDepartments, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockContacts = jasmine.createSpy('MockContacts');
            MockLookups = jasmine.createSpy('MockLookups');
            MockDepartments = jasmine.createSpy('MockDepartments');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Contacts': MockContacts,
                'Lookups': MockLookups,
                'Departments': MockDepartments,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ContactsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:contactsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
