(function() {
	'use strict';

	angular.module('smartLpcApp').controller('ProjectsDetailController',
			ProjectsDetailController);

	ProjectsDetailController.$inject = [ '$http', '$state', '$uibModal',
			'$scope', '$rootScope', '$stateParams', 'entity', 'Projects',
			'Lookups', 'Contacts', 'User', 'Departments', 'Storage_Disk' ];

	function ProjectsDetailController($http, $state, $uibModal, $scope,
			$rootScope, $stateParams, entity, Projects, Lookups, Contacts,
			User, Departments, Storage_Disk) {

	    console.log('Project Detail Controller');
        $(document).ready(function(){
            $(this).scrollTop(0);
        });



	    var vm = this;

        vm.load = function (id) {
            Projects.get({id: id}, function(result) {
                vm.projectsDTO = result;
               // console.log("FROM LOAD : "+JSON.stringify(vm.projectsDTO))
            });
        };
        vm.load(entity.projects.id);

        vm.projectsDTO = entity;
        	//console.log("projectsDTO " + JSON.stringify(vm.projectsDTO));

        $scope.isGeneric = function (tags) {
            return tags.contact.fullName !== 'generic pkotag';
        };

        $scope.isExecORAlbumViewer = function (execs) {
          return  (execs.exec === true || execs.restartRole === 'ALBUMVIEWER') && (execs.internal === false) ;
        };

        vm.noExec = true;
        /*
         * console.log("projectsDTO : " + JSON.stringify(vm.projectsDTO));
         * vm.tags = []; vm.projectRoles = vm.projectsDTO.projectRoles;
         * vm.contactPrivileges = vm.projectsDTO.contactPrivileges;
         * vm.purchaseOrders = vm.projectsDTO.projectPurchaseOrderses; vm.labs =
         * vm.projectsDTO.projectLabTaskses; console.log("lab : " +
         * JSON.stringify(vm.labs)); console.log("po : " +
         * JSON.stringify(vm.purchaseOrders))
         *
         * if (vm.projectRoles) { for (var i = 0; i < vm.projectRoles.length;
         * i++) { console.log("iterated "); if
         * (angular.equals(vm.projectRoles[i].relationship_type, "Main
         * Contact")) { console.log("main contact added"); vm.mainC =
         * vm.projectRoles[i]; } else if
         * (angular.equals(vm.projectRoles[i].relationship_type, "Unit
         * Publicist")) { console.log("u pub added"); vm.uPub =
         * vm.projectRoles[i]; } if
         * (angular.equals(vm.projectRoles[i].relationship_type, "Unit
         * Photographer")) { console.log("u photo added"); vm.uPhoto =
         * vm.projectRoles[i]; } if
         * (angular.equals(vm.projectRoles[i].relationship_type, "Lab")) {
         * console.log("labs added"); vm.lab = vm.projectRoles[i]; } if
         * (angular.equals(vm.projectRoles[i].relationship_type, "PKO_Tag")) {
         * console.log("tags added"); vm.tags.push(vm.projectRoles[i]);
         * console.log("-----> " + JSON.stringify(vm.tag)); } } }
         * console.log(JSON.stringify(vm.projectsDTO)); vm.load = function (id) {
         * Projects.get({id: id}, function (result) { vm.projectsDTO = result;
         *
         * vm.projectRoles = vm.projectsDTO.projectRoles; vm.contactPrivileges =
         * vm.projectsDTO.contactPrivileges; vm.purchaseOrders =
         * vm.projectsDTO.projectPurchaseOrderses; vm.labs =
         * vm.projectsDTO.projectLabTaskses; console.log("lab : " +
         * JSON.stringify(vm.labs)); console.log("po : " +
         * JSON.stringify(vm.purchaseOrders)) if (vm.projectRoles) { for (var i =
         * 0; i < vm.projectRoles.length; i++) { console.log("iterated "); if
         * (angular.equals(vm.projectRoles[i].relationship_type, "Main
         * Contact")) { console.log("main contact added"); vm.mainC =
         * vm.projectRoles[i]; } else if
         * (angular.equals(vm.projectRoles[i].relationship_type, "Unit
         * Publicist")) { console.log("u pub added"); vm.uPub =
         * vm.projectRoles[i]; } if
         * (angular.equals(vm.projectRoles[i].relationship_type, "Unit
         * Photographer")) { console.log("u photo added"); vm.uPhoto =
         * vm.projectRoles[i]; } if
         * (angular.equals(vm.projectRoles[i].relationship_type, "Lab")) {
         * console.log("labs added"); vm.lab = vm.projectRoles[i]; } if
         * (angular.equals(vm.projectRoles[i].relationship_type, "PKO_Tag")) {
         * console.log("tags added"); vm.tags = vm.projectRoles[i];
         * console.log("-----> " + JSON.stringify(vm.tag)); } } }
         *
         * }); };
         */
        /*vm.relatedContact = [];
        vm.expandedInfo ={"talent": null,"related": null};
        for (var i = 0; i < vm.projectsDTO.projectRoles.length; i++) {
            var talent = null;
            // for (var taggs in vm.projectsDTO.projectRoles) {
            console.log("inside for");
            if (angular
                    .equals(vm.projectsDTO.projectRoles[i].relationship_type,
                            'PKO_Tag')) {
                // get related too.
                console.log(" get releated  : "
                        + vm.projectsDTO.projectRoles[i].contact.id);
                talent = vm.projectsDTO.projectRoles[i].contact;
                console.log("Talent : "+JSON.stringify(talent));
                $http({method : 'GET',url : 'api/contacts/related/'+ vm.projectsDTO.projectRoles[i].contact.id})
                    .then(function successCallback(response) {
                    var related = response.data
                    console.log("Related : "+JSON.stringify(related));
                    vm.expandedInfo = {
                            "talent": talent,
                            "related": related
                        };
                    vm.relatedContact.concat(vm.expandedInfo);

                }, function errorCallback(response) {

                });
            }

            if(i == vm.projectsDTO.projectRoles.length-1){
                console.log("LAst object");
                console.log("Final Data: "+JSON.stringify(vm.relatedContact));
            }
        }*/

        var unsubscribe = $rootScope.$on('smartLpcApp:projectsUpdate',
            function (event, result) {
                vm.projectsDTO = result;
            });
        $scope.$on('$destroy', unsubscribe);

      /*  vm.sendIndividualMail = function (talent) {

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/mails.html',
                controller: 'MailsIndividualController',
                size: 'lg',
                scope: $scope,
                controllerAs: 'vm',
                backdrop: 'static',
                resolve: {

                    talent: function () {

                        return talent;
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

        // send multiple email
        vm.sendMail = function (talents) {

            var modalInstance = $uibModal.open({

                templateUrl: 'app/entities/projects/mails.html',
                controller: 'MailsController',
                size: 'lg',
                scope: $scope,
                controllerAs: 'vm',
                backdrop: 'static',
                resolve: {

                    talents: function () {

                        return talents;
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

        };*/

        vm.addSimilar = function (id) {
            console.log("similar to add id :", id);
            $state.go('projects.similar-add', {
                id: id
            }, {
                reload: true
            });
        };
        vm.relatedContacts = [];

        $http({
            method: 'GET', url: 'api/contacts/related/' + vm.projectsDTO.projects.id
        }).then(function successCallback(response) {
            vm.relatedContacts = response.data;
            console.log("Total Related Contact found  : "+vm.relatedContacts.length);
        }, function
            errorCallback(response) {

        });


        vm.sortProjectRoles = function () {
            for (var i = 0; i < vm.projectsDTO.projectRoles.length; i++) {
                console.log("Relationship " + i + "  --> " + vm.projectsDTO.projectRoles[i].relationship_type);
                if (angular.equals(vm.projectsDTO.projectRoles[i].relationship_type, 'Main Contact')) {

                    vm.mainC = vm.projectsDTO.projectRoles[i];
                } else if (angular.equals(vm.projectsDTO.projectRoles[i].relationship_type, 'Unit Publicist')) {
                    vm.uPub = vm.projectsDTO.projectRoles[i];
                } else if (angular.equals(vm.projectsDTO.projectRoles[i].relationship_type, 'Unit Photographer')) {
                    vm.uPhoto = vm.projectsDTO.projectRoles[i];
                } else if (angular.equals(vm.projectsDTO.projectRoles[i].relationship_type, 'Lab')) {
                    vm.lab = vm.projectsDTO.projectRoles[i];
                }
            }
        };
        vm.sortProjectRoles();

    }


})();
