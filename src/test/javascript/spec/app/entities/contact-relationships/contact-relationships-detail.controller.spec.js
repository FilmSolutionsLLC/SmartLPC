'use strict';

describe('Controller Tests', function() {

    describe('ContactRelationships Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockContactRelationships, MockUser, MockContacts;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockContactRelationships = jasmine.createSpy('MockContactRelationships');
            MockUser = jasmine.createSpy('MockUser');
            MockContacts = jasmine.createSpy('MockContacts');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ContactRelationships': MockContactRelationships,
                'User': MockUser,
                'Contacts': MockContacts
            };
            createController = function() {
                $injector.get('$controller')("ContactRelationshipsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:contactRelationshipsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
