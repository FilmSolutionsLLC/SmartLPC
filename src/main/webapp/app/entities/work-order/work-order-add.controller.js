/**
 * Created by macbookpro on 11/14/16.
 */
(function () {
    'use strict';

    angular.module('smartLpcApp').controller('WorkOrderAddController',
        WorkOrderAddController);

    WorkOrderAddController.$inject = ['$ngConfirm', 'Principal', '$state', 'RelationType',
        '$rootScope', '$uibModal', '$scope', '$stateParams', 'entity',
        'WorkOrder', 'Lookups', 'Projects', 'User', 'Contacts', '$http'];

    function WorkOrderAddController($ngConfirm, Principal, $state, RelationType,
                                    $rootScope, $uibModal, $scope, $stateParams, entity, WorkOrder,
                                    Lookups, Projects, User, Contacts, $http) {


        var vm = this;


        vm.currentAccount = null;
        Principal.identity().then(function (account) {
            vm.currentAccount = account;

            console.log("Current User : " + JSON.stringify(vm.currentAccount));
        });

        $scope.toggle = function () {
            $scope.showOptions = !$scope.showOptions;
        }


        $scope.options = ['Car', 'Lorry', 'Motorbike', 'Bus', 'Train',
            'Airplane']
        $scope.selected = [];
        $scope.update = function () {
            $scope.count = $scope.selected.filter(isSelected).length
            console.log($scope.selected.filter(isSelected));
            console.log(JSON.stringify($scope.selected));
        }

        function isSelected(element) {
            return element;
        }

        /*vm.trackingCheck = function () {
            vm.selecteddueMountReminder.push({
                "admin_user": vm.currentAccount ,
                "relation_type": {
                    "id": 6,
                    "relation": "reminder1"
                }});
            vm.selectedclientReminder.push({
                "admin_user": vm.currentAccount ,
                "relation_type": {
                    "id": 8,
                    "relation": "reminder3"
                }});
            vm.selectedtoMountReminder.push({
                "admin_user": vm.currentAccount,
                "relation_type": {
                    "id": 7,
                    "relation": "reminder2"
                }});
        };*/


        vm.workOrder = entity;
        vm.lookupss = Lookups.query();
        vm.projectss = [];

        vm.relationType = []
        vm.workOrdersAdminRelations = [];

        vm.invoiced = [
            {"id":105,"name":"Yes"},
            {"id":106,"name":"Comp"},
            {"id":107,"name":"Included"},
            {"id":108,"name":"No"},
        ]
        RelationType.query({
            size: 100
        }, onSuccess1);

        function onSuccess1(data, headers) {
            console.log("Loaded Relation Types");

            for (var i = 0; i < data.length; i++) {

                console.log("Relation Type : " + JSON.stringify(data[i]));
                vm.workOrdersAdminRelations[i] = {
                    "admin_user": null,
                    "relation_type": data[i]
                };

            }

            vm.workOrdersAdminRelationType = data;
        };

        $http({
            method: 'GET',
            url: 'api/idname/projects'
        }).then(function (response) {
            console.log("total projects : " + response.data.length);
            vm.temp = response.data;
            console.log("first project " + JSON.stringify(vm.temp[0][1]));
            console.log("first id " + vm.temp[0][0]);
            for (var i = 0; i < vm.temp.length; i++) {
                $scope.tempObj = {
                    "id": vm.temp[i][0],
                    "name": vm.temp[i][1]
                }
                vm.projectss.push($scope.tempObj);
            }
        })
        vm.adminClicked = function () {
            vm.selectedAdmins = vm.selectedverifiedBy.concat(
                vm.selectedingestBy).concat(vm.selectedprintBy).concat(
                vm.selectedprocessedBy).concat(vm.selecteduploadBy).concat(
                vm.selectedarchivedBy);

            console.log("FINALLY GOT THE ONE : "
                + JSON.stringify(vm.selectedAdmins));
        };

        // vm.users = User.query();
        vm.selectedAdmins = [];
        vm.assignedTo = []
        vm.selectedAssignedTo = [];

        vm.auditedBy = [];
        vm.selectedauditedBy = [];

        vm.dueMountReminder = [];
        vm.selecteddueMountReminder = [];

        vm.clientReminder = [];
        vm.selectedclientReminder = [];

        vm.toMountReminder = [];
        vm.selectedtoMountReminder = [];

        vm.ingestBy = [];
        vm.selectedingestBy = [];

        vm.printBy = [];
        vm.selectedprintBy = [];

        vm.processedBy = [];
        vm.selectedprocessedBy = [];

        vm.uploadBy = [];
        vm.selecteduploadBy = [];

        vm.verifiedBy = [];
        vm.selectedverifiedBy = [];

        vm.archivedBy = [];
        vm.selectedarchivedBy = [];

        $http({
            method: 'GET',
            url: 'api/wo/allusers'
        }).then(
            function successCallback(response) {
                vm.admins = response.data;
                console.log("Got admin users : " + vm.admins.length);
                for (var i = 0; i < vm.admins.length; i++) {

                    if (angular.equals(vm.admins[i].dropdownName,
                            'WO_ASSIGNED_TO')) {
                        console.log("Addded user assignedTo: "
                            + vm.admins[i].admin.fullName)
                        vm.assignedTo.push(vm.admins[i].admin);

                        vm.assignedTo.push
                    }

                    else if (angular.equals(vm.admins[i].dropdownName,
                            'WO_AUDITED_BY')) {
                        console.log("Addded user auditedBy: "
                            + vm.admins[i].admin.fullName)
                        vm.auditedBy.push(vm.admins[i].admin);

                    }


                    else if (angular.equals(vm.admins[i].dropdownName,
                            'WO_INGEST_BY')) {
                        console.log("Addded user ingestBy: "
                            + vm.admins[i].admin.fullName)
                        // vm.ingestBy.push(vm.admins[i].admin);

                        vm.ingestBy.push({
                            "admin_user": vm.admins[i].admin,
                            "relation_type": {
                                "id": 3,
                                "relation": "lab_ingestby"
                            }
                        });
                    }


                    else if (angular.equals(vm.admins[i].dropdownName,
                            'WO_PRINT_BY')) {
                        console.log("Addded user printBy: "
                            + vm.admins[i].admin.fullName)
                        // vm.printBy.push(vm.admins[i].admin);
                        vm.printBy.push({
                            "admin_user": vm.admins[i].admin,
                            "relation_type": {
                                "id": 4,
                                "relation": "lab_print"
                            }
                        });

                    } else if (angular.equals(vm.admins[i].dropdownName,
                            'WO_PROCESSED_BY')) {
                        console.log("Addded user processedBy: "
                            + vm.admins[i].admin.fullName)
                        // vm.processedBy.push(vm.admins[i].admin);
                        vm.processedBy.push({
                            "admin_user": vm.admins[i].admin,
                            "relation_type": {
                                "id": 2,
                                "relation": "lab_processed"
                            }
                        });

                    }

                    else if (angular.equals(vm.admins[i].dropdownName,
                            'WO_TO_CLIENT_REMINDER')) {
                        console.log("Addded user clientReminder: "
                            + vm.admins[i].admin.fullName)
                        //vm.clientReminder.push(vm.admins[i].admin);
                        vm.clientReminder.push({
                            "admin_user": vm.admins[i].admin,
                            "relation_type": {
                                "id": 8,
                                "relation": "reminder3"
                            }
                        });
                    }

                    else if (angular.equals(vm.admins[i].dropdownName,
                            'WO_TO_MOUNT_REMINDER')) {
                        console.log("Addded user toMountReminder: "
                            + vm.admins[i].admin.fullName);
                        //vm.toMountReminder.push(vm.admins[i].admin);
                        vm.toMountReminder.push({
                            "admin_user": vm.admins[i].admin,
                            "relation_type": {
                                "id": 7,
                                "relation": "reminder2"
                            }
                        });
                    }
                    else if (angular.equals(vm.admins[i].dropdownName,
                            'WO_DUE_MOUNT_REMINDER')) {
                        console.log("Addded user dueMountReminder: "
                            + vm.admins[i].admin.fullName)
                        //vm.dueMountReminder.push(vm.admins[i].admin);
                        vm.dueMountReminder.push({
                            "admin_user": vm.admins[i].admin,
                            "relation_type": {
                                "id": 6,
                                "relation": "reminder1"
                            }
                        });
                    }


                    else if (angular.equals(vm.admins[i].dropdownName,
                            'WO_UPLOAD_BY')) {
                        console.log("Addded user uploadBy: "
                            + vm.admins[i].admin.fullName);
                        // vm.uploadBy.push(vm.admins[i].admin);

                        vm.uploadBy.push({
                            "admin_user": vm.admins[i].admin,
                            "relation_type": {
                                "id": 5,
                                "relation": "lab_upload"
                            }

                        });

                    } else if (angular.equals(vm.admins[i].dropdownName,
                            'WO_VERIFIED_BY')) {
                        console.log("Addded user verifiedBy: "
                            + vm.admins[i].admin.fullName)

                        // vm.verifiedBy.push(vm.admins[i].admin);

                        vm.verifiedBy.push({
                            "admin_user": vm.admins[i].admin,
                            "relation_type": {
                                "id": 1,
                                "relation": "lab_verified"
                            }

                        });

                    } else if (angular.equals(vm.admins[i].dropdownName,
                            'WO_ARCHIVED_BY')) {
                        console.log("Addded user archivedBy: "
                            + vm.admins[i].admin.fullName)
                        // vm.archivedBy.push(vm.admins[i].admin);
                        vm.archivedBy.push({
                            "admin_user": vm.admins[i].admin,
                            "relation_type": {
                                "id": 9,
                                "relation": "lab_archived"
                            }

                        });

                    }
                }

                console.log("NEW JSON ---> VERIFIED : "
                    + JSON.stringify(vm.verifiedBy));
            }, function errorCallback(response) {
            });

        //vm.contactss = Contacts.query();

        vm.simples = null;

        vm.purchaseOrders = [];
        vm.lab = [];

        vm.onProjectChange = function () {

            console.log(JSON.stringify(vm.workOrder.project));
            console.log(vm.workOrder.project.id);

            $http(
                {
                    method: 'GET',
                    url: 'api/project-purchase-orders/projects/'
                    + vm.workOrder.project.id
                }).then(
                function (response) {
                    vm.purchaseOrders = response.data;
                    console.log("PURCHASE ORDERS : "
                        + JSON.stringify(vm.purchaseOrders));
                });

            $http(
                {
                    method: 'GET',
                    url: 'api/project-lab-tasks/projects/'
                    + vm.workOrder.project.id
                }).then(function (response) {
                vm.lab = response.data;
                console.log("PURCHASE LAB Tasks : " + JSON.stringify(vm.lab));
            });

        };

        vm.onTypeChange = function () {

            vm.showPKOFlag = true;

            console.log("WO Type : " + JSON.stringify(vm.workOrder.type));
            console.log("actual type : " + vm.workOrder.type.textValue);
        };

        vm.workOrderType = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/type_id'
        }).then(function successCallback(response) {
            vm.workOrderType = response.data;
            console.log(JSON.stringify(vm.workOrderType));
        }, function errorCallback(response) {
        });

        vm.workOrderPriority = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/priority_id'
        }).then(function successCallback(response) {
            vm.workOrderPriority = response.data;
            console.log("priority_id");
        }, function errorCallback(response) {
        });

        vm.abcHdd = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/abc_hdd_to'
        }).then(function successCallback(response) {
            vm.abcHdd = response.data;
            console.log('abc_hdd_to');
        }, function errorCallback(response) {
        });

        vm.workOrderStatus = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/status_id'

        }).then(function successCallback(response) {
            vm.workOrderStatus = response.data;
            console.log("status_id");
        }, function errorCallback(response) {
        });

        vm.workOrderAbcFileType = {};
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/abc_file_type'
        }).then(function successCallback(response) {
            vm.workOrderAbcFileType = response.data;
            console.log("abc file type");
        }, function errorCallback(response) {
        });

        vm.printSize = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/print_size_id'
        }).then(function successCallback(response) {
            vm.printSize = response.data;
            console.log("print size");
        }, function errorCallback(response) {
        });

        vm.printSurface = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/print_surface_id'
        }).then(function successCallback(response) {
            vm.printSurface = response.data;
            console.log("print_surface_id");
        }, function errorCallback(response) {
        });

        vm.printBleed = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/print_bleed_id'
        }).then(function successCallback(response) {
            vm.printBleed = response.data;
            console.log("print_bleed_id");
        }, function errorCallback(response) {
        });

        vm.printFilename = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/print_filename_flag'
        }).then(function successCallback(response) {
            console.log("Print Filename Flag");
            vm.printFilename = response.data;

        }, function errorCallback(response) {
        });

        vm.printPhotoCredit = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/print_photo_credit_id'
        }).then(function successCallback(response) {
            vm.printPhotoCredit = response.data;
            console.log("print photo credit");
        }, function errorCallback(response) {
        });

        vm.printCreditLocation = [];
        $http({
            method: 'GET',
            url: 'api/lookups/get/work_order/print_photo_credit_location_id'
        }).then(function successCallback(response) {
            vm.printCreditLocation = response.data;
            console.log("print_photo_credit_location_id");
        }, function errorCallback(response) {
        });

        vm.load = function (id) {
            WorkOrder.get({
                id: id
            }, function (result) {
                vm.workOrder = result;
            });
        };

        vm.adminRelationDTOs = [];
        vm.workOrderAbcFiles = [];
        vm.workOrderAbcHdds = [];
        vm.workOrdersAdminRelation = [];


        vm.save = function () {
            vm.workOrdersAdminRelations =
                vm.selectedverifiedBy.
                concat(vm.selectedtoMountReminder).
                concat(vm.selectedclientReminder).
                concat(vm.selecteddueMountReminder).
                concat(vm.selectedingestBy).
                concat(vm.selectedprintBy).
                concat(vm.selectedprocessedBy).
                concat(vm.selecteduploadBy).
                concat(vm.selectedarchivedBy);

            vm.workOrderDTO = {
                "workOrder": vm.workOrder,
                "workOrdersAdminRelations": vm.workOrdersAdminRelations,
                "workOrderAbcFiles": vm.workOrderAbcFiles,
                "workOrderAbcHdds": vm.workOrderAbcHdds
            };
            vm.isSaving = true;

            if (vm.workOrderDTO.workOrder.id !== null) {
                WorkOrder.update(vm.workOrderDTO, onSaveSuccess, onSaveError);
                console.log("updating work order");
            } else {
                console.log("saving work order");
                console.log(JSON.stringify(vm.workOrderDTO));
                WorkOrder.save(vm.workOrderDTO, onSaveSuccess, onSaveError);
            }

        };
        var onSaveSuccess = function (result) {
            //$scope.$emit('smartLpcApp:workOrderUpdate', result);
            $ngConfirm({
                title: 'Success!',
                content: "WorkOrder with id : <strong>" + result.id + "</strong> has been created",
                type: 'red',
                typeAnimated: true,
                theme: 'dark',
                buttons: {
                    confirm: {
                        text: 'Okay',
                        btnClass: 'btn-red',
                        action: function () {
                        }
                    }
                }
            });
            vm.isSaving = false;
            $state.go('work-order', {}, {reload: true});// use for redirecting ...
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.clear = function () {
            // $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.requestDate = false;
        vm.datePickerOpenStatus.reminderDate1 = false;
        vm.datePickerOpenStatus.reminderDate2 = false;
        vm.datePickerOpenStatus.reminderDate3 = false;
        vm.datePickerOpenStatus.processingDateRecieved = false;
        vm.datePickerOpenStatus.processingDateShipped = false;
        vm.datePickerOpenStatus.dueToClientReminder = false;
        vm.datePickerOpenStatus.dueToMounterReminder = false;
        vm.datePickerOpenStatus.recievedFromMounterReminder = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;
        vm.datePickerOpenStatus.completionDate = false;
        vm.datePickerOpenStatus.processingProofShipped = false;

        vm.openCalendar = function (date) {
            vm.datePickerOpenStatus[date] = true;
        };


        $rootScope.$watch(function () {
            return $rootScope.relationships;
        }, function () {
            if ($rootScope.relationships == null) {
                console.log("null rootscope");

            } else {
                console.log("not null");

                vm.currrentOBJ = $rootScope.relationships;
                console.log("========> " + JSON.stringify(vm.currentOBJ));
                if (angular.equals(vm.currrentOBJ.elementID,
                        'field_vm.workOrder.requestor')) {
                    console.log("found equal");

                    vm.workOrder.requestor = vm.currrentOBJ.data;

                } else {
                }
            }
        });


        vm.openModal = function (elementID) {

            console.log("id of textbox : " + elementID);
            // var ctrl = angular.element(id).data('$ngModelController');

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
                    translatePartialLoader: ['$translate',
                        '$translatePartialLoader',
                        function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('contacts');
                            $translatePartialLoader.addPart('projects');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                }
            })
        };

    }
})();
