/**
 * Created by macbookpro on 2/27/17.
 */
(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderUpdateController', WorkOrderUpdateController);

    WorkOrderUpdateController.$inject = ['$uibModal', '$http', '$scope', '$rootScope', '$stateParams', 'entity', 'WorkOrder', 'Lookups', 'Projects', 'User', 'Contacts'];

    function WorkOrderUpdateController($uibModal, $http, $scope, $rootScope, $stateParams, entity, WorkOrder, Lookups, Projects, User, Contacts) {
        var vm = this;
        vm.workOrderDTO = entity;
        console.log(JSON.stringify(vm.workOrderDTO));
        vm.lookupss = Lookups.query();
        vm.projectss = [];

        vm.relationType = []
        $http({
            method: 'GET',
            url: '/api/idname/projects'
        }).then(function (response) {
            console.log("total projects : " + response.data.length);
            vm.temp = response.data;
            console.log("first project " + JSON.stringify(vm.temp[0][1]));
            console.log("first id " + vm.temp[0][0]);
            for (var i = 0; i < vm.temp.length; i++) {
                $scope.tempObj = {"id": vm.temp[i][0], "name": vm.temp[i][1]}
                vm.projectss.push($scope.tempObj);
            }
        })
        vm.users = User.query();
        vm.contactss = Contacts.query();


        vm.onProjectChange = function () {

            console.log(JSON.stringify(vm.workOrder.project));
            console.log(vm.workOrder.project.id);

            $http({
                method: 'GET',
                url: 'api/project-purchase-orders/projects/' + vm.workOrder.project.id
            }).then(function (response) {
                vm.purchaseOrders = response.data;
                console.log("PURCHASE ORDERS : " + JSON.stringify(vm.purchaseOrders));
            });

            $http({
                method: 'GET',
                url: 'api/project-lab-tasks/projects/' + vm.workOrder.project.id
            }).then(function (response) {
                vm.lab = response.data;
                console.log("PURCHASE LAB Tasks : " + JSON.stringify(vm.lab));
            });


        };

        vm.onTypeChange = function () {
            vm.showPKOFlag = true;
        };

        vm.workOrderType = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/type_id'
        }).then(function successCallback(response) {
            vm.workOrderType = response.data;
            console.log(JSON.toString(vm.workOrderType));
        }, function errorCallback(response) {
        });

        vm.workOrderPriority = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/priority_id'
        }).then(function successCallback(response) {
            vm.workOrderPriority = response.data;

        }, function errorCallback(response) {
        });

        vm.workOrderType = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/type_id'
        }).then(function successCallback(response) {
            vm.workOrderType = response.data;
            console.log(JSON.toString(vm.workOrderType));
        }, function errorCallback(response) {
        });

        vm.workOrderPriority = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/priority_id'
        }).then(function successCallback(response) {
            vm.workOrderPriority = response.data;

        }, function errorCallback(response) {
        });

        vm.abcHdd = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/abc_hdd_to'
        }).then(function successCallback(response) {
            vm.abcHdd = response.data;

        }, function errorCallback(response) {
        });

        vm.workOrderStatus = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/status_id'
        }).then(function successCallback(response) {
            vm.workOrderStatus = response.data;

        }, function errorCallback(response) {
        });

        vm.workOrderAbcFileType = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/abc_file_type'
        }).then(function successCallback(response) {
            vm.workOrderAbcFileType = response.data;

        }, function errorCallback(response) {
        });


        vm.printSize = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/print_size_id'
        }).then(function successCallback(response) {
            vm.printSize = response.data;

        }, function errorCallback(response) {
        });


        vm.printSurface = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/print_surface_id'
        }).then(function successCallback(response) {
            vm.printSurface = response.data;

        }, function errorCallback(response) {
        });


        vm.printBleed = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/print_bleed_id'
        }).then(function successCallback(response) {
            vm.printBleed = response.data;

        }, function errorCallback(response) {
        });


        vm.printFilename = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/print_filename_flag'
        }).then(function successCallback(response) {
            vm.printFilename = response.data;

        }, function errorCallback(response) {
        });


        vm.printPhotoCredit = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/print_photo_credit_id'
        }).then(function successCallback(response) {
            vm.printPhotoCredit = response.data;

        }, function errorCallback(response) {
        });

        vm.printCreditLocation = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_orders/print_photo_credit_location_id'
        }).then(function successCallback(response) {
            vm.printCreditLocation = response.data;

        }, function errorCallback(response) {
        });


        $rootScope.$watch(function () {
            return $rootScope.relationships;
        }, function () {
            if ($rootScope.relationships == null) {
                console.log("null rootscope");

            } else {
                console.log("not null");

                vm.currrentOBJ = $rootScope.relationships;
                console.log("========> " + JSON.stringify(vm.currentOBJ));
                if (angular.equals(vm.currrentOBJ.elementID, 'field_vm.workOrder.requestor')) {
                    console.log("found equal");

                    vm.workOrderDTO.workOrder.requestor = vm.currrentOBJ.data;

                } else {
                }
            }
        });

        vm.openModal = function (elementID) {

            console.log("id of textbox : " + elementID);
            //var ctrl = angular.element(id).data('$ngModelController');

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/contacts/simpleModal.html',
                controller: 'SimpleController',
                size: 'lg',
                scope: $scope,
                controllerAs: 'vm',
                backdrop: 'static',
                resolve: {
                    sendID: function () {
                        return elementID;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contacts');
                        $translatePartialLoader.addPart('projects');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.workOrderDTO.workOrder.id !== null) {
                //WorkOrder.update(vm.workOrder, onSaveSuccess, onSaveError);
                console.log("updating work order");
                console.log(JSON.stringify(vm.workOrderDTO));
            } else {

                console.log("saving work order");
                console.log(JSON.stringify(vm.workOrderDTO));
                WorkOrder.save(vm.workOrderDTO, onSaveSuccess, onSaveError);
            }
        };

    }
})();
