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
});

app.controller('projectDetailController', function($q, $scope, $routeParams, projectDetailFactory) {
	var id = $routeParams.id;
	$scope.id = $routeParams.id;
	$scope.ctrl = this;
	
	$scope.init = function() {
		$scope.get();
		$scope.getPacakges();
	};
	
	$scope.getPacakges = function() {
		$scope.ctrl.displayed = [];
		$scope.ctrl.callServer = function callServer(params) {
			$scope.ctrl.isLoading = true;
			$scope.ctrl.params = params;
			var pagination = params.pagination;
			var start = pagination.start || 0;
			var number = pagination.number || 10;
			projectDetailFactory.getFiles(id, start, number, params).then(function(result) {
				$scope.ctrl.displayed = result.data;
				params.pagination.numberOfPages = result.numberOfPages;
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
		var deferred = $q.defer();
		projectDetailFactory.delete(id).success(function(data, status, headers, config) {
			console.log(data);
			deferred.resolve();
		}).error(function(data, status, headers, config) {
			console.log(status);
		});
		
		deferred.promise.then(function() {
			console.log("done");
			$scope.ctrl.displayed = [];
			$scope.ctrl.callServer($scope.ctrl.params);
		});
	};

	$scope.download = function(pkgId) {
		window.location = '/package/' + pkgId;
	};
	$scope.build = function() {
		projectDetailFactory.build($scope.id).then(function() {
			console.log("done");
			$scope.ctrl.displayed = [];
            $scope.ctrl.callServer($scope.ctrl.params);
            alert('빌드완료! 목록에서 패키지를 확인하세요.');
		});
	};
	$scope.standardbuild = function() {
		alert('아직 동작하지 않는 기능입니다. 빨리 만들어 놓을께요...');
//		projectDetailFactory.standardbuild($scope.id);
	};
	$scope.checksource = function() {
		projectDetailFactory.checksource($scope.id).then(function() {
			console.log("done");
			$scope.ctrl.displayed = [];
			$scope.ctrl.callServer($scope.ctrl.params);
			alert('소스 비교 완료! 로그에서 에러를 확인하세요.');
		});
	};
	$scope.mobile = function() {
		alert('아직 동작하지 않는 기능입니다. 빨리 만들어 놓을께요...');
	};

});
