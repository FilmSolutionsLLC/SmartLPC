'use strict';

describe('Controller Tests', function() {

    describe('Storage_Disk Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStorage_Disk, MockStorage_Servers;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStorage_Disk = jasmine.createSpy('MockStorage_Disk');
            MockStorage_Servers = jasmine.createSpy('MockStorage_Servers');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Storage_Disk': MockStorage_Disk,
                'Storage_Servers': MockStorage_Servers
            };
            createController = function() {
                $injector.get('$controller')("Storage_DiskDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:storage_DiskUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
