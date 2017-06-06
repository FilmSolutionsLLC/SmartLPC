'use strict';

describe('Controller Tests', function() {

    describe('Ingest Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockIngest, MockUser, MockLookups, MockStorage_Servers;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockIngest = jasmine.createSpy('MockIngest');
            MockUser = jasmine.createSpy('MockUser');
            MockLookups = jasmine.createSpy('MockLookups');
            MockStorage_Servers = jasmine.createSpy('MockStorage_Servers');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Ingest': MockIngest,
                'User': MockUser,
                'Lookups': MockLookups,
                'Storage_Servers': MockStorage_Servers
            };
            createController = function() {
                $injector.get('$controller')("IngestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:ingestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
