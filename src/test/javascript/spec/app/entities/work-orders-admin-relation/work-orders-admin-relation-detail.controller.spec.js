'use strict';

describe('Controller Tests', function() {

    describe('WorkOrdersAdminRelation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWorkOrdersAdminRelation, MockWorkOrder, MockUser, MockRelationType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWorkOrdersAdminRelation = jasmine.createSpy('MockWorkOrdersAdminRelation');
            MockWorkOrder = jasmine.createSpy('MockWorkOrder');
            MockUser = jasmine.createSpy('MockUser');
            MockRelationType = jasmine.createSpy('MockRelationType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'WorkOrdersAdminRelation': MockWorkOrdersAdminRelation,
                'WorkOrder': MockWorkOrder,
                'User': MockUser,
                'RelationType': MockRelationType
            };
            createController = function() {
                $injector.get('$controller')("WorkOrdersAdminRelationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartLpcApp:workOrdersAdminRelationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
