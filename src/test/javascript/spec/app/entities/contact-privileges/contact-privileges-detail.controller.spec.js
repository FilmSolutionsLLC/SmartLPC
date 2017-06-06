'use strict';

describe('Controller Tests', function() {

    describe('ContactPrivileges Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockContactPrivileges, MockProjects, MockContacts, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockContactPrivileges = jasmine.createSpy('MockContactPrivileges');
            MockProjects = jasmine.createSpy('MockProjects');
            MockContacts = jasmine.createSpy('MockContacts');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ContactPrivileges': MockContactPrivileges,
                'Projects': MockProjects,
                'Contacts': MockContacts,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ContactPrivilegesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:contactPrivilegesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
