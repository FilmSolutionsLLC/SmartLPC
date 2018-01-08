(function () {
    'use strict';

    angular
        .module('smartLpcApp')
        .controller('WorkOrderDetailController', WorkOrderDetailController);

    WorkOrderDetailController.$inject = ['$http', '$scope', '$rootScope', '$stateParams', 'entity', 'WorkOrder', 'Lookups', 'Projects', 'User', 'Contacts'];

    function WorkOrderDetailController($http, $scope, $rootScope, $stateParams, entity, WorkOrder, Lookups, Projects, User, Contacts) {
        var vm = this;
        vm.workOrderDTO = entity;
        console.log(JSON.stringify(vm.workOrderDTO));
        vm.purchaseOrders = [];
        //vm.mailTo = "mailto:"+vm.workOrderDTO.workOrder.requestor.email;
        if (angular.equals(vm.workOrderDTO.workOrder.project, null)) {

        } else {
            $http({
                method: 'GET',
                url: 'api/project-purchase-orders/projects/' + vm.workOrderDTO.workOrder.project.id
            }).then(function (response) {
                vm.purchaseOrders = response.data;
                console.log("project purchaseOrders : " + JSON.stringify(vm.purchaseOrders));
            });


            $http({
                method: 'GET',
                url: 'api/project-lab-tasks/projects/' + vm.workOrderDTO.workOrder.project.id
            }).then(function (response) {
                vm.labs = response.data;
                console.log("project LAB Tasks : " + JSON.stringify(vm.labs));
            });
        }

        vm.prevNext = {};
        $http({
            method: 'GET',
            url: 'api/prev/next/work-orders/' + vm.workOrderDTO.workOrder.id

        }).then(function successCallback(response) {
            vm.prevNext = response.data;
            console.log("Prev Next workOrders : " + JSON.stringify(vm.prevNext));
        }, function errorCallback(response) {

        });


        /*console.log(JSON.stringify(vm.workOrderDTO));
        vm.workOrder = vm.workOrderDTO.workOrder;
        vm.workOrderAbcFiles = vm.workOrderDTO.workOrderAbcFiles;
        vm.workOrderAbcHdds = vm.workOrderDTO.workOrderAbcHdds;
        vm.workOrdersAdminRelations = vm.workOrderDTO.workOrdersAdminRelations;
        console.log(" ===== > " + JSON.stringify(vm.workOrder));
        console.log(" ===== > " + JSON.stringify(vm.workOrderAbcFiles));
        console.log(" ===== > " + JSON.stringify(vm.workOrderAbcHdds));
        console.log(" ===== > " + JSON.stringify(vm.workOrdersAdminRelations));

        Projects.get({id: vm.workOrderDTO.workOrder.project.id}, function (result) {
            vm.projectsDTO = result;
            vm.purchaseOrders = vm.projectsDTO.projectPurchaseOrderses;
            vm.labs = vm.projectsDTO.projectLabTaskses;

        });
        console.log(" ===== > " + JSON.stringify(vm.workOrder));
        console.log(" ===== > " + JSON.stringify(vm.workOrderAbcFiles));
        console.log(" ===== > " + JSON.stringify(vm.workOrderAbcHdds));
        console.log(" ===== > " + JSON.stringify(vm.workOrdersAdminRelations));

        vm.load = function (id) {
            WorkOrder.get({id: id}, function (result) {
                vm.workOrderDTO = result;
                vm.workOrder = vm.workOrderDTO.workOrder;
                vm.workOrderAbcFiles = vm.workOrderDTO.workOrderAbcFiles;
                vm.workOrderAbcHdds = vm.workOrderDTO.workOrderAbcHdds;
                vm.workOrdersAdminRelations = vm.workOrderDTO.workOrdersAdminRelations;
                Projects.get({id: vm.workOrderDTO.workOrder.project.id}, function (result) {
                    vm.projectsDTO = result;
                    vm.projects = vm.projectsDTO.projects;
                    vm.purchaseOrders = vm.projectsDTO.projectPurchaseOrderses;
                    vm.labs = vm.projectsDTO.projectLabTaskses;
                });
                if (angular.equals(vm.workOrderDTO.workOrder.type.textValue, 'PKO')) {
                    vm.showPKOFlag = true;
                }
            });
        };
        var unsubscribe = $rootScope.$on('smartLpcApp:workOrderUpdate', function (event, result) {
            vm.workOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
*/


        vm.printDiv = function () {
            var printContents = document.getElementById('printIT').innerHTML;

            var mywindow = window.open('', 'PRINT', 'height=800,width=1000');

            mywindow.document.write('<html><head><title>' + document.title + '</title>');

            mywindow.document.write('</head><body >');
            mywindow.document.write('<h2 style="color: dodgerblue;">WorkOrder #' + vm.workOrderDTO.workOrder.id + '</h2>');
            mywindow.document.write('<hr>');
            mywindow.document.write('<h4 style="color: dodgerblue;"><u>WorkOrder</u></h4>');


            mywindow.document.write('<table>');

            mywindow.document.write('<tr>');
            mywindow.document.write('<td width="200"><strong><small>Project Name</small></strong></td>');
            mywindow.document.write('<td width="50"><strong><small>Type</small></strong></td>');
            mywindow.document.write('<td width="50"><strong><small>Print</small></strong></td>');
            mywindow.document.write('<td width="50"><strong><small>Proof</small></strong></td>');
            mywindow.document.write('<td width="50"><strong><small>ABC</small></strong></td>');
            mywindow.document.write('<td width="130"><strong><small>Assigned To</small></strong></td>');
            mywindow.document.write('<td width="130"><strong><small>Priority</small></strong></td>');
            mywindow.document.write('</tr>');

            mywindow.document.write('<tr>');
            mywindow.document.write('<td><small>' + vm.workOrderDTO.workOrder.project.fullName + '</small></td>');
            if (vm.workOrderDTO.workOrder.type === null) {
                mywindow.document.write('<td><small></small></td>');
            }
            else {
                mywindow.document.write('<td><small>' + vm.workOrderDTO.workOrder.type.textValue + '</small></td>');
            }


            if(vm.workOrderDTO.workOrder.isPrint === true){
                mywindow.document.write('<td><small><input type="checkbox" align="center" checked></small></td>');
            }else{
                mywindow.document.write('<td><small><input type="checkbox" align="center"></small></td>');
            }
            if(vm.workOrderDTO.workOrder.isProof === true){
                mywindow.document.write('<td><small><input type="checkbox" align="center" checked></small></td>');
            }else{
                mywindow.document.write('<td><small><input type="checkbox" align="center"></small></td>');
            }
            if(vm.workOrderDTO.workOrder.isAbc === true){
                mywindow.document.write('<td><small><input type="checkbox" align="center" checked></small></td>');
            }else{
                mywindow.document.write('<td><small><input type="checkbox" align="center"></small></td>');
            }

            if (vm.workOrderDTO.workOrder.assignedToUser === null) {
                mywindow.document.write('<td><small></small></td>');
            } else {
                mywindow.document.write('<td><small>' + vm.workOrderDTO.workOrder.assignedToUser.fullName + '</small></td>');
            }

            if (vm.workOrderDTO.workOrder.priority === null) {
                mywindow.document.write('<td><small></small></td>');
            } else {
                mywindow.document.write('<td><small>' + vm.workOrderDTO.workOrder.priority.textValue + '</small></td>');
            }

            mywindow.document.write('</tr></table>');


            ///////////


            if (vm.workOrderDTO.workOrder.type.textValue === 'PKO') {
                mywindow.document.write('<h4 style="color: dodgerblue;"><u>Processing</u></h4>');
                mywindow.document.write('<table>');
                mywindow.document.write('<tr>');
                mywindow.document.write('<td><strong><small>Date Recieved</small></strong></td>');
                mywindow.document.write('<td width="130"><small>'+ vm.workOrderDTO.workOrder.processingDateRecieved+'</small></td>');
                mywindow.document.write('<td><strong><small>HDD ID</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td></tr>');

                mywindow.document.write('<tr>');
                mywindow.document.write('<td><strong><small>Date Shipped</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td>');
                mywindow.document.write('<td><strong><small>Proof Shipped</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td>');
                mywindow.document.write('<td><strong><small>PKO</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td></tr>');


                mywindow.document.write('<tr>');
                mywindow.document.write('<td><strong><small>Notes</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td>');
                mywindow.document.write('<td><strong><small>Location</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td></tr>');

                mywindow.document.write('<tr>');
                mywindow.document.write('<td><strong><small>Image Range</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td>');
                mywindow.document.write('<td><strong><small>Image Qty</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td></tr></table>');

                mywindow.document.write('<h4 style="color: dodgerblue;"><u>Lab</u></h4>');
                mywindow.document.write('<table>');
                for (var i = 0; i < vm.labs.length; i++) {
                    mywindow.document.write('<tr><td><small>' + vm.labs[i].textValue + '</small></td></tr>');
                }
                mywindow.document.write('<tr><td></td></tr>');
                mywindow.document.write('<tr>');
                mywindow.document.write('<td><strong><small>Image Number Schema</small></strong></td>');
                if(vm.workOrderDTO.workOrder.project.labImageNumberSchema === null){
                    mywindow.document.write('<td width="130"><small></small></td>');
                }else {
                    mywindow.document.write('<td width="130"><small>' + vm.workOrderDTO.workOrder.project.labImageNumberSchema + '</small></td>');
                }
                mywindow.document.write('<td><strong><small>Folder/Batch Schema</small></strong></td>');
                if(vm.workOrderDTO.workOrder.project.labFolderBatchSchema === null){
                    mywindow.document.write('<td width="130"><small></small></td>');
                }else {
                    mywindow.document.write('<td width="230"><small>' + vm.workOrderDTO.workOrder.project.labFolderBatchSchema + '</small></td></tr>');
                }

                mywindow.document.write('</table>');
                mywindow.document.write('<br>');

                mywindow.document.write('<table>');
                mywindow.document.write('<tr>');
                mywindow.document.write('<td><strong><small>Verified</small></strong></td>');
                mywindow.document.write('<td width="200"><small></small></td>');
                mywindow.document.write('<td><strong><small>Processed</small></strong></td>');
                mywindow.document.write('<td width="200"><small></small></td>');
                mywindow.document.write('<td><strong><small>Ingest By</small></strong></td>');
                mywindow.document.write('<td width="200"><small></small></td></tr>');

                mywindow.document.write('<td><strong><small>Print</small></strong></td>');
                mywindow.document.write('<td width="200"><small></small></td>');
                mywindow.document.write('<td><strong><small>Upload/HDD Transfer</small></strong></td>');
                mywindow.document.write('<td width="200"><small></small></td>');
                mywindow.document.write('<td><strong><small>Archived</small></strong></td>');
                mywindow.document.write('<td width="200"><small></small></td></tr>');

                mywindow.document.write('</table>');

            }




            mywindow.document.write('<hr>');
            mywindow.document.write('<h4 style="color: dodgerblue;"><u>Requestor</u></h4>');

            mywindow.document.write('<table>');
            mywindow.document.write('<tr>');
            if (vm.workOrderDTO.workOrder.requestor !== null) {
                mywindow.document.write('<td><strong><small>Name</small></strong></td>');
                mywindow.document.write('<td width="130"><small>' + vm.workOrderDTO.workOrder.requestor.fullName + '</small></td>');

                mywindow.document.write('<td><strong><small>Company</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td></tr>');

                mywindow.document.write('<tr><td><strong><small>Office Phone</small></strong></td>');
                mywindow.document.write('<td width="130"><small>' + vm.workOrderDTO.workOrder.requestor.phoneOffice + '</small></td>');

                mywindow.document.write('<td><strong><small>Email</small></strong></td>');
                mywindow.document.write('<td width="130"><small>' + vm.workOrderDTO.workOrder.requestor.email + '</small></td></tr></table>');

            }
            mywindow.document.write('<hr>');
            mywindow.document.write('<h4 style="color: dodgerblue;"><u>Work</u></h4>');
            mywindow.document.write('<table>');
            mywindow.document.write('<tr><td><strong><small>Request Date</small></strong></td></tr>');

            if (vm.workOrderDTO.workOrder.requestDate !== null) {
                mywindow.document.write('<tr><td><small>' + vm.workOrderDTO.workOrder.requestDate + '</small></td></tr>');
            } else {
                mywindow.document.write('<tr><td><small> - </small></td></tr>');
            }

            mywindow.document.write('<tr><td><strong><small>Work Description</small></strong></td></tr>');
            if (vm.workOrderDTO.workOrder.requestDescription !== null) {
                mywindow.document.write('<tr><td><small>' + vm.workOrderDTO.workOrder.requestDescription + '</small></td></tr></table>');
            } else {
                mywindow.document.write('<tr><td><small> - </small></td></tr></table>');
            }
            mywindow.document.write('<hr>');
            if (vm.workOrderDTO.workOrder.isPrint === true) {
                mywindow.document.write('<h4 style="color: dodgerblue;"><u>Printing</u></h4>');
                mywindow.document.write('<table>');

                mywindow.document.write('<tr>');
                mywindow.document.write('<td><strong><small>Size</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td>');

                mywindow.document.write('<td><strong><small>Surface</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td>');

                mywindow.document.write('<td><strong><small>Bleed</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td></tr>');

                mywindow.document.write('<tr>');
                mywindow.document.write('<td><strong><small>Filename</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td>');

                mywindow.document.write('<td><strong><small>Position</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td>');

                mywindow.document.write('<td><strong><small>Photo Credit</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td>');

                mywindow.document.write('<td><strong><small>Credit Location</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td></tr></table>');


                mywindow.document.write('<h4 style="color: dodgerblue;"><u>Quantity</u></h4>');
                mywindow.document.write('<table>');

                mywindow.document.write('<tr>');
                mywindow.document.write('<td><strong><small>Sets</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td>');

                mywindow.document.write('<td><strong><small>Of Quantity</small></strong></td>');
                mywindow.document.write('<td width="130"><small></small></td></tr></table>');
                mywindow.document.write('<hr>');
            }


            mywindow.document.write('<table>');

            mywindow.document.write('<tr>');
            mywindow.document.write('<td><strong><small>Status</small></strong></td>');
            mywindow.document.write('<td width="130"><small>' + vm.workOrderDTO.workOrder.status.textValue + '</small></td>');

            mywindow.document.write('<td><strong><small>Completion</small></strong></td>');
            mywindow.document.write('<td><small>' + vm.workOrderDTO.workOrder.completionDate + '</small></td>');

            mywindow.document.write('<td><strong><small>Duration of Service</small></strong></td>');
            mywindow.document.write('<td width="130"><small>' + vm.workOrderDTO.workOrder.durationOfService + '</small></td></tr>');

            mywindow.document.write('<tr><td><strong><small>Audited</small></strong></td>');
            if (vm.workOrderDTO.workOrder.auditedFlag === 220) {
                mywindow.document.write('<td width="130"><small>Yes</small></td>');
            } else {
                mywindow.document.write('<td width="130"><small>No</small></td>');
            }

            mywindow.document.write('<td><strong><small>Audited By</small></strong></td>');
            if (vm.workOrderDTO.workOrder.auditedBy !== null) {
                mywindow.document.write('<td><small>' + vm.workOrderDTO.workOrder.auditedBy.fullName + '</small></td></tr></table>');
            }

            mywindow.document.write('<hr>');
            mywindow.document.write('<table>');
            mywindow.document.write('<tr><td><strong><small>PO Record Field</small></strong></td></tr>');
            if (vm.workOrderDTO.workOrder.poRecord !== null) {
                mywindow.document.write('<tr><td><small>' + vm.workOrderDTO.workOrder.poRecord + '</small></td></tr></table>');
            } else {
                mywindow.document.write('<tr><td><small></small></td></tr></table>');
            }


            mywindow.document.write('<hr>');
            mywindow.document.write('<h4 style="color: dodgerblue;"><u>Accounting</u></h4>');
            mywindow.document.write('<table>');
            mywindow.document.write('<tr><td><strong><small>Invoiced</small></strong></td>');

            if (vm.workOrderDTO.workOrder.invoiced === 105) {
                mywindow.document.write('<td><small>Yes</small></td></tr>');
            } else if (vm.workOrderDTO.workOrder.invoiced === 106) {
                mywindow.document.write('<td><small>Comp</small></td></tr>');
            } else if (vm.workOrderDTO.workOrder.invoiced === 107) {
                mywindow.document.write('<td><small>Included</small></td></tr>');
            } else if (vm.workOrderDTO.workOrder.invoiced === 108) {
                mywindow.document.write('<td><small>No</small></td></tr>');
            }

            mywindow.document.write('<tr><td><strong><small>P.O.#</small></strong></td>');

            if (vm.workOrderDTO.workOrder.invoiceNumber !== null) {
                mywindow.document.write('<td><small>'+vm.workOrderDTO.workOrder.invoiceNumber+'</small></td></tr></table>');
            } else {
                mywindow.document.write('<td><small></small></td></tr></table>');
            }


            mywindow.document.write('</body></html>');

            mywindow.document.close(); // necessary for IE >= 10
            mywindow.focus(); // necessary for IE >= 10*!/

            mywindow.print();
            mywindow.close();

            return true;

        };


    }
})();
