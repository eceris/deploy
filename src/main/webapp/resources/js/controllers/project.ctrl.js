//DO 이벤트 컨트롤러
app.controller('ProjectCtrl', function($scope, $http, $modal, projectFactory, $log, cfpLoadingBar) {

	var ctrl = this;
	ctrl.displayed = [];
	ctrl.callServer = function callServer(tableState) {
		ctrl.isLoading = true;
		var pagination = tableState.pagination;
		var start = pagination.start || 0; // This is NOT the page number, but
		var number = pagination.number || 10; // Number of entries showed per
		projectFactory.getProjectList(start, number, tableState).then(function(result) {
			ctrl.displayed = result.data;
			tableState.pagination.numberOfPages = result.numberOfPages;
			ctrl.isLoading = false;
		});
	};

	$scope.buildProject = function(project) {
		$modal.open({
			templateUrl : '/resources/partials/dialogView/projectSuccess.html',
			size : 'lg',
			controller : 'ModalController',
			resolve : {
				data : function() {
					return;
				}
			}
		});
		projectFactory.buildProject(project);
	};

	$scope.scanCustomFile = function(project) {
		$modal.open({
			templateUrl : '/resources/partials/dialogView/projectSuccess.html',
			size : 'lg',
			controller : 'ModalController',
			resolve : {
				data : function() {
					return;
				}
			}
		});
		projectFactory.scanCustomFile(project);
	};

	$scope.buildStandardProject = function(project) {

		var modalInstance = $modal.open({
			templateUrl : '/resources/partials/dialogView/buildStandardProject.html',
			controller : 'projectModalCtrl',
			size : 'lg',
			resolve : {
				project : function() {
					return project;
				}
			}
		});
		modalInstance.result.then(function() {});
	};

	$scope.mobileBuild = function(project) {
		projectFactory.mobileBuild(project);
	};

});

app.controller('projectModalCtrl', function($scope, $modalInstance, projectFactory, project, $modal) {
	$scope.project = project;

	$scope.buildStandardProject = function(version, branch) {
		if (!(version || 0)) {
			alert('DO 버전을 입력하세요.')
			return;
		}
		if (!(branch || 0)) {
			alert('브랜치 명을 입력하세요.')
			return;
		}
		projectFactory.buildStandardProject($scope.project, version, branch);
	};
	$scope.ok = function() {
		$modalInstance.close();
	};
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
});

app.controller('projectDetailController', function($scope, $routeParams, projectDetailFactory) {
	var id = $routeParams.id;
	$scope.id = $routeParams.id;
	$scope.ctrl = this;
	
	$scope.init = function() {
		$scope.get();
		$scope.getPacakges();
	};
	$scope.getPacakges = function() {
		$scope.ctrl.displayed = [];
		$scope.ctrl.callServer = function callServer(tableState) {
			$scope.ctrl.isLoading = true;
			var pagination = tableState.pagination;
			var start = pagination.start || 0;
			var number = pagination.number || 10;
			projectDetailFactory.getFiles(id, start, number, tableState).then(function(result) {
				$scope.ctrl.displayed = result.data;
				tableState.pagination.numberOfPages = result.numberOfPages;
				$scope.ctrl.isLoading = false;
			});
		};
	};
	
		

	$scope.get = function() {
		projectDetailFactory.get(id).success(function(data, status, headers, config) {
			console.log(data);
			$scope.data = data;
		}).error(function(data, status, headers, config) {
			console.log(status);
		});
	};

	$scope.delete = function(id) {
		projectDetailFactory.delete(id).success(function(data, status, headers, config) {
			console.log(data);
		}).error(function(data, status, headers, config) {
			console.log(status);
		});
	};

	$scope.download = function(pkgId) {
		window.location = '/package/' + pkgId;
	};
	$scope.build = function() {
		projectDetailFactory.build($scope.id).then(function() {
			console.log("done");
			$scope.ctrl = {};
			$scope.getPacakges();
		});
	};
	$scope.standardbuild = function() {
		alert('아직 동작하지 않는 기능입니다. 빨리 만들어 놓을께요...');
//		projectDetailFactory.standardbuild($scope.id);
	};
	$scope.checksource = function() {
		projectDetailFactory.checksource($scope.id).then(function() {
			console.log("done");
			$scope.ctrl = {};
			$scope.getPacakges();
		});
	};
	$scope.mobile = function() {
		alert('아직 동작하지 않는 기능입니다. 빨리 만들어 놓을께요...');
	};

});
